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
@Table(name = "users")
public class User {

  @Id
  private UUID id;

  private String email;

  private String password;

  private String token;

  @Column(name = "token_expired_at")
  private Long tokenExpiredAt;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private UserProfile userProfiles;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private DoctorProfile doctorProfile;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private PatientProfile patientProfile;

  @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
  private List<UserRole> userRoles;


}
