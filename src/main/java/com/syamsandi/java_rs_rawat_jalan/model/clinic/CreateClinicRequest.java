package com.syamsandi.java_rs_rawat_jalan.model.clinic;

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
public class CreateClinicRequest {

  @NotNull
  @JsonIgnore
  private UUID polyclinicId;

  @NotBlank
  @JsonIgnore
  private String polyclinicSlug;

  @NotBlank
  @Size(max = 100)
  private String name;

}
