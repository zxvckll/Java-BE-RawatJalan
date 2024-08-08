package com.syamsandi.java_rs_rawat_jalan.controller;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.WebResponse;
import com.syamsandi.java_rs_rawat_jalan.model.appointment.*;
import com.syamsandi.java_rs_rawat_jalan.model.role.CreateRoleRequest;
import com.syamsandi.java_rs_rawat_jalan.model.role.RoleResponse;
import com.syamsandi.java_rs_rawat_jalan.service.AppointmentService;
import com.syamsandi.java_rs_rawat_jalan.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class AppointmentController {

  @Autowired
  private AppointmentService appointmentService;

  @PostMapping(path = "/api/appointments",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<AppointmentResponse> create(User user, @RequestBody CreateAppointmentRequest request) {
    AppointmentResponse response = appointmentService.create(request,user);
    return WebResponse.<AppointmentResponse>builder()
        .data(response)
        .build();
  }

  @GetMapping(path = "/api/appointments/{appointmentId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<AppointmentResponse> get(User user, @PathVariable("appointmentId") UUID appointmentId) {
    AppointmentPath appointmentPath = new AppointmentPath();
    appointmentPath.setAppointmentId(appointmentId);
    AppointmentResponse response = appointmentService.get(appointmentPath,user);
    return WebResponse.<AppointmentResponse>builder()
        .data(response)
        .build();
  }

  @GetMapping(path = "/api/appointments",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<Page<AppointmentResponse>> getAll(User user, SearchAppointmentRequest searchAppointmentRequest) {
    Page<AppointmentResponse> responses = appointmentService.search(searchAppointmentRequest);
    return WebResponse.<Page<AppointmentResponse>>builder()
        .data(responses)
        .build();
  }

  @PutMapping(path = "/api/appointments/{appointmentId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<AppointmentResponse> update(User user,
                                                 @RequestBody UpdateAppointmentRequest request,
                                                 @PathVariable("appointmentId") UUID appointmentId) {
    request.setAppointmentId(appointmentId);
    AppointmentResponse response = appointmentService.update(request,user);
    return WebResponse.<AppointmentResponse>builder()
        .data(response)
        .build();
  }

  @DeleteMapping(path = "/api/appointments/{appointmentId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<String> delete(User user, @PathVariable("appointmentId") UUID appointmentId) {
    AppointmentPath appointmentPath = new AppointmentPath();
    appointmentPath.setAppointmentId(appointmentId);
    appointmentService.delete(appointmentPath,user);
    return WebResponse.<String>builder()
        .data("OK")
        .build();
  }
}
