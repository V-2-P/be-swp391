package com.v2p.swp391.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.v2p.swp391.common.model.Location;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Shop {
    @JsonProperty("_id")
    private int id;

    private String name;
    private String phone;
    private String address;

    @JsonProperty("ward_code")
    private String wardCode;

    @JsonProperty("district_id")
    private int districtId;

    @JsonProperty("client_id")
    private int clientId;

    @JsonProperty("bank_account_id")
    private int bankAccountId;

    private int status;

    private Location location;

    @JsonProperty("version_no")
    private String versionNo;

    @JsonProperty("is_created_chat_channel")
    private boolean isCreatedChatChannel;

    @JsonProperty("updated_ip")
    private String updatedIp;

    @JsonProperty("updated_employee")
    private int updatedEmployee;

    @JsonProperty("updated_client")
    private int updatedClient;

    @JsonProperty("updated_source")
    private String updatedSource;

    @JsonProperty("updated_date")
    private String updatedDate;

    @JsonProperty("created_ip")
    private String createdIp;

    @JsonProperty("created_employee")
    private int createdEmployee;

    @JsonProperty("created_client")
    private int createdClient;

    @JsonProperty("created_source")
    private String createdSource;

    @JsonProperty("created_date")
    private String createdDate;
}
