package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.entity.UserProfile;
import com.syamsandi.java_rs_rawat_jalan.model.RegisterUserRequest;
import com.syamsandi.java_rs_rawat_jalan.repository.UserProfileRepository;
import com.syamsandi.java_rs_rawat_jalan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class UserProfileServiceImpl implements UserProfileService {

  @Autowired
  private UserProfileRepository userProfileRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ValidatorService validatorService;

  @Override
  public void Register(RegisterUserRequest request) {
    validatorService.validate(request);
    User user = userRepository.findFirstByEmail(request.getEmail()).orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND, "Address is not found"));

    UserProfile userProfile = new UserProfile();
    userProfile.setId(UUID.randomUUID());
    userProfile.setUser(user);
    userProfileRepository.save(userProfile);
  }
}
