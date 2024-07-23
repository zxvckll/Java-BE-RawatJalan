package com.syamsandi.java_rs_rawat_jalan.controller;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.CreateUserProfileRequest;
import com.syamsandi.java_rs_rawat_jalan.model.UserProfileResponse;
import com.syamsandi.java_rs_rawat_jalan.model.WebResponse;
import com.syamsandi.java_rs_rawat_jalan.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserProfileController {

  @Autowired
  private UserProfileService userProfileService;


  @PostMapping(path = "/api/users/current/profile")
  public WebResponse<String> create(@RequestBody CreateUserProfileRequest request, User user){
    userProfileService.create(request,user);
    return WebResponse.<String>builder()
        .data("OK")
        .build();
  }

}
