package com.syamsandi.java_rs_rawat_jalan.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRoleRequest {

  @NotNull
  private UUID user_id;

  @NotNull
  private UUID role_id;
}
