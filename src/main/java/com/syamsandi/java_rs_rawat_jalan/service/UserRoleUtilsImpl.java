package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.entity.UserRole;
import com.syamsandi.java_rs_rawat_jalan.repository.UserRepository;
import com.syamsandi.java_rs_rawat_jalan.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserRoleUtilsImpl implements UserRoleUtils {


  @Autowired
  private UserRoleRepository userRoleRepository;

  @Override
  public void isAdmin(User user) {
    List<UserRole> userRoles = userRoleRepository.findAllByUser(user);
    Boolean isAdmin = userRoles.stream()
        .anyMatch(userRole -> "admin".equals(userRole.getRole().getName()));
    if (!isAdmin) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
    }
  }

  @Override
  public void isDoctor(User user) {
    List<UserRole> userRoles = userRoleRepository.findAllByUser(user);
    Boolean isDoctor = userRoles.stream()
        .anyMatch(userRole -> "doctor".equals(userRole.getRole().getName()));
    if (!isDoctor) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
    }
  }

  @Override
  public void isAdminOrDoctor(User user) {
    List<UserRole> userRoles = userRoleRepository.findAllByUser(user);

    Boolean isAdmin = userRoles.stream()
        .anyMatch(userRole -> "admin".equals(userRole.getRole().getName()));

    Boolean isDoctor = userRoles.stream()
        .anyMatch(userRole -> "doctor".equals(userRole.getRole().getName()));
    if (!isDoctor && !isAdmin) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
    }
  }


}
