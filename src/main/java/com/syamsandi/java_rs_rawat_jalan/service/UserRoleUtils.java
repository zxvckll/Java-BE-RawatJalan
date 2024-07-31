package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.User;

public interface UserRoleUtils {

  void isAdmin(User user);
  void isDoctor(User user);
}
