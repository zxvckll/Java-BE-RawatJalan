package com.syamsandi.java_rs_rawat_jalan.entity;

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
@Table(name = "user_profile")
public class UserProfile {

  @Id
  private UUID id;

  private String name;

  @Column(name = "image_url")
  private String imageUrl;

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  private String address;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;
}
