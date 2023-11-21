package com.v2p.swp391.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoreResponse<T> {
    public int code;
    public String message;
    public long timestamp;
    public T data;
}
