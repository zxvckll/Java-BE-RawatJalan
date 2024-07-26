package com.syamsandi.java_rs_rawat_jalan.controller;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.RegisterUserRequest;
import com.syamsandi.java_rs_rawat_jalan.model.UpdateUserRequest;
import com.syamsandi.java_rs_rawat_jalan.model.UserResponse;
import com.syamsandi.java_rs_rawat_jalan.model.WebResponse;
import com.syamsandi.java_rs_rawat_jalan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping(path = "/api/users",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<String> register(@RequestBody RegisterUserRequest request) {
    userService.register(request);
    return WebResponse.<String>builder()
        .data("OK")
        .build();
  }

  @GetMapping(path = "/api/users/current",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<UserResponse> get(User user) {
    UserResponse userResponse = userService.get(user);
    return WebResponse.<UserResponse>builder()
        .data(userResponse)
        .build();
  }

  @PatchMapping(path = "/api/users/current",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<UserResponse> update(@RequestBody UpdateUserRequest request, User user) {
    UserResponse response = userService.update(request, user);
    return WebResponse.<UserResponse>builder()
        .data(response)
        .build();
  }


}
