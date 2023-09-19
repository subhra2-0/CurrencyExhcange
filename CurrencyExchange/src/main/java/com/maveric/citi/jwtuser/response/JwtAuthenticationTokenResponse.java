package com.maveric.citi.jwtuser.response;

import com.maveric.citi.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthenticationTokenResponse {
    String token;
    RoleEnum role;
    Long id;
//    String email;
//    String password;
}
