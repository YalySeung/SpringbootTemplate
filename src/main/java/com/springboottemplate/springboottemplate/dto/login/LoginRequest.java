package com.springboottemplate.springboottemplate.dto.login;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    @NotBlank(message = "username은 필수입니다.")
    String username;
    @NotBlank(message = "password는 필수입니다.")
    String password;
}
