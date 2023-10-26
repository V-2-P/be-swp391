package com.v2p.swp391.application.service;

import com.v2p.swp391.application.model.Category;
import com.v2p.swp391.application.model.ShippingMethod;
import com.v2p.swp391.application.request.ShippingMethodRequest;

import java.util.List;

public interface ShippingMethodService {

    ShippingMethod createShippingMethod(ShippingMethod shippingMethod) ;
    ShippingMethod getShippingMethodById(long id);
    List<ShippingMethod> getAllShippingMethod();
    ShippingMethod updateShippingMethod(long shippingMethodId, ShippingMethodRequest shippingMethod);
    void deleteShippingMethod(long id);
}
