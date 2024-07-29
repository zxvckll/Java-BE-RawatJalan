package com.syamsandi.java_rs_rawat_jalan.model.polyclinic;

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
public class UpdatePolyclinicRequest {

  @NotBlank
  @Size(max = 100)
  private String name;

  @JsonIgnore
  @NotNull
  private UUID polyclinicId;

  @JsonIgnore
  @NotBlank
  private String polyclinicSlug;




}
