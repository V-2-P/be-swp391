package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.request.CalculateFeeRequest;
import com.v2p.swp391.application.request.CalculateLeadtimeRequest;
import com.v2p.swp391.application.request.GetShippingServiceRequest;
import com.v2p.swp391.application.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.api.version.v1}/shipments")
@RequiredArgsConstructor
public class ShipmentController {
    private final ShipmentService shipmentService;

    @GetMapping("/province")
    public ResponseEntity<Object> getProvince() {
        return ResponseEntity.ok(shipmentService.getProvince());
    }

    @GetMapping("/district")
    public ResponseEntity<Object> getDistrict(@RequestParam(name = "province_id") int provinceId) {
        return ResponseEntity.ok(shipmentService.getDistrict(provinceId));
    }

    @GetMapping("/ward")
    public ResponseEntity<Object> getWard(@RequestParam(name = "district_id") int districtId) {
        return ResponseEntity.ok(shipmentService.getWard(districtId));
    }

    @PostMapping("/leadtime")
    public ResponseEntity<Object> calculateLeadtime(@RequestBody CalculateLeadtimeRequest request) {
        return ResponseEntity.ok(shipmentService.calculateLeadtime(request));
    }

    @PostMapping("/available-services")
    public ResponseEntity<Object> getAvailableServices(@RequestBody GetShippingServiceRequest request) {
        return ResponseEntity.ok(shipmentService.getShippingServices(request));
    }

    @PostMapping("/fee")
    public ResponseEntity<Object> calculateFee(@RequestBody CalculateFeeRequest request) {
        return ResponseEntity.ok(shipmentService.calculateFee(request));
    }

}
