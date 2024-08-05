package com.syamsandi.java_rs_rawat_jalan.model.schedule;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class SearchScheduleRequest {

  private UUID scheduleId;
  @JsonIgnore
  private UUID doctorProfileId;
  @JsonIgnore
  private String doctorProfileSlug;

  private LocalTime time;

  private String day;

  @NotNull
  private Integer page;

  @NotNull
  private Integer size;

}
