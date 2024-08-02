package com.syamsandi.java_rs_rawat_jalan.model.status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusResponse {

  private UUID statusId;

  private String name;
}
