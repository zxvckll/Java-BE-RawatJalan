package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.CreateUserProfileRequest;
import com.syamsandi.java_rs_rawat_jalan.model.UserProfileResponse;

public interface UserProfileService {

  void create (CreateUserProfileRequest request, User user);

  UserProfileResponse get(User user);
}
