package com.v2p.swp391.shipment;

import com.v2p.swp391.shipment.request.CreateOrderGHNRequest;

public interface CreateOrderStrategy<T> {
    T excute(Long id, CreateOrderGHNRequest request,GhnRestTemplate ghnRestTemplate);
}
