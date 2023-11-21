package com.v2p.swp391.application.service;

import com.v2p.swp391.application.request.CalculateFeeRequest;
import com.v2p.swp391.application.request.CalculateLeadtimeRequest;
import com.v2p.swp391.application.request.GetShippingServiceRequest;

public interface ShipmentService {
    Object getProvince();
    Object getDistrict(int provinceId);
    Object getWard(int districtId);
    Object calculateLeadtime(CalculateLeadtimeRequest request);
    Object getShippingServices(GetShippingServiceRequest request);
    Object calculateFee(CalculateFeeRequest request);
    Object getAllShop(int offset, int limit);
    Object getShopById(int id);
}
