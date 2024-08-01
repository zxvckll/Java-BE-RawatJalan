package com.syamsandi.java_rs_rawat_jalan.model.patient_profile;

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
public class UpdatePatientProfileRequest {

  @JsonIgnore
  @NotNull
  private UUID patientProfileId;

  @JsonIgnore
  @NotNull
  private String patientProfileSlug;

  private String noRM;

  private String medical_history;

  private String current_treatment;
}
