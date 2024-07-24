package com.syamsandi.java_rs_rawat_jalan.repository;

import com.syamsandi.java_rs_rawat_jalan.entity.Role;
import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.entity.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID>, JpaSpecificationExecutor<UserRole> {

  Page<UserRole> findAllByUserAndRole(User user, Role role, Pageable pageable);

  List<UserRole> findAllByUser(User user);
  Boolean existsByUserAndRole(User user, Role role);

  Page<UserRole> findAllByUser(User user,Pageable pageable);

  Page<UserRole> findAllByRole(Role role,Pageable pageable);
}
