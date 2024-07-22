package com.syamsandi.java_rs_medika_utama.controller;

import com.syamsandi.java_rs_medika_utama.model.LoginUserRequest;
import com.syamsandi.java_rs_medika_utama.model.TokenResponse;
import com.syamsandi.java_rs_medika_utama.model.WebResponse;
import com.syamsandi.java_rs_medika_utama.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping(path = "/api/auth/login",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<TokenResponse> login(@RequestBody LoginUserRequest request) {
    TokenResponse tokenResponse = authService.login(request);

    return WebResponse.<TokenResponse>builder()
        .data(tokenResponse)
        .build();
  }
}
