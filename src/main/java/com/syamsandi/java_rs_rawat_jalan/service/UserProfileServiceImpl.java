package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.entity.UserProfile;
import com.syamsandi.java_rs_rawat_jalan.model.user_profile.SearchUserProfileRequest;
import com.syamsandi.java_rs_rawat_jalan.model.user_profile.UserProfileRequest;
import com.syamsandi.java_rs_rawat_jalan.model.user_profile.UserProfileResponse;
import com.syamsandi.java_rs_rawat_jalan.repository.UserProfileRepository;
import com.syamsandi.java_rs_rawat_jalan.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserProfileServiceImpl implements UserProfileService {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
  @Autowired
  private UserProfileRepository userProfileRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ValidatorService validatorService;

  @Autowired
  private UserRoleUtils userRoleUtils;

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

  @Override
  public Page<UserProfileResponse> search(User user, SearchUserProfileRequest request) {
    userRoleUtils.checkAdminRole(user);
    validatorService.validate(request);
    Specification<UserProfile> specification = ((root, query, builder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (Objects.nonNull(request.getName())) {
        predicates.add(builder.like(root.get("email"), "%" + request.getName() + "%"));
      }
      if (Objects.nonNull(request.getAddress())) {
        predicates.add(builder.like(root.get("email"), "%" + request.getAddress() + "%"));
      }
      if (Objects.nonNull(request.getDateOfBirth())) {
        predicates.add(builder.like(root.get("email"), "%" + request.getDateOfBirth() + "%"));
      }

      return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
    });

    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
    Page<UserProfile> userProfiles = userProfileRepository.findAll(specification, pageable);
    List<UserProfileResponse> userProfileResponses = userProfiles.getContent().stream()
        .map(this::toUserProfileResponse)
        .collect(Collectors.toList());


    return new PageImpl<>(userProfileResponses,pageable,userProfiles.getTotalElements());
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
