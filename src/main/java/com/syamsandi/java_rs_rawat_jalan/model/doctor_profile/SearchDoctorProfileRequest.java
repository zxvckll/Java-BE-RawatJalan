package com.syamsandi.java_rs_rawat_jalan.model.doctor_profile;

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
public class SearchDoctorProfileRequest {

  @JsonIgnore
  private UUID doctorProfileId;
  @JsonIgnore
  private String doctorProfileSlug;

  private String education;

  private String course;

  private String experience;

  private String organization;

  @NotNull
  private Integer page;

  @NotNull
  private Integer size;
}
