package com.syamsandi.java_rs_rawat_jalan.model.user_profile;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchUserProfileRequest {


  private String name;

  private String dateOfBirth;

  private String address;

  @NotNull
  private Integer page;

  @NotNull
  private Integer size;
}
