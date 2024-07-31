package com.syamsandi.java_rs_rawat_jalan.model.doctor_profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorProfilePath {

  private String clinicSlug;

  private String doctorProfileSlug;

  private UUID doctorProfileId;

  private UUID clinicId;
  
}
