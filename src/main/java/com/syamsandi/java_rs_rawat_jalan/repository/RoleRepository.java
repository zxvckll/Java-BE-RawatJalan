package com.syamsandi.java_rs_rawat_jalan.repository;

import com.syamsandi.java_rs_rawat_jalan.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

  Optional<Role> findFirstById(UUID id);

  Optional<Role> findFirstByName(String name);

  Boolean existsByName(String name);

}
