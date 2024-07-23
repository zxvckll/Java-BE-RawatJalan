package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.RegisterUserRequest;
import com.syamsandi.java_rs_rawat_jalan.model.UserResponse;

public interface UserService {

  void register(RegisterUserRequest request);

  UserResponse get(User user);
}
