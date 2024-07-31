package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.entity.UserProfile;
import com.syamsandi.java_rs_rawat_jalan.model.user_profile.SearchUserProfileRequest;
import com.syamsandi.java_rs_rawat_jalan.model.user_profile.UserProfileRequest;
import com.syamsandi.java_rs_rawat_jalan.model.user_profile.UserProfileResponse;
import org.springframework.data.domain.Page;

public interface UserProfileService {

  void create (UserProfileRequest request, User user);
  UserProfileResponse get(User user);

  UserProfileResponse update(UserProfileRequest request, User user);

  Page<UserProfileResponse> search(User user, SearchUserProfileRequest request);
}
