package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.DoctorProfile;
import com.syamsandi.java_rs_rawat_jalan.entity.Schedule;
import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.doctor_profile.DoctorProfilePath;
import com.syamsandi.java_rs_rawat_jalan.model.doctor_profile.DoctorProfileResponse;
import com.syamsandi.java_rs_rawat_jalan.model.schedule.*;
import com.syamsandi.java_rs_rawat_jalan.repository.DoctorProfileRepository;
import com.syamsandi.java_rs_rawat_jalan.repository.ScheduleRepository;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {

  @Autowired
  private ValidatorService validatorService;

  @Autowired
  private UserRoleUtils userRoleUtils;
  @Autowired
  private DoctorProfileRepository doctorProfileRepository;
  @Autowired
  private ScheduleRepository scheduleRepository;

  @Override
  public ScheduleResponse create(CreateScheduleRequest request, User user) {
    validatorService.validate(request);
    userRoleUtils.isAdmin(user);

    DoctorProfile doctorProfile = doctorProfileRepository.findFirstByIdAndSlug(request.getDoctorProfileId(), request.getDoctorProfileSlug()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Doctor Not Found")
    );

    Schedule schedule = new Schedule();
    schedule.setId(UUID.randomUUID());
    schedule.setDay(request.getDay());
    schedule.setTime(request.getTime());
    schedule.setDoctorProfile(doctorProfile);

    scheduleRepository.save(schedule);
    return toScheduleResponse(schedule);
  }

  @Override
  public ScheduleResponse get(SchedulePath schedulePath) {

    DoctorProfile doctorProfile = doctorProfileRepository.findFirstByIdAndSlug(schedulePath.getDoctorProfileId(), schedulePath.getDoctorProfileSlug()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Doctor Not Found")
    );

    Schedule schedule = scheduleRepository.findFirstByDoctorProfileAndId(doctorProfile, schedulePath.getScheduleId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Schedule Not Found")
    );

    return toScheduleResponse(schedule);
  }

  @Override
  public ScheduleResponse update(UpdateScheduleRequest request, User user) {
    validatorService.validate(request);
    userRoleUtils.isAdmin(user);

    DoctorProfile doctorProfile = doctorProfileRepository.findFirstByIdAndSlug(request.getDoctorProfileId(), request.getDoctorProfileSlug()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Doctor Not Found")
    );

    Schedule schedule = scheduleRepository.findFirstByDoctorProfileAndId(doctorProfile, request.getScheduleId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Schedule Not Found")
    );

    schedule.setTime(request.getTime());
    schedule.setDay(request.getDay());
    scheduleRepository.save(schedule);

    return toScheduleResponse(schedule);
  }

  @Override
  public Page<ScheduleResponse> search(SearchScheduleRequest request) {

    Optional<DoctorProfile> doctorProfile = doctorProfileRepository.findFirstByIdAndSlug(request.getScheduleId(), request.getDoctorProfileSlug());

    Specification<Schedule> specification = (root, query, builder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (doctorProfile.isPresent()) {
        predicates.add(builder.equal(root.get("doctorProfile"), doctorProfile));
      }
      if (Objects.nonNull(request.getDoctorProfileId())) {
        predicates.add(builder.equal(root.get("doctorProfile.id"),  request.getDoctorProfileId() ));
      }
      if (Objects.nonNull(request.getDoctorProfileSlug())) {
        predicates.add(builder.like(root.get("doctorProfile.slug"), "%" + request.getDoctorProfileSlug() + "%"));
      }
      if (Objects.nonNull(request.getScheduleId())) {
        predicates.add(builder.equal(root.get("id"),  request.getScheduleId() ));
      }
      if (Objects.nonNull(request.getTime())) {
        predicates.add(builder.like(root.get("time"), "%" + request.getTime() + "%"));
      }

      return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
    };
    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
    Page<Schedule> schedules = scheduleRepository.findAll(specification, pageable);
    List<ScheduleResponse> scheduleResponses = schedules.getContent().stream().map(this::toScheduleResponse).collect(Collectors.toList());

    return new PageImpl<>(scheduleResponses,pageable,schedules.getTotalElements());
  }

  @Override
  public void delete(User user, SchedulePath schedulePath) {
    userRoleUtils.isAdmin(user);

    DoctorProfile doctorProfile = doctorProfileRepository.findFirstByIdAndSlug(schedulePath.getDoctorProfileId(), schedulePath.getDoctorProfileSlug()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Doctor Not Found")
    );

    Schedule schedule = scheduleRepository.findFirstByDoctorProfileAndId(doctorProfile, schedulePath.getScheduleId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Schedule Not Found")
    );
    scheduleRepository.delete(schedule);
  }

  private ScheduleResponse toScheduleResponse(Schedule schedule) {
    return ScheduleResponse.builder()
        .scheduleId(schedule.getId())
        .doctorProfileId(schedule.getDoctorProfile().getId())
        .doctorProfileSlug(schedule.getDoctorProfile().getSlug())
        .day(schedule.getDay())
        .time(schedule.getTime())
        .build();
  }
}
