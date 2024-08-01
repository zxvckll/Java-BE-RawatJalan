package com.syamsandi.java_rs_rawat_jalan.model.patient_profile;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchPatientProfileRequest {

  private String noRM;

  private String medical_history;

  private String current_treatment;

  @NotNull
  private Integer page;

  @NotNull
  private Integer size;
}
