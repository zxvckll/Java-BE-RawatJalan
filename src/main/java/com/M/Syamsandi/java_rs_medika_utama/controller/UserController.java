package com.M.Syamsandi.java_rs_medika_utama.controller;

import com.M.Syamsandi.java_rs_medika_utama.entity.UserProfile;
import com.M.Syamsandi.java_rs_medika_utama.model.RegisterUserRequest;
import com.M.Syamsandi.java_rs_medika_utama.model.WebResponse;
import com.M.Syamsandi.java_rs_medika_utama.service.UserProfileService;
import com.M.Syamsandi.java_rs_medika_utama.service.UserService;
import com.M.Syamsandi.java_rs_medika_utama.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

@RestController
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping(path = "/api/users",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<String> register(@RequestBody RegisterUserRequest request) {
    userService.register(request);
    return WebResponse.<String>builder().data("OK").build();
  }
}
