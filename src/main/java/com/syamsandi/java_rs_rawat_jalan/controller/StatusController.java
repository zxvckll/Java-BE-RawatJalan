package com.syamsandi.java_rs_rawat_jalan.controller;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.WebResponse;
import com.syamsandi.java_rs_rawat_jalan.model.role.CreateRoleRequest;
import com.syamsandi.java_rs_rawat_jalan.model.role.RoleResponse;
import com.syamsandi.java_rs_rawat_jalan.model.status.CreateStatusRequest;
import com.syamsandi.java_rs_rawat_jalan.model.status.StatusPath;
import com.syamsandi.java_rs_rawat_jalan.model.status.StatusResponse;
import com.syamsandi.java_rs_rawat_jalan.model.status.UpdateStatusRequest;
import com.syamsandi.java_rs_rawat_jalan.service.RoleService;
import com.syamsandi.java_rs_rawat_jalan.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class StatusController {

  @Autowired
  private StatusService statusService;

  @PostMapping(path = "/api/status",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<StatusResponse> create(User user, @RequestBody CreateStatusRequest request) {
    StatusResponse response = statusService.create(user, request);
    return WebResponse.<StatusResponse>builder()
        .data(response)
        .build();
  }

  @GetMapping(path = "/api/status/{statusId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<StatusResponse> get(User user, @PathVariable("statusId") UUID statusId) {
    StatusPath statusPath = new StatusPath();
    statusPath.setStatusId(statusId);
    StatusResponse response = statusService.get(user, statusPath);
    return WebResponse.<StatusResponse>builder()
        .data(response)
        .build();
  }

  @GetMapping(path = "/api/statuses",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<List<StatusResponse>> getAll(User user) {
    List<StatusResponse> responses = statusService.getAll(user);
    return WebResponse.<List<StatusResponse>>builder()
        .data(responses)
        .build();
  }

  @PutMapping(path = "/api/status/{statusId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<StatusResponse> update(User user, @RequestBody UpdateStatusRequest request, @PathVariable("statusId") UUID statusId) {
    request.setStatusId(statusId);
    StatusResponse response = statusService.update(user, request);
    return WebResponse.<StatusResponse>builder()
        .data(response)
        .build();
  }

  @DeleteMapping(path = "/api/status/{statusId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<String> delete(User user, @PathVariable("statusId") UUID statusId) {
    StatusPath statusPath = new StatusPath();
    statusPath.setStatusId(statusId);
    statusService.delete(user, statusPath);
    return WebResponse.<String>builder()
        .data("OK")
        .build();
  }
}
