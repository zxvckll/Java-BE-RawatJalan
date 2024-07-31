package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.Polyclinic;
import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.polyclinic.CreatePolyclinicRequest;
import com.syamsandi.java_rs_rawat_jalan.model.polyclinic.PolyclinicPath;
import com.syamsandi.java_rs_rawat_jalan.model.polyclinic.PolyclinicResponse;
import com.syamsandi.java_rs_rawat_jalan.model.polyclinic.UpdatePolyclinicRequest;
import com.syamsandi.java_rs_rawat_jalan.repository.PolyclinicRepository;
import com.syamsandi.java_rs_rawat_jalan.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class PolyclinicServiceImpl implements PolyclinicService {

  @Autowired
  private ValidatorService validatorService;

  @Autowired
  private PolyclinicRepository polyclinicRepository;

  @Autowired
  private UserRoleRepository userRoleRepository;

  @Autowired
  private UserRoleUtils userRoleUtils;

  @Autowired
  private SlugUtils slugUtils;

  @Transactional
  @Override
  public PolyclinicResponse create(User user, CreatePolyclinicRequest request) {
    validatorService.validate(request);
    userRoleUtils.isAdmin(user);

    String slug = slugUtils.toSlug(request.getName());
    Polyclinic polyclinic = new Polyclinic();
    polyclinic.setId(UUID.randomUUID());
    polyclinic.setName(request.getName());
    polyclinic.setSlug(slug);
    polyclinicRepository.save(polyclinic);

    return toPolyclinicResponse(polyclinic);
  }

  @Transactional(readOnly = true)
  @Override
  public PolyclinicResponse get(User user, PolyclinicPath polyclinicPath) {
    userRoleUtils.isAdmin(user);

    Polyclinic polyclinic = polyclinicRepository.findFirstBySlugAndId(polyclinicPath.getPolyclinicSlug(), polyclinicPath.getPolyclinicId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Polyclinic not found"));

    return toPolyclinicResponse(polyclinic);
  }

  @Transactional(readOnly = true)
  @Override
  public List<PolyclinicResponse> getAll(User user) {
    userRoleUtils.isAdmin(user);

    List<Polyclinic> polyclinics = polyclinicRepository.findAll();
    return polyclinics.stream().map(this::toPolyclinicResponse).toList();
  }

  @Transactional
  @Override
  public PolyclinicResponse update(User user, UpdatePolyclinicRequest request) {
    validatorService.validate(request);
    userRoleUtils.isAdmin(user);

    Polyclinic polyclinic = polyclinicRepository.findFirstBySlugAndId(request.getPolyclinicSlug(), request.getPolyclinicId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Polyclinic not found"));

    polyclinic.setName(request.getName());
    polyclinicRepository.save(polyclinic);

    return toPolyclinicResponse(polyclinic);
  }

  @Transactional
  @Override
  public void delete(User user, PolyclinicPath polyclinicPath) {
    userRoleUtils.isAdmin(user);

    Polyclinic polyclinic = polyclinicRepository.findFirstBySlugAndId(polyclinicPath.getPolyclinicSlug(), polyclinicPath.getPolyclinicId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Polyclinic not found"));

    polyclinicRepository.delete(polyclinic);
  }

  private PolyclinicResponse toPolyclinicResponse(Polyclinic polyclinic) {
    return PolyclinicResponse.builder()
        .polyclinicId(polyclinic.getId())
        .name(polyclinic.getName())
        .polyclinicSlug(polyclinic.getSlug())
        .build();
  }
}
