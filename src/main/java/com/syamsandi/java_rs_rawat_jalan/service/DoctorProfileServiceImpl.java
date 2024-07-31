package com.syamsandi.java_rs_rawat_jalan.service;


import com.syamsandi.java_rs_rawat_jalan.entity.Clinic;
import com.syamsandi.java_rs_rawat_jalan.entity.DoctorProfile;
import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.doctor_profile.*;
import com.syamsandi.java_rs_rawat_jalan.repository.ClinicRepository;
import com.syamsandi.java_rs_rawat_jalan.repository.DoctorProfileRepository;
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
public class DoctorProfileServiceImpl implements DoctorProfileService {

  @Autowired
  private ValidatorService validatorService;

  @Autowired
  private UserRoleUtils userRoleUtils;

  @Autowired
  private SlugUtils slugUtils;

  @Autowired
  private ClinicRepository clinicRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private DoctorProfileRepository doctorProfileRepository;

  @Override
  public DoctorProfileResponse create(CreateDoctorProfileRequest request, User user) {
    validatorService.validate(request);
    userRoleUtils.isAdmin(user);

    Clinic clinic = clinicRepository.findFirstBySlugAndId(request.getClinicSlug(), request.getClinicId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clinic not found"));

    User userDoctor = userRepository.findById(request.getUserId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
    userRoleUtils.isDoctor(userDoctor);

    DoctorProfile doctorProfile = new DoctorProfile();
    doctorProfile.setId(UUID.randomUUID());
    doctorProfile.setSlug(slugUtils.toSlug(user.getUserProfiles().getName()));

    doctorProfile.setClinic(clinic);
    doctorProfile.setUser(userDoctor);

    doctorProfile.setCourse(request.getCourse());
    doctorProfile.setEducation(request.getEducation());
    doctorProfile.setExperience(request.getExperience());
    doctorProfile.setOrganization(request.getOrganization());

    doctorProfileRepository.save(doctorProfile);

    return toDoctorProfileResponse(doctorProfile);
  }

  @Override
  public DoctorProfileResponse get(DoctorProfilePath doctorProfilePath) {
    Clinic clinic = clinicRepository.findFirstBySlugAndId(doctorProfilePath.getClinicSlug(), doctorProfilePath.getClinicId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clinic not found"));

    DoctorProfile doctorProfile = doctorProfileRepository.findFirstByClinicAndIdAndSlug(clinic, doctorProfilePath.getDoctorProfileId(), doctorProfilePath.getDoctorProfileSlug()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "DoctorProfile not found"));

    return toDoctorProfileResponse(doctorProfile);
  }

  @Override
  public DoctorProfileResponse update(UpdateDoctorProfileRequest request, User user) {
    validatorService.validate(request);

    userRoleUtils.isAdmin(user);

    Clinic clinic = clinicRepository.findFirstBySlugAndId(request.getClinicSlug(), request.getClinicId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clinic not found"));

    DoctorProfile doctorProfile = doctorProfileRepository.findFirstByClinicAndIdAndSlug(clinic, request.getDoctorProfileId(), request.getDoctorProfileSlug()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "DoctorProfile not found"));

    doctorProfile.setOrganization(request.getOrganization());
    doctorProfile.setExperience(request.getExperience());
    doctorProfile.setEducation(request.getEducation());
    doctorProfile.setCourse(request.getCourse());

    doctorProfileRepository.save(doctorProfile);

    return toDoctorProfileResponse(doctorProfile);
  }

  @Override
  public Page<DoctorProfileResponse> search(SearchDoctorProfileRequest request, DoctorProfilePath doctorProfilePath) {
    validatorService.validate(request);

    Clinic clinic = clinicRepository.findFirstBySlugAndId(doctorProfilePath.getClinicSlug(), doctorProfilePath.getClinicId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clinic not found"));

    Specification<DoctorProfile> specification = (root, query, builder) -> {
      List<Predicate> predicates = new ArrayList<>();
      predicates.add(builder.equal(root.get("clinic"), clinic));
      if (Objects.nonNull(request.getCourse())) {
        predicates.add(builder.like(root.get("course"), "%" + request.getCourse() + "%"));
      }
      if (Objects.nonNull(request.getEducation())) {
        predicates.add(builder.like(root.get("education"), "%" + request.getEducation() + "%"));
      }
      if (Objects.nonNull(request.getExperience())) {
        predicates.add(builder.like(root.get("experience"), "%" + request.getExperience() + "%"));
      }
      if (Objects.nonNull(request.getOrganization())) {
        predicates.add(builder.like(root.get("organization"), "%" + request.getOrganization() + "%"));
      }
      return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
    };

    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
    Page<DoctorProfile> doctorProfiles = doctorProfileRepository.findAll(specification, pageable);
    List<DoctorProfileResponse> doctorProfileResponses = doctorProfiles.getContent().stream().map(this::toDoctorProfileResponse).collect(Collectors.toList());

    return new PageImpl<>(doctorProfileResponses,pageable,doctorProfiles.getTotalElements());
  }

  @Override
  public void delete(User user, DoctorProfilePath doctorProfilePath) {
    userRoleUtils.isAdmin(user);
    Clinic clinic = clinicRepository.findFirstBySlugAndId(doctorProfilePath.getClinicSlug(), doctorProfilePath.getClinicId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clinic not found"));

    DoctorProfile doctorProfile = doctorProfileRepository.findFirstByClinicAndIdAndSlug(clinic, doctorProfilePath.getDoctorProfileId(), doctorProfilePath.getDoctorProfileSlug()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "DoctorProfile not found"));

    doctorProfileRepository.delete(doctorProfile);
  }

  private DoctorProfileResponse toDoctorProfileResponse(DoctorProfile doctorProfile) {
    return DoctorProfileResponse.builder()
        .doctorProfileId(doctorProfile.getId())
        .doctorProfileSlug(doctorProfile.getSlug())
        .clinicId(doctorProfile.getClinic().getId())
        .clinicSlug(doctorProfile.getClinic().getSlug())
        .education(doctorProfile.getEducation())
        .course(doctorProfile.getCourse())
        .experience(doctorProfile.getExperience())
        .organization(doctorProfile.getOrganization())
        .build();
  }
}
