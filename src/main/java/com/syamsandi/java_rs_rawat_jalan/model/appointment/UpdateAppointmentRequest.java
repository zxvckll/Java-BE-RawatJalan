package com.syamsandi.java_rs_rawat_jalan.model.appointment;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class UpdateAppointmentRequest {

  @NotNull
  @JsonIgnore
  private UUID appointmentId;
  @NotNull
  private UUID doctorProfileId;
  @NotNull
  private UUID patientProfileId;
  @NotNull
  private UUID scheduleId;
  @NotNull
  private UUID clinicId;
  @NotNull
  private UUID statusId;
  @JsonIgnore
  private LocalDate date;

  private String waiting_estimation;

}
