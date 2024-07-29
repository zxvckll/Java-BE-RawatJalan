package com.syamsandi.java_rs_rawat_jalan.model.user_role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRoleResponse {

  private UUID userRoleId;

  private UUID userId;

  private UUID roleId;

}
