package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.PatientProfile;
import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.entity.UserProfile;
import com.syamsandi.java_rs_rawat_jalan.model.user.RegisterUserRequest;
import com.syamsandi.java_rs_rawat_jalan.model.user.UpdateUserRequest;
import com.syamsandi.java_rs_rawat_jalan.model.user.UserResponse;
import com.syamsandi.java_rs_rawat_jalan.repository.UserProfileRepository;
import com.syamsandi.java_rs_rawat_jalan.repository.UserRepository;
import com.syamsandi.java_rs_rawat_jalan.security.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserProfileRepository userProfileRepository;

  @Autowired
  private ValidatorService validatorService;

  @Autowired
  private SlugUtils slugUtils;


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

    PatientProfile patientProfile = new PatientProfile();
    patientProfile.setId(UUID.randomUUID());
    patientProfile.setUser(user);
    patientProfile.setSlug(slugUtils.toSlug(userProfile.getName()));
  }

  @Transactional(readOnly = true)
  @Override
  public UserResponse get(User user) {

    UserProfile userProfile = userProfileRepository.findFirstByUser(user).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized")
    );

    return toUserResponse(user,userProfile);
  }

  @Transactional
  @Override
  public UserResponse update(UpdateUserRequest request, User user) {
    validatorService.validate(request);

    if(Objects.nonNull(request.getEmail())){
      user.setEmail(request.getEmail());
    }
    if(Objects.nonNull(request.getPassword())){
      user.setPassword(BCrypt.hashpw(request.getPassword(),BCrypt.gensalt()));
    }
    userRepository.save(user);

    UserProfile userProfile = userProfileRepository.findFirstByUser(user).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized")
    );


    return toUserResponse(user,userProfile);

  }

  private UserResponse toUserResponse (User user, UserProfile userProfile){
    return UserResponse.builder()
        .name(userProfile.getName())
        .email(user.getEmail())
        .id(user.getId())
        .build();
  }


}
