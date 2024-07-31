package com.syamsandi.java_rs_rawat_jalan.entity;

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

  private String slug;

  @ManyToOne
  @JoinColumn(name = "polyclinic_id")
  private Polyclinic polyclinic;

  @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL)
  private List<DoctorProfile> doctorProfiles;

  @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL)
  private List<Appointment> appointments;
}
