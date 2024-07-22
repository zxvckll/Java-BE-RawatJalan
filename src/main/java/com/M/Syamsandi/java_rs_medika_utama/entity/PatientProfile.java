package com.M.Syamsandi.java_rs_medika_utama.entity;

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
@Table(name = "patient_profile")
public class PatientProfile {

  @Id
  private UUID id;

  private String noRM;

  @Column(name = "medical_history")
  private String medicalHistory;

  @Column(name = "current_treatment")
  private String currentTreatment;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(mappedBy = "patientProfile")
  private List<Appointment> appointments;
}
