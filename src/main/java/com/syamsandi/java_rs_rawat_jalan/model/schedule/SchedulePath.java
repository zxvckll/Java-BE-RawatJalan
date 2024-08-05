package com.syamsandi.java_rs_rawat_jalan.model.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SchedulePath {

  private UUID scheduleId;

  private UUID doctorProfileId;

  private String doctorProfileSlug;

}
