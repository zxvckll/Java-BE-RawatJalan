package com.syamsandi.java_rs_rawat_jalan.controller;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.*;
import com.syamsandi.java_rs_rawat_jalan.model.polyclinic.CreatePolyclinicRequest;
import com.syamsandi.java_rs_rawat_jalan.model.polyclinic.PolyclinicPath;
import com.syamsandi.java_rs_rawat_jalan.model.polyclinic.PolyclinicResponse;
import com.syamsandi.java_rs_rawat_jalan.model.polyclinic.UpdatePolyclinicRequest;
import com.syamsandi.java_rs_rawat_jalan.service.PolyclinicService;
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
  public WebResponse<PolyclinicResponse> create(User user, @RequestBody CreatePolyclinicRequest request) {
    PolyclinicResponse response = polyclinicService.create(user, request);
    return WebResponse.<PolyclinicResponse>builder()
        .data(response)
        .build();
  }

  @GetMapping(path = "/api/polyclinics/{polyclinicSlug}/{polyclinicId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<PolyclinicResponse> get(User user,
                                             @PathVariable("polyclinicSlug") String polyclinicSlug,
                                             @PathVariable("polyclinicId") UUID polyclinicId) {
    System.out.println(polyclinicId);
    PolyclinicPath polyclinicPath = new PolyclinicPath();
    polyclinicPath.setPolyclinicSlug(polyclinicSlug);
    polyclinicPath.setPolyclinicId(polyclinicId);
    PolyclinicResponse response = polyclinicService.get(user, polyclinicPath);
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

  @PutMapping(path = "/api/polyclinics/{polyclinicSlug}/{polyclinicId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<PolyclinicResponse> update(User user,
                                                @RequestBody UpdatePolyclinicRequest request,
                                                @PathVariable("polyclinicSlug") String polyclinicSlug,
                                                @PathVariable("polyclinicId") UUID polyclinicId) {
    request.setPolyclinicId(polyclinicId);
    request.setPolyclinicSlug(polyclinicSlug);
    PolyclinicResponse response = polyclinicService.update(user, request);
    return WebResponse.<PolyclinicResponse>builder()
        .data(response)
        .build();
  }

  @DeleteMapping(path = "/api/polyclinics/{polyclinicSlug}/{polyclinicId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<String> delete(User user,
                                    @PathVariable("polyclinicSlug") String polyclinicSlug,
                                    @PathVariable("polyclinicId") UUID polyclinicId) {
    PolyclinicPath polyclinicPath = new PolyclinicPath();
    polyclinicPath.setPolyclinicSlug(polyclinicSlug);
    polyclinicPath.setPolyclinicId(polyclinicId);

    polyclinicService.delete(user,polyclinicPath);
    return WebResponse.<String>builder()
        .data("OK")
        .build();
  }
}
