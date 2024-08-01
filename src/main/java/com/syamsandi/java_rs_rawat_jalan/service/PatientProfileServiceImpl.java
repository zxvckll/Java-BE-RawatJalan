package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.PatientProfile;
import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.patient_profile.*;
import com.syamsandi.java_rs_rawat_jalan.repository.PatientProfileRepository;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PatientProfileServiceImpl implements PatientProfileService {

  @Autowired
  private ValidatorService validatorService;

  @Autowired
  private PatientProfileRepository patientProfileRepository;

  @Autowired
  private SlugUtils slugUtils;
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserRoleUtils userRoleUtils;

  @Override
  public PatientProfileResponse create(CreatePatientProfileRequest request, User user) {
    validatorService.validate(request);
    userRoleUtils.isAdmin(user);

    User userDB = userRepository.findById(request.getUserId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found")
    );

    PatientProfile patientProfile = new PatientProfile();
    patientProfile.setId(UUID.randomUUID());
    patientProfile.setUser(userDB);
    patientProfile.setSlug(slugUtils.toSlug(userDB.getUserProfiles().getName()));
    patientProfile.setNoRM(request.getNoRM());
    patientProfile.setCurrentTreatment(request.getCurrent_treatment());
    patientProfile.setMedicalHistory(request.getMedical_history());

    patientProfileRepository.save(patientProfile);

    return toPatientProfileResponse(patientProfile);
  }

  @Override
  public PatientProfileResponse get(PatientProfilePath patientProfilePath, User user) {
    PatientProfile patientProfile = patientProfileRepository.findFirstBySlugAndId(patientProfilePath.getPatientProfileSlug(), patientProfilePath.getPatientProfileId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Patient Profile not found")
    );

    if (!patientProfile.getUser().getId().equals(user.getId())) {
      userRoleUtils.isAdminOrDoctor(user);
    }
    return toPatientProfileResponse(patientProfile);
  }

  @Override
  public PatientProfileResponse update(UpdatePatientProfileRequest request, User user) {
    validatorService.validate(request);

    PatientProfile patientProfile = patientProfileRepository.findFirstBySlugAndId(request.getPatientProfileSlug(), request.getPatientProfileId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Patient Profile not found")
    );
    if (!patientProfile.getUser().getId().equals(user.getId())) {
      userRoleUtils.isAdminOrDoctor(user);
    }

    patientProfile.setNoRM(request.getNoRM());
    patientProfile.setCurrentTreatment(request.getCurrent_treatment());
    patientProfile.setMedicalHistory(request.getMedical_history());
    patientProfileRepository.save(patientProfile);


    return toPatientProfileResponse(patientProfile);
  }

  @Override
  public Page<PatientProfileResponse> search(SearchPatientProfileRequest request) {
    validatorService.validate(request);

    Specification<PatientProfile> specification = (root, query, builder) -> {

      List<Predicate> predicates = new ArrayList<>();
      if (Objects.nonNull(request.getNoRM())){
        predicates.add(builder.like(root.get("noRM"),"%"+request.getNoRM()+"%"));
      }
      if (Objects.nonNull(request.getCurrent_treatment())){
        predicates.add(builder.like(root.get("current_treatment"),"%"+request.getCurrent_treatment()+"%"));
      }
      if (Objects.nonNull(request.getMedical_history())){
        predicates.add(builder.like(root.get("medical_history"),"%"+request.getMedical_history()+"%"));
      }
      return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
    };
    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
    Page<PatientProfile> patientProfiles = patientProfileRepository.findAll(specification, pageable);
    List<PatientProfileResponse> patientProfileResponses = patientProfiles.getContent().stream().map(this::toPatientProfileResponse).collect(Collectors.toList());

    return new PageImpl<>(patientProfileResponses,pageable,patientProfiles.getTotalElements());
  }

  @Override
  public void delete(User user, PatientProfilePath patientProfilePath) {
    userRoleUtils.isAdmin(user);

    PatientProfile patientProfile = patientProfileRepository.findFirstBySlugAndId(patientProfilePath.getPatientProfileSlug(), patientProfilePath.getPatientProfileId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Patient Profile not found")
    );

    patientProfileRepository.delete(patientProfile);
  }

  private PatientProfileResponse toPatientProfileResponse(PatientProfile patientProfile) {
    return PatientProfileResponse.builder()
        .patientProfileId(patientProfile.getId())
        .patientProfileSlug(patientProfile.getSlug())
        .noRM(patientProfile.getNoRM())
        .current_treatment(patientProfile.getCurrentTreatment())
        .medical_history(patientProfile.getMedicalHistory())
        .build();
  }
}
