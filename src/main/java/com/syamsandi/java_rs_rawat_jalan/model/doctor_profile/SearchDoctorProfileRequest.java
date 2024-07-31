package com.syamsandi.java_rs_rawat_jalan.model.doctor_profile;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchDoctorProfileRequest {

  private String education;

  private String course;

  private String experience;

  private String organization;

  @NotNull
  private Integer page;

  @NotNull
  private Integer size;
}
