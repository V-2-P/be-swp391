package com.v2p.swp391.shipment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.v2p.swp391.application.model.Bird;
import com.v2p.swp391.application.model.OrderDetail;
import com.v2p.swp391.application.repository.OrderRepository;
import com.v2p.swp391.shipment.model.OrderItem;
import com.v2p.swp391.shipment.model.Shop;
import com.v2p.swp391.shipment.request.*;
import com.v2p.swp391.common.api.CoreResponse;
import com.v2p.swp391.exception.AppException;
import com.v2p.swp391.shipment.response.CreateOrderReponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ShipmentImpl implements ShipmentService {

    private final Map<CreateStrategyType,CreateOrderStrategy> createOrderStrategies;
    private final Integer shopId;
    private final String token;
    private final GhnRestTemplate ghnRestTemplate;

    public ShipmentImpl(Map<CreateStrategyType, CreateOrderStrategy> strategies) {
        this.createOrderStrategies = strategies;
        this.shopId = 190386;
        this.token = "c7e6272f-8ab6-11ee-b1d4-92b443b7a897";
        this.ghnRestTemplate = new GhnRestTemplate(token,shopId);
    }

    @Override
    public Object getProvince() {
        try {
            Object jsonResponse = ghnRestTemplate.getForObject("/shiip/public-api/master-data/province", Object.class);

            return jsonResponse;
        } catch (Exception e) {
            throw new AppException(HttpStatus.BAD_REQUEST,"Lỗi GHN");
        }
    }

    @Override
    public Object getDistrict(int provinceId) {
        try {
            Object jsonResponse = ghnRestTemplate.getForObject("/shiip/public-api/master-data/district?province_id="+provinceId, Object.class);

            return jsonResponse;
        } catch (Exception e) {
            throw new AppException(HttpStatus.BAD_REQUEST,"Lỗi GHN");
        }
    }

    @Override
    public Object getWard(int districtId) {
        try {
            Object jsonResponse = ghnRestTemplate.getForObject("/shiip/public-api/master-data/ward?district_id="+districtId, Object.class);

            return jsonResponse;
        } catch (Exception e) {
            throw new AppException(HttpStatus.BAD_REQUEST,"Lỗi GHN");
        }
    }

    @Override
    public Object calculateLeadtime(CalculateLeadtimeRequest request) {
        try {
            Object shopResponseObject = getShopById(shopId);
            CoreResponse<Shop> shopReponse = convertObjectToValue(shopResponseObject,CoreResponse.class);
            Shop shop= convertObjectToValue(shopReponse.getData(),Shop.class);

            request.setFrom_district_id(shop.getDistrictId());
            request.setFrom_ward_code(shop.getWardCode());

            HttpEntity<CalculateLeadtimeRequest> requestEntity = new HttpEntity<>(request);
            Object jsonResponse = ghnRestTemplate.postForObject(
                    "/shiip/public-api/v2/shipping-order/leadtime",
                    requestEntity,
                    Object.class
            );
            return jsonResponse;
        } catch (Exception e) {
            throw new AppException(HttpStatus.BAD_REQUEST,"Lỗi GHN");
        }
    }

    @Override
    public Object getShippingServices(GetShippingServiceRequest request) {
        try {
            Object shopResponseObject = getShopById(shopId);
            CoreResponse<Shop> shopReponse = convertObjectToValue(shopResponseObject,CoreResponse.class);
            Shop shop= convertObjectToValue(shopReponse.getData(),Shop.class);

            request.setShop_id(shopId);
            request.setFrom_district(shop.getDistrictId());

            HttpEntity<GetShippingServiceRequest> requestEntity = new HttpEntity<>(request);
            Object jsonResponse = ghnRestTemplate.postForObject(
                    "/shiip/public-api/v2/shipping-order/available-services",
                    requestEntity,
                    Object.class
            );
            return jsonResponse;
        } catch (Exception e) {
            throw new AppException(HttpStatus.BAD_REQUEST,"Lỗi GHN");
        }
    }

    @Override
    public Object calculateFee(CalculateFeeRequest request) {
        try {
            // hard code
            request.setHeight(15);
            request.setLength(15);
            request.setWidth(15);
            request.setWeight(5000);
            HttpEntity<CalculateFeeRequest> requestEntity = new HttpEntity<>(request);
            Object jsonResponse = ghnRestTemplate.postForObject(
                    "/shiip/public-api/v2/shipping-order/fee",
                    requestEntity,
                    Object.class
            );
            return jsonResponse;
        } catch (Exception e) {
            throw new AppException(HttpStatus.BAD_REQUEST,"Lỗi GHN");
        }
    }

    @Override
    public Object getShopById(int id) {
        try {
            String query = "?id="+id;
            Object jsonResponse = ghnRestTemplate.getForObject("/shiip/public-api/v2/shop"+query, Object.class);

            return jsonResponse;
        } catch (Exception e) {
            throw new AppException(HttpStatus.BAD_REQUEST,"Lỗi GHN");
        }
    }

    @Override
    public Object createOrder(CreateOrderRequest request) {
        CreateOrderStrategy strategy = createOrderStrategies.get(CreateStrategyType.valueOf(request.getStrategyType()));
        if (strategy == null) {
            throw new AppException(HttpStatus.BAD_REQUEST,"Chiến lược không tồn tại: " + request.getStrategyType());
        }
        Object shopResponseObject = getShopById(shopId);
        CoreResponse<Shop> shopReponse = convertObjectToValue(shopResponseObject,CoreResponse.class);
        Shop shop= convertObjectToValue(shopReponse.getData(),Shop.class);

        CreateOrderGHNRequest ghnRequest = new CreateOrderGHNRequest();
        // hard code
        ghnRequest.setPayment_type_id(1);
        ghnRequest.setNote(request.getNote());
        ghnRequest.setRequired_note("CHOXEMHANGKHONGTHU");
        ghnRequest.setFrom_name(shop.getName());
        ghnRequest.setFrom_phone(shop.getPhone());
        ghnRequest.setFrom_address(shop.getAddress());
        ghnRequest.setFrom_ward_name("Phường Linh Chiểu");
        ghnRequest.setFrom_district_name("Thành Phố Thủ Đức");
        ghnRequest.setFrom_province_name("Hồ Chí Minh");

        return strategy.excute(request.getId(),ghnRequest,ghnRestTemplate);
    }

    private <T> T convertObjectToValue(Object object, Class<T> clazz){
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(object, clazz);
    }
}
