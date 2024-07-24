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
  public Boolean isAdmin(User user) {
    List<UserRole> userRoles = userRoleRepository.findAllByUser(user);
    return userRoles.stream()
        .anyMatch(userRole -> "admin".equals(userRole.getRole().getName()));
  }

  @Override
  public void checkAdminRole(User user) {
    if (!isAdmin(user)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
    }
  }
}
