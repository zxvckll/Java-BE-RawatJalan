package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.UserProfileRequest;
import com.syamsandi.java_rs_rawat_jalan.model.UserProfileResponse;

public interface UserProfileService {

  void create (UserProfileRequest request, User user);
  UserProfileResponse get(User user);

  UserProfileResponse update(UserProfileRequest request, User user);
}
