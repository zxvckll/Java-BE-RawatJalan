package com.syamsandi.java_rs_rawat_jalan.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileRequest {

  @NotBlank
  @Size(max = 100)
  private String name;

  private String dateOfBirth;

  @Size(max = 100)
  private String imageUrl;

  @Size(max = 255)
  private String address;
}
