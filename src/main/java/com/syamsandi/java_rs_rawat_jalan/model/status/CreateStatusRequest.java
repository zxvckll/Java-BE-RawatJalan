package com.syamsandi.java_rs_rawat_jalan.model.status;

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
public class CreateStatusRequest {

  @NotBlank
  @Size(max = 50)
  private String name;
}
