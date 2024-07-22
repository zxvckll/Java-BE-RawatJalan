package com.syamsandi.java_rs_medika_utama.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment {

  @Id
  private UUID id;

  private LocalDate date;

  @Column(name = "waiting_estimation")
  private String waitingEstimation;

  @ManyToOne
  @JoinColumn(name = "doctor_profile_id")
  private DoctorProfile doctorProfile;

  @ManyToOne
  @JoinColumn(name = "patient_profile_id")
  private PatientProfile patientProfile;

  @ManyToOne
  @JoinColumn(name = "schedule_id")
  private Schedule schedule;

  @ManyToOne
  @JoinColumn(name = "clinic_id")
  private Clinic clinic;

  @ManyToOne
  @JoinColumn(name = "status_id")
  private Status status;

}
