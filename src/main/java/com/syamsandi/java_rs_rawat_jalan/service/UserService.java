package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.user.RegisterUserRequest;
import com.syamsandi.java_rs_rawat_jalan.model.user.UpdateUserRequest;
import com.syamsandi.java_rs_rawat_jalan.model.user.UserResponse;

public interface UserService {

  void register(RegisterUserRequest request);

  UserResponse get(User user);

  UserResponse update(UpdateUserRequest request,User user);


}
