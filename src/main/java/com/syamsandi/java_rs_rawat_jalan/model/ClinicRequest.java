package com.syamsandi.java_rs_rawat_jalan.model;

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
public class ClinicRequest {

  @NotNull
  private UUID polyclinicId;

  @NotBlank
  @Size(max = 100)
  private String name;
}
