package com.syamsandi.java_rs_rawat_jalan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserProfileRequest {

  @NotBlank
  @Size(max = 100)
  private String name;

  private String dateOfBirth;

  @Size(max = 100)
  private String imageUrl;

  @Size(max = 255)
  private String address;
}
