package com.syamsandi.java_rs_medika_utama.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "doctor_profile")
public class DoctorProfile {

  @Id
  private UUID id;

  private String education;

  private String course;

  private String experience;

  private String organization;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "clinic_id")
  private Clinic clinic;

  @OneToMany(mappedBy = "doctorProfile")
  private List<Schedule> schedules;

  @OneToMany(mappedBy = "doctorProfile")
  private List<Appointment> appointments;
}
