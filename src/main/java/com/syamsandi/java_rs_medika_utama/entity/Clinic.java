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
@Table(name = "clinics")
public class Clinic {

  @Id
  private UUID id;

  private String name;

  @ManyToOne
  @JoinColumn(name = "polyclinic_id")
  private Polyclinic polyclinic;

  @OneToMany(mappedBy = "clinic")
  private List<DoctorProfile> doctorProfiles;

  @OneToMany(mappedBy = "clinic")
  private List<Appointment> appointments;
}
