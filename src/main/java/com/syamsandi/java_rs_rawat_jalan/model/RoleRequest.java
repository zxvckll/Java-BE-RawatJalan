package com.syamsandi.java_rs_rawat_jalan.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleRequest {

  @NotBlank
  @Size(max = 50)
  private String name;
}
