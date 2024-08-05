package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.doctor_profile.*;
import com.syamsandi.java_rs_rawat_jalan.model.schedule.*;
import org.springframework.data.domain.Page;

public interface ScheduleService {
  ScheduleResponse create (CreateScheduleRequest request, User user);
  ScheduleResponse get(SchedulePath schedulePath);

  ScheduleResponse update(UpdateScheduleRequest request, User user);

  Page<ScheduleResponse> search(SearchScheduleRequest request);

  void delete(User user, SchedulePath schedulePath);
}
