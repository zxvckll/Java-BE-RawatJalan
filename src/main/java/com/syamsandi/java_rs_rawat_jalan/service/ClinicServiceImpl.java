package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.Clinic;
import com.syamsandi.java_rs_rawat_jalan.entity.Polyclinic;
import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.clinic.ClinicPath;
import com.syamsandi.java_rs_rawat_jalan.model.clinic.CreateClinicRequest;
import com.syamsandi.java_rs_rawat_jalan.model.clinic.ClinicResponse;
import com.syamsandi.java_rs_rawat_jalan.model.clinic.UpdateClinicRequest;
import com.syamsandi.java_rs_rawat_jalan.repository.ClinicRepository;
import com.syamsandi.java_rs_rawat_jalan.repository.PolyclinicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClinicServiceImpl implements ClinicService {

  @Autowired
  private ValidatorService validatorService;

  @Autowired
  private ClinicRepository clinicRepository;

  @Autowired
  private PolyclinicRepository polyclinicRepository;

  @Autowired
  private UserRoleUtils userRoleUtils;

  @Autowired
  private SlugUtils slugUtils;

  @Transactional
  @Override
  public ClinicResponse create(User user, CreateClinicRequest request) {
    validatorService.validate(request);
    userRoleUtils.checkAdminRole(user);

    Polyclinic polyclinic = polyclinicRepository.findFirstBySlugAndId(request.getPolyclinicSlug(),request.getPolyclinicId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Polyclinic not found"));

    String slug = slugUtils.toSlug(request.getName());

    Clinic clinic = new Clinic();
    clinic.setId(UUID.randomUUID());
    clinic.setPolyclinic(polyclinic);
    clinic.setName(request.getName());
    clinic.setSlug(slug);
    clinicRepository.save(clinic);
    return toClinicResponse(clinic);
  }

  @Transactional(readOnly = true)
  @Override
  public ClinicResponse get(User user, ClinicPath clinicPath) {
    userRoleUtils.checkAdminRole(user);

    Clinic clinic = clinicRepository.findFirstBySlugAndId(clinicPath.getClinicSlug(),clinicPath.getClinicId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clinic not found"));

    if (!clinic.getPolyclinic().getSlug().equals(clinicPath.getPolyclinicSlug())) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clinic is not related to the specified polyclinic");
    }

    return toClinicResponse(clinic);
  }

  @Transactional(readOnly = true)
  @Override
  public List<ClinicResponse> getAll(User user, ClinicPath clinicPath) {
    userRoleUtils.checkAdminRole(user);

    List<Clinic> clinics;
    if (clinicPath.getPolyclinicSlug() != null) {
      Polyclinic polyclinic = polyclinicRepository.findFirstBySlugAndId(clinicPath.getPolyclinicSlug(),clinicPath.getPolyclinicId())
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Polyclinic not found"));
      clinics = clinicRepository.findAllByPolyclinic(polyclinic);
    } else {
      clinics = clinicRepository.findAll();
    }

    return clinics.stream().map(this::toClinicResponse).collect(Collectors.toList());
  }

  @Transactional
  @Override
  public ClinicResponse update(User user, UpdateClinicRequest request) {
    validatorService.validate(request);
    userRoleUtils.checkAdminRole(user);

    Polyclinic polyclinic = polyclinicRepository.findFirstBySlugAndId(request.getPolyclinicSlug(),request.getPolyclinicId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Polyclinic not found"));

    Clinic clinic = clinicRepository.findFirstBySlugAndId(request.getClinicSlug(),request.getClinicId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clinic not found"));

    clinic.setName(request.getName());
    clinic.setPolyclinic(polyclinic);

    clinicRepository.save(clinic);

    return toClinicResponse(clinic);
  }

  @Transactional
  @Override
  public void delete(User user, ClinicPath clinicPath) {
    userRoleUtils.checkAdminRole(user);

    Clinic clinic = clinicRepository.findFirstBySlugAndId(clinicPath.getClinicSlug(),clinicPath.getClinicId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clinic not found"));

    if (!clinic.getPolyclinic().getId().equals(clinicPath.getPolyclinicId())) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clinic is not related to the specified polyclinic");
    }

    clinicRepository.delete(clinic);
  }

  private ClinicResponse toClinicResponse(Clinic clinic) {
    return ClinicResponse.builder()
        .clinicId(clinic.getId())
        .polyclinicId(clinic.getPolyclinic().getId())
        .polyclinicSlug(clinic.getPolyclinic().getSlug())
        .clinicSlug(clinic.getSlug())
        .name(clinic.getName())
        .build();
  }
}
