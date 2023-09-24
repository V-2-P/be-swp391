package com.v2p.swp391.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoreResponse<T> {
    public final int code;
    public final String message;
    public final long timestamp;
    public final T data;
}
