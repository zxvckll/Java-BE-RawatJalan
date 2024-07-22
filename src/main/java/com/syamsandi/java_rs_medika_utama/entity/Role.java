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
@Table(name = "roles")
public class Role {

  @Id
  private UUID id;

  @Column(name = "role_name")
  private String roleName;

  @ManyToMany(mappedBy = "roles")
  private List<User> users;
}
