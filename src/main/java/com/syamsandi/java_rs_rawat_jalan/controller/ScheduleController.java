package com.syamsandi.java_rs_rawat_jalan.controller;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.PagingResponse;
import com.syamsandi.java_rs_rawat_jalan.model.WebResponse;
import com.syamsandi.java_rs_rawat_jalan.model.doctor_profile.DoctorProfilePath;
import com.syamsandi.java_rs_rawat_jalan.model.doctor_profile.DoctorProfileResponse;
import com.syamsandi.java_rs_rawat_jalan.model.doctor_profile.SearchDoctorProfileRequest;
import com.syamsandi.java_rs_rawat_jalan.model.role.CreateRoleRequest;
import com.syamsandi.java_rs_rawat_jalan.model.role.RoleResponse;
import com.syamsandi.java_rs_rawat_jalan.model.schedule.*;
import com.syamsandi.java_rs_rawat_jalan.service.RoleService;
import com.syamsandi.java_rs_rawat_jalan.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/doctors/{doctorSlug}/{doctorId}")
public class ScheduleController {

  @Autowired
  private ScheduleService scheduleService;

  @PostMapping(path = "/schedules",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<ScheduleResponse> create(User user,
                                              @RequestBody CreateScheduleRequest request,
                                              @PathVariable("doctorSlug") String doctorSlug,
                                              @PathVariable("doctorId") UUID doctorId
  ) {
    request.setDoctorProfileSlug(doctorSlug);
    request.setDoctorProfileId(doctorId);
    ScheduleResponse response = scheduleService.create(request, user);
    return WebResponse.<ScheduleResponse>builder()
        .data(response)
        .build();
  }

  @GetMapping(path = "/schedules/{scheduleId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<ScheduleResponse> get(User user, @PathVariable("scheduleId") UUID scheduleId,
                                           @PathVariable("doctorSlug") String doctorSlug,
                                           @PathVariable("doctorId") UUID doctorId) {
    SchedulePath schedulePath = new SchedulePath();
    schedulePath.setScheduleId(scheduleId);
    schedulePath.setDoctorProfileSlug(doctorSlug);
    schedulePath.setDoctorProfileId(doctorId);
    ScheduleResponse response = scheduleService.get(schedulePath);
    return WebResponse.<ScheduleResponse>builder()
        .data(response)
        .build();
  }

  @GetMapping(path = "/schedules",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<List<ScheduleResponse>> search(
                                                    @PathVariable("doctorSlug") String doctorSlug,
                                                    @PathVariable("doctorId") UUID doctorId,
                                                    @RequestParam(value = "time", required = false) LocalTime time,
                                                    @RequestParam(value = "day", required = false) String day,
                                                    @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                    @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
    SearchScheduleRequest request = new SearchScheduleRequest();
    request.setPage(page);
    request.setSize(size);
    request.setDay(day);
    request.setTime(time);
    request.setDoctorProfileId(doctorId);
    request.setDoctorProfileSlug(doctorSlug);

    Page<ScheduleResponse> responses = scheduleService.search(request);

    return WebResponse.<List<ScheduleResponse>>builder()
        .data(responses.getContent())
        .paging(PagingResponse.builder()
            .size(responses.getSize())
            .totalPage(responses.getTotalPages())
            .currentPage(responses.getNumber())
            .build())
        .build();
  }


  @PutMapping(path = "/schedules/{scheduleId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<ScheduleResponse> update(User user,
                                              @RequestBody UpdateScheduleRequest request,
                                              @PathVariable("scheduleId") UUID scheduleId,
                                              @PathVariable("doctorSlug") String doctorSlug,
                                              @PathVariable("doctorId") UUID doctorId) {
    request.setScheduleId(scheduleId);
    request.setDoctorProfileSlug(doctorSlug);
    request.setDoctorProfileId(doctorId);
    ScheduleResponse response = scheduleService.update(request, user);
    return WebResponse.<ScheduleResponse>builder()
        .data(response)
        .build();
  }

  @DeleteMapping(path = "/schedules/{scheduleId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<String> delete(User user,
                                    @PathVariable("doctorSlug") String doctorSlug,
                                    @PathVariable("doctorId") UUID doctorId,
                                    @PathVariable("scheduleId") UUID scheduleId) {
    SchedulePath schedulePath = new SchedulePath();
    schedulePath.setScheduleId(scheduleId);
    schedulePath.setDoctorProfileId(doctorId);
    schedulePath.setDoctorProfileSlug(doctorSlug);
    scheduleService.delete(user, schedulePath);
    return WebResponse.<String>builder()
        .data("OK")
        .build();
  }
}
