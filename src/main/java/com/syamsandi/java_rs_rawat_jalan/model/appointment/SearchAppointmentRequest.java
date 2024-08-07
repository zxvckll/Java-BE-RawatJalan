package com.syamsandi.java_rs_rawat_jalan.model.appointment;

import jakarta.validation.constraints.NotNull;
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
public class SearchAppointmentRequest {


  private UUID appointmentId;

  private UUID doctorProfileId;

  private String doctorName;

  private UUID patientProfileId;

  private String patientName;

  private String patientNoRM;

  private UUID scheduleId;

  private UUID scheduleDay;

  private UUID scheduleTime;

  private UUID clinicId;

  private UUID clinicName;

  private UUID statusId;

  private UUID statusName;

  private LocalDate date;

  private String waiting_estimation;

  @NotNull
  private Integer page;

  @NotNull
  private Integer size;

}
