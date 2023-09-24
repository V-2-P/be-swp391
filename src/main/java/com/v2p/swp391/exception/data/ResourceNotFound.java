package com.v2p.swp391.exception.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResourceNotFound {
    private String resourceName;
    private String fieldName;
    private Object fieldValue;
}