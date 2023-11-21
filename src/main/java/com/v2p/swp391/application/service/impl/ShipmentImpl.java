package com.v2p.swp391.application.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.v2p.swp391.application.model.Shop;
import com.v2p.swp391.application.request.CalculateFeeRequest;
import com.v2p.swp391.application.request.CalculateLeadtimeRequest;
import com.v2p.swp391.application.request.GetShippingServiceRequest;
import com.v2p.swp391.application.service.ShipmentService;
import com.v2p.swp391.common.api.CoreResponse;
import com.v2p.swp391.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
@RequiredArgsConstructor
public class ShipmentImpl implements ShipmentService {

    private final RestTemplate ghnRestTemplate;
    private final Integer shopId;
    private final String token;
    public ShipmentImpl() {
        this.shopId=4710975;
        this.token="48159190-878d-11ee-96dc-de6f804954c9";
        this.ghnRestTemplate = new RestTemplate();

        // Thiết lập URI template handler cho RestTemplate
        ghnRestTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("https://online-gateway.ghn.vn"));

        // Tạo HttpHeaders cho RestTemplate
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", token);
        headers.set("ShopId", shopId.toString());

        // Tạo một ClientHttpRequestInterceptor để thêm header vào mỗi request
        ClientHttpRequestInterceptor interceptor = (request, body, execution) -> {
            request.getHeaders().addAll(headers);
            return execution.execute(request, body);
        };

        // Thêm interceptor vào RestTemplate
        ghnRestTemplate.getInterceptors().add(interceptor);
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
            ObjectMapper objectMapper = new ObjectMapper();
            Object shopResponseObject = getShopById(shopId);
            CoreResponse<Shop> shopReponse = objectMapper.convertValue(shopResponseObject, CoreResponse.class);
            Shop shop= objectMapper.convertValue(shopReponse.getData(), Shop.class);
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
    public Object getAllShop(int offset, int limit) {
        try {
            String query = "?offset="+offset + "&limit="+limit;
            Object jsonResponse = ghnRestTemplate.getForObject("/shiip/public-api/v2/shop/all"+query, Object.class);

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
}
