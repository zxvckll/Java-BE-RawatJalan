package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.*;
import com.syamsandi.java_rs_rawat_jalan.model.appointment.*;
import com.syamsandi.java_rs_rawat_jalan.repository.*;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

public class AppointmentServiceImpl implements AppointmentService {


  @Autowired
  private ValidatorService validatorService;

  @Autowired
  private UserRoleUtils userRoleUtils;

  @Autowired
  private AppointmentRepository appointmentRepository;

  @Autowired
  private PatientProfileRepository patientProfileRepository;

  @Autowired
  private DoctorProfileRepository doctorProfileRepository;

  @Autowired
  private ClinicRepository clinicRepository;

  @Autowired
  private ScheduleRepository scheduleRepository;

  @Autowired
  private StatusRepository statusRepository;

  @Transactional
  @Override
  public AppointmentResponse create(CreateAppointmentRequest request, User user) {
    validatorService.validate(request);

    PatientProfile patientProfile = patientProfileRepository.findById(request.getPatientProfileId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Patient not found"));

    Clinic clinic = clinicRepository.findById(request.getClinicId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clinic not found"));

    DoctorProfile doctorProfile = doctorProfileRepository.findFirstByClinicAndId(clinic,request.getDoctorProfileId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Doctor not found"));

    Schedule schedule = scheduleRepository.findFirstByDoctorProfileAndId(doctorProfile,request.getScheduleId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Schedule not found"));

    Status status = statusRepository.findById(request.getStatusId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status not found"));

    Appointment appointment = new Appointment();
    appointment.setId(UUID.randomUUID());
    appointment.setPatientProfile(patientProfile);
    appointment.setDoctorProfile(doctorProfile);
    appointment.setClinic(clinic);
    appointment.setSchedule(schedule);
    appointment.setStatus(status);
    appointment.setWaitingEstimation(request.getWaiting_estimation());
    appointment.setDate(LocalDate.now());

    appointmentRepository.save(appointment);

    return toAppointmentResponse(appointment);
  }
  @Transactional(readOnly = true)
  @Override
  public AppointmentResponse get(AppointmentPath appointmentPath, User user) {


    Appointment appointment = appointmentRepository.findById(appointmentPath.getAppointmentId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointment not found"));

    if (!appointment.getPatientProfile().getUser().getId().equals(user.getId())){
      userRoleUtils.isAdminOrDoctor(user);
    }
    return toAppointmentResponse(appointment);
  }
  @Transactional
  @Override
  public AppointmentResponse update(UpdateAppointmentRequest request, User user) {
    validatorService.validate(request);

    Appointment appointment = appointmentRepository.findById(request.getAppointmentId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointment not found"));

    PatientProfile patientProfile = patientProfileRepository.findById(request.getPatientProfileId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Patient not found"));

    Clinic clinic = clinicRepository.findById(request.getClinicId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clinic not found"));

    DoctorProfile doctorProfile = doctorProfileRepository.findFirstByClinicAndId(clinic,request.getDoctorProfileId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Doctor not found"));

    Schedule schedule = scheduleRepository.findFirstByDoctorProfileAndId(doctorProfile,request.getScheduleId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Schedule not found"));

    Status status = statusRepository.findById(request.getStatusId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status not found"));

    appointment.setPatientProfile(patientProfile);
    appointment.setDoctorProfile(doctorProfile);
    appointment.setClinic(clinic);
    appointment.setSchedule(schedule);
    appointment.setStatus(status);
    appointment.setWaitingEstimation(request.getWaiting_estimation());
    appointment.setDate(LocalDate.now());

    appointmentRepository.save(appointment);

    return toAppointmentResponse(appointment);
  }
  @Transactional(readOnly = true)
  @Override
  public Page<AppointmentResponse> search(SearchAppointmentRequest request) {
    Specification<Appointment> specification = (root, query, builder) -> {
      List<Predicate> predicates = new ArrayList<>();

      Optional<PatientProfile> optionalPatientProfile = patientProfileRepository.findById(request.getPatientProfileId());
      optionalPatientProfile.ifPresent(patientProfile ->
          predicates.add(builder.equal(root.get("patientProfile"), patientProfile))
      );
      return null;
    };
    return null;
  }
  @Transactional
  @Override
  public void delete(AppointmentPath appointmentPath, User user) {
    userRoleUtils.isAdmin(user);

    Appointment appointment = appointmentRepository.findById(appointmentPath.getAppointmentId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointment not found"));

    if (!appointment.getPatientProfile().getUser().getId().equals(user.getId())){
      userRoleUtils.isAdminOrDoctor(user);
    }

    appointmentRepository.delete(appointment);
  }


  private AppointmentResponse toAppointmentResponse(Appointment appointment) {
    return AppointmentResponse.builder()
        .appointmentId(appointment.getId())
        .date(appointment.getDate())
        .clinicId(appointment.getClinic().getId())
        .clinicName(appointment.getClinic().getName())
        .scheduleTime(appointment.getSchedule().getTime().toString())
        .patientName(appointment.getPatientProfile().getUser().getUserProfiles().getName())
        .patientNoRM(appointment.getPatientProfile().getNoRM())
        .doctorName(appointment.getDoctorProfile().getUser().getUserProfiles().getName())
        .scheduleDay(appointment.getSchedule().getDay())
        .doctorProfileId(appointment.getDoctorProfile().getId())
        .patientProfileId(appointment.getPatientProfile().getId())
        .scheduleId(appointment.getSchedule().getId())
        .statusId(appointment.getStatus().getId())
        .statusName(appointment.getStatus().getName())
        .waiting_estimation(appointment.getWaitingEstimation())
        .build();
  }
}
