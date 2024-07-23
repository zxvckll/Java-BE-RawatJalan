package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.LoginUserRequest;
import com.syamsandi.java_rs_rawat_jalan.model.TokenResponse;

public interface AuthService {


  TokenResponse login (LoginUserRequest request);

  void  logout (User user);

}
