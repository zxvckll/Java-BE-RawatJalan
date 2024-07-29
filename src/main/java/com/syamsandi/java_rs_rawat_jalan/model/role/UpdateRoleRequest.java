package com.syamsandi.java_rs_rawat_jalan.model.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateRoleRequest {

  @NotBlank
  @Size(max = 50)
  private String name;

  @NotNull
  @JsonIgnore
  private UUID updateRoleId;
}
