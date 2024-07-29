package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.entity.UserProfile;
import com.syamsandi.java_rs_rawat_jalan.model.user_profile.UserProfileRequest;
import com.syamsandi.java_rs_rawat_jalan.model.user_profile.UserProfileResponse;
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


  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

  @Transactional
  @Override
  public void create(UserProfileRequest request, User user) {
    validatorService.validate(request);



    userProfileRepository.findFirstByUser(user).ifPresent(profile -> {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User profile already created");
    });

    UserProfile userProfile = new UserProfile();
    userProfile.setId(UUID.randomUUID());
    userProfile.setUser(user);
    userProfile.setName(request.getName());
    userProfile.setAddress(request.getAddress());
    userProfile.setImageUrl(request.getImageUrl());
    setBirthDate(request.getDateOfBirth(), userProfile);

    userProfileRepository.save(userProfile);
  }

  @Transactional(readOnly = true)
  @Override
  public UserProfileResponse get(User user) {
    UserProfile userProfile = userProfileRepository.findFirstByUser(user)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Profile Not Found"));

    return toUserProfileResponse(userProfile);
  }

  @Transactional
  @Override
  public UserProfileResponse update(UserProfileRequest request, User user) {
    UserProfile userProfile = userProfileRepository.findFirstByUser(user)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Profile Not Found"));

    userProfile.setName(request.getName());
    userProfile.setAddress(request.getAddress());
    userProfile.setImageUrl(request.getImageUrl());
    setBirthDate(request.getDateOfBirth(), userProfile);

    userProfileRepository.save(userProfile);

    return toUserProfileResponse(userProfile);
  }

  private void setBirthDate(String dateOfBirth, UserProfile userProfile) {
    if (Objects.nonNull(dateOfBirth)) {
      LocalDate birthDate = LocalDate.parse(dateOfBirth, DATE_FORMATTER);
      userProfile.setDateOfBirth(birthDate);
    }
  }

  private UserProfileResponse toUserProfileResponse(UserProfile userProfile) {
    String dateOfBirthString = userProfile.getDateOfBirth().format(DATE_FORMATTER);

    return UserProfileResponse.builder()
        .userProfileId(userProfile.getId())
        .name(userProfile.getName())
        .address(userProfile.getAddress())
        .imageUrl(userProfile.getImageUrl())
        .dateOfBirth(dateOfBirthString)
        .build();
  }
}
