package com.syamsandi.java_rs_medika_utama.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "schedules")
public class Schedule {

  @Id
  private UUID id;

  private String day;

  private LocalTime time;

  @ManyToOne
  @JoinColumn(name = "doctor_profile_id")
  private DoctorProfile doctorProfile;

  @OneToMany(mappedBy = "schedule")
  private List<Appointment> appointments;
}
