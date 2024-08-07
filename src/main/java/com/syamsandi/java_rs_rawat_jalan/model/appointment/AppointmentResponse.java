package com.syamsandi.java_rs_rawat_jalan.model.appointment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentResponse {


  private UUID appointmentId;

  private UUID doctorProfileId;

  private String doctorName;

  private UUID patientProfileId;

  private String patientName;

  private String patientNoRM;

  private UUID scheduleId;

  private String scheduleDay;

  private String scheduleTime;

  private UUID clinicId;

  private String clinicName;

  private UUID statusId;

  private String statusName;

  private LocalDate date;

  private String waiting_estimation;

}
