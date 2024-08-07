package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.appointment.*;
import com.syamsandi.java_rs_rawat_jalan.model.doctor_profile.*;
import org.springframework.data.domain.Page;

public interface AppointmentService {
  AppointmentResponse create (CreateAppointmentRequest request, User user);
  AppointmentResponse get(AppointmentPath appointmentPath,User user);

  AppointmentResponse update(UpdateAppointmentRequest request, User user);

  Page<AppointmentResponse> search(SearchAppointmentRequest request);

  void delete(AppointmentPath appointmentPath,User user);
}
