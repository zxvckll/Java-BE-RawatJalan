package com.syamsandi.java_rs_rawat_jalan.controller;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.*;
import com.syamsandi.java_rs_rawat_jalan.service.PolyclinicService;
import com.syamsandi.java_rs_rawat_jalan.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class PolyclinicController {

  @Autowired
  private PolyclinicService polyclinicService;

  @PostMapping(path = "/api/polyclinics",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<PolyclinicResponse> create(User user, @RequestBody PolyclinicRequest request) {
    PolyclinicResponse response = polyclinicService.create(user, request);
    return WebResponse.<PolyclinicResponse>builder()
        .data(response)
        .build();
  }

  @GetMapping(path = "/api/polyclinics/{polyclinicId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<PolyclinicResponse> get(User user, @PathVariable("polyclinicId") UUID polyclinicId) {
    PolyclinicResponse response = polyclinicService.get(user, polyclinicId);
    return WebResponse.<PolyclinicResponse>builder()
        .data(response)
        .build();
  }

  @GetMapping(path = "/api/polyclinics",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<List<PolyclinicResponse>> getAll(User user) {
    List<PolyclinicResponse> responses = polyclinicService.getAll(user);
    return WebResponse.<List<PolyclinicResponse>>builder()
        .data(responses)
        .build();
  }

  @PutMapping(path = "/api/polyclinics/{polyclinicId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<PolyclinicResponse> update(User user,
                                                @RequestBody PolyclinicRequest request,
                                                @PathVariable("polyclinicId") UUID polyclinicId) {
    PolyclinicResponse response = polyclinicService.update(user, request, polyclinicId);
    return WebResponse.<PolyclinicResponse>builder()
        .data(response)
        .build();
  }

  @DeleteMapping(path = "/api/polyclinics/{polyclinicId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<String> delete(User user, @PathVariable("polyclinicId") UUID polyclinicId) {
    polyclinicService.delete(user, polyclinicId);
    return WebResponse.<String>builder()
        .data("OK")
        .build();
  }
}
