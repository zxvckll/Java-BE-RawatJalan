package com.syamsandi.java_rs_rawat_jalan.model.clinic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClinicResponse {

  private String name;

  private UUID polyclinicId;

  private String polyclinicSlug;

  private UUID clinicId;

  private String clinicSlug;


}
