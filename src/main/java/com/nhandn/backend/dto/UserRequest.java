package com.nhandn.backend.dto;

import com.nhandn.backend.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

  private Integer id;

  @NotBlank(message = "username is mandatory")
  private String username;
  @NotBlank(message = "email is mandatory")
  @Email
  private String email;

  @NotBlank(message = "password is mandatory")
  private String password;
  private Role role;
}
