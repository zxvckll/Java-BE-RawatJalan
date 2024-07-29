package com.syamsandi.java_rs_rawat_jalan.model.polyclinic;

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
public class CreatePolyclinicRequest {

  @NotBlank
  @Size(max = 100)
  private String name;

}
