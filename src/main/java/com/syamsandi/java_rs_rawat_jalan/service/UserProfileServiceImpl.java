package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.entity.UserProfile;
import com.syamsandi.java_rs_rawat_jalan.model.CreateUserProfileRequest;
import com.syamsandi.java_rs_rawat_jalan.model.RegisterUserRequest;
import com.syamsandi.java_rs_rawat_jalan.repository.UserProfileRepository;
import com.syamsandi.java_rs_rawat_jalan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserProfileServiceImpl implements UserProfileService {

  @Autowired
  private UserProfileRepository userProfileRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ValidatorService validatorService;


  @Transactional
  @Override
  public void create(CreateUserProfileRequest request, User user) {
    validatorService.validate(request);
    UserProfile userProfileDB = userProfileRepository.findFirstByUser(user).orElse(null);
    if(Objects.nonNull(userProfileDB)){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"user profile already created");
    }

    UserProfile userProfile = new UserProfile();
    userProfile.setId(UUID.randomUUID());
    userProfile.setUser(user);
    userProfile.setName(request.getName());
    userProfile.setAddress(request.getAddress());
    userProfile.setImageUrl(request.getImageUrl());

    if(Objects.nonNull(request.getDateOfBirth())){
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
      LocalDate dateOfBirth = LocalDate.parse(request.getDateOfBirth(), formatter);
      userProfile.setDateOfBirth(dateOfBirth);
    }

    userProfileRepository.save(userProfile);
  }

}
