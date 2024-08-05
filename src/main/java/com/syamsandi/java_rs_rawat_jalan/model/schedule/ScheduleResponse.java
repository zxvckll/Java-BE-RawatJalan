package com.syamsandi.java_rs_rawat_jalan.model.schedule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ScheduleResponse {

  private UUID scheduleId;

  private UUID doctorProfileId;

  private String doctorProfileSlug;

  private LocalTime time;

  private String day;
}
