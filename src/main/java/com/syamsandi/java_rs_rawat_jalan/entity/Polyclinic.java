package com.syamsandi.java_rs_rawat_jalan.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "polyclinics")
public class Polyclinic {

  @Id
  private UUID id;

  private String name;

  @OneToMany(mappedBy = "polyclinic")
  private List<Clinic> clinics;
}
