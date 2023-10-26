package com.v2p.swp391.application.service.impl;

import com.v2p.swp391.application.mapper.ShippingMethodHttpMapper;
import com.v2p.swp391.application.model.ShippingMethod;
import com.v2p.swp391.application.repository.ShippingMethodRepository;
import com.v2p.swp391.application.request.ShippingMethodRequest;
import com.v2p.swp391.application.service.ShippingMethodService;
import com.v2p.swp391.exception.AppException;
import com.v2p.swp391.utils.StringUtlis;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ShippingMethodImpl implements ShippingMethodService {
    private final ShippingMethodRepository shippingMethodRepository;
    private final ShippingMethodHttpMapper mapper;
    @Override
    public ShippingMethod createShippingMethod(ShippingMethod shippingMethod) {
        shippingMethod.setName(StringUtlis.NameStandardlizing(shippingMethod.getName()));
        if(shippingMethodRepository.existsByName(shippingMethod.getName())){
            throw new AppException(HttpStatus.BAD_REQUEST, "Shipping method name already exists");
        }
        return shippingMethodRepository.save(shippingMethod);
    }

    @Override
    public ShippingMethod getShippingMethodById(long id) {
        return shippingMethodRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Cannot find shipping method with id: " + id));
    }

    @Override
    public List<ShippingMethod> getAllShippingMethod() {

        return shippingMethodRepository.findAll();
    }

    @Override
    public ShippingMethod updateShippingMethod(long shippingMethodId, ShippingMethodRequest shippingMethod) {
        ShippingMethod existing = getShippingMethodById(shippingMethodId);
        mapper.updateShippingFromRequest(shippingMethod,existing);
        return shippingMethodRepository.save(existing);
    }

    @Override
    public void deleteShippingMethod(long id) {
        ShippingMethod existing = getShippingMethodById(id);
        shippingMethodRepository.deleteById(id);
    }
}
