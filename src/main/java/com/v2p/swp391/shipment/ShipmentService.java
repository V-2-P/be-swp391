package com.v2p.swp391.shipment;

import com.v2p.swp391.shipment.request.*;

public interface ShipmentService {
    Object getProvince();
    Object getDistrict(int provinceId);
    Object getWard(int districtId);
    Object calculateLeadtime(CalculateLeadtimeRequest request);
    Object getShippingServices(GetShippingServiceRequest request);
    Object calculateFee(CalculateFeeRequest request);
    Object getShopById(int id);
    Object createOrder(CreateOrderRequest request);
}
