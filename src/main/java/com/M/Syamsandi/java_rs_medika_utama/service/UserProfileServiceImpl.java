package com.M.Syamsandi.java_rs_medika_utama.service;

import com.M.Syamsandi.java_rs_medika_utama.entity.User;
import com.M.Syamsandi.java_rs_medika_utama.entity.UserProfile;
import com.M.Syamsandi.java_rs_medika_utama.model.RegisterUserRequest;
import com.M.Syamsandi.java_rs_medika_utama.repository.UserProfileRepository;
import com.M.Syamsandi.java_rs_medika_utama.repository.UserRepository;
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
