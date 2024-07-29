package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.user_profile.UserProfileRequest;
import com.syamsandi.java_rs_rawat_jalan.model.user_profile.UserProfileResponse;

public interface UserProfileService {

  void create (UserProfileRequest request, User user);
  UserProfileResponse get(User user);

  UserProfileResponse update(UserProfileRequest request, User user);
}
