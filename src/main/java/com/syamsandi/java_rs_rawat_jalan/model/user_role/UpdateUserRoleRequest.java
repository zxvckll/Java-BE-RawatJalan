package com.syamsandi.java_rs_rawat_jalan.model.user_role;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class UpdateUserRoleRequest {

  @NotNull
  @JsonIgnore
  private UUID updateUserRoleId;

  @NotNull
  @JsonIgnore
  private UUID userId;

  @NotNull
  @JsonIgnore
  private UUID roleId;
}
