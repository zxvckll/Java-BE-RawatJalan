package com.syamsandi.java_rs_rawat_jalan.model.patient_profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientProfileResponse {

  private UUID patientProfileId;

  private String patientProfileSlug;

  private String noRM;

  private String medical_history;

  private String current_treatment;
}
