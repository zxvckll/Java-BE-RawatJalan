package com.syamsandi.java_rs_medika_utama.service;

import com.syamsandi.java_rs_medika_utama.entity.User;
import com.syamsandi.java_rs_medika_utama.model.LoginUserRequest;
import com.syamsandi.java_rs_medika_utama.model.TokenResponse;

public interface AuthService {


  TokenResponse login (LoginUserRequest request);

  void  logout (User user);
}
