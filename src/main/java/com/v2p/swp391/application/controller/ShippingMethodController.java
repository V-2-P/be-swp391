package com.v2p.swp391.application.controller;




import com.v2p.swp391.application.model.BirdType;
import com.v2p.swp391.application.model.ShippingMethod;
import com.v2p.swp391.application.request.ShippingMethodRequest;
import com.v2p.swp391.application.service.ShippingMethodService;
import com.v2p.swp391.common.api.CoreApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.v2p.swp391.application.mapper.ShippingMethodHttpMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/shippingmethod")
@RequiredArgsConstructor
public class ShippingMethodController {
    private final ShippingMethodService shippingMethodService;

    @PostMapping("")
    public CoreApiResponse<ShippingMethod> createShippingMethod(@Valid @RequestBody ShippingMethodRequest request){
        ShippingMethod shippingMethod=shippingMethodService.createShippingMethod(INSTANCE.toModel(request));
        return CoreApiResponse.success(shippingMethod,"Insert shipping method successfully");
    }

    @GetMapping("")
    public CoreApiResponse<List<ShippingMethod>> getAllBirdTypes(){
        List<ShippingMethod> shippingMethods = shippingMethodService.getAllShippingMethod();
        return CoreApiResponse.success(shippingMethods);
    }

    @GetMapping("/{id}")
    public CoreApiResponse<ShippingMethod> getShippingMethodById(@Valid @PathVariable Long id){
        ShippingMethod shippingMethod = shippingMethodService.getShippingMethodById(id);
        return CoreApiResponse.success(shippingMethod);
    }

    @PutMapping("/{id}")
    public CoreApiResponse<ShippingMethod> updateShippingMethod(
            @PathVariable Long id,
            @RequestBody ShippingMethodRequest birdTypeRequest
    ){
        ShippingMethod update = shippingMethodService.updateShippingMethod(id,birdTypeRequest);
        return CoreApiResponse.success(update, "Update shipping method successfully");
    }
    @DeleteMapping("/{id}")
    public CoreApiResponse<ShippingMethod> deleteShippingMethod(
            @PathVariable Long id
    ){
         shippingMethodService.deleteShippingMethod(id);
        return CoreApiResponse.success("Delete shipping method successfully with data: "+id);
    }
}
