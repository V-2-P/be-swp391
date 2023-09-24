package com.v2p.swp391.exception;

import com.v2p.swp391.exception.data.ResourceNotFound;
import lombok.Data;

@Data
public class ResourceNotFoundException extends RuntimeException {
    private ResourceNotFound data;

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        data = new ResourceNotFound(resourceName,fieldName,fieldValue);
    }

    public String getResourceName() {
        return data.getResourceName();
    }

    public String getFieldName() {
        return data.getFieldName();
    }

    public Object getFieldValue() {
        return data.getFieldValue();
    }
}
