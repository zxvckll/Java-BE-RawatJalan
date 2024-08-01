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
public class PatientProfilePath {

  private String patientProfileSlug;

  private UUID patientProfileId;

  
}
