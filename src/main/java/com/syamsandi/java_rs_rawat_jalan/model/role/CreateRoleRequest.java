package com.syamsandi.java_rs_rawat_jalan.model.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateRoleRequest {

  @NotBlank
  @Size(max = 50)
  private String name;
}
