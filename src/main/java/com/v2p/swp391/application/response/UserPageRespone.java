package com.v2p.swp391.application.response;

import com.v2p.swp391.application.model.User;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
public class UserPageRespone {
    private List<UserResponse> users;
    private int totalPages;
}
