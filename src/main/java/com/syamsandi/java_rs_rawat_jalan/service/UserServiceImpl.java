package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.entity.UserProfile;
import com.syamsandi.java_rs_rawat_jalan.model.RegisterUserRequest;
import com.syamsandi.java_rs_rawat_jalan.model.UserResponse;
import com.syamsandi.java_rs_rawat_jalan.repository.UserProfileRepository;
import com.syamsandi.java_rs_rawat_jalan.repository.UserRepository;
import com.syamsandi.java_rs_rawat_jalan.security.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserProfileRepository userProfileRepository;

  @Autowired
  private ValidatorService validatorService;


  @Transactional
  @Override
  public void register(RegisterUserRequest request) {
    validatorService.validate(request);
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
    }

    User user = new User();
    user.setId(UUID.randomUUID());
    user.setEmail(request.getEmail());
    user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
    userRepository.save(user);

    UserProfile userProfile = new UserProfile();
    userProfile.setId(UUID.randomUUID());
    userProfile.setUser(user);
    userProfile.setName(request.getName());
    userProfileRepository.save(userProfile);
  }

  @Override
  public UserResponse get(User user) {

    UserProfile userProfile = userProfileRepository.findFirstByUser(user).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized")
    );

    return UserResponse.builder()
        .id(user.getId())
        .name(userProfile.getName())
        .email(user.getEmail())
        .password(user.getPassword())
        .build();
  }

}
