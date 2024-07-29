package com.syamsandi.java_rs_rawat_jalan.controller;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.user_profile.UserProfileRequest;
import com.syamsandi.java_rs_rawat_jalan.model.user_profile.UserProfileResponse;
import com.syamsandi.java_rs_rawat_jalan.model.WebResponse;
import com.syamsandi.java_rs_rawat_jalan.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/current")
public class UserProfileController {

  @Autowired
  private UserProfileService userProfileService;


  @PostMapping(path = "/profile")
  public WebResponse<String> create(@RequestBody UserProfileRequest request, User user) {
    userProfileService.create(request, user);
    return WebResponse.<String>builder()
        .data("OK")
        .build();
  }

  @GetMapping(path = "/profile")
  public WebResponse<UserProfileResponse> get(User user) {
    UserProfileResponse response = userProfileService.get(user);
    return WebResponse.<UserProfileResponse>builder()
        .data(response)
        .build();
  }

  @PutMapping(path = "/profile",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<UserProfileResponse> update(@RequestBody UserProfileRequest request, User user) {
    UserProfileResponse response = userProfileService.update(request, user);

    return WebResponse.<UserProfileResponse>builder()
        .data(response)
        .build();
  }

}
