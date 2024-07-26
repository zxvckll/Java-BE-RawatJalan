package com.syamsandi.java_rs_rawat_jalan.controller;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.ClinicRequest;
import com.syamsandi.java_rs_rawat_jalan.model.ClinicResponse;
import com.syamsandi.java_rs_rawat_jalan.model.WebResponse;
import com.syamsandi.java_rs_rawat_jalan.service.ClinicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/polyclinics")
public class ClinicController {

  @Autowired
  private ClinicService clinicService;

  @PostMapping(path = "/{polyclinicId}/clinics", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<ClinicResponse> create(User user, @RequestBody ClinicRequest request, @PathVariable("polyclinicId") UUID polyclinicId) {
    request.setPolyclinicId(polyclinicId);
    ClinicResponse response = clinicService.create(user, request);
    return WebResponse.<ClinicResponse>builder().data(response).build();
  }

  @GetMapping(path = "/{polyclinicSlug}/clinics/{clinicSlug}", produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<ClinicResponse> get(User user, @PathVariable("polyclinicSlug") String polyclinicSlug, @PathVariable("clinicSlug") String clinicSlug) {
    ClinicResponse response = clinicService.get(user, polyclinicSlug, clinicSlug);
    return WebResponse.<ClinicResponse>builder().data(response).build();
  }

  @GetMapping(path = "/{polyclinicSlug}/clinics", produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<List<ClinicResponse>> getAll(User user, @PathVariable("polyclinicSlug") String polyclinicSlug) {
    List<ClinicResponse> responses = clinicService.getAll(user, polyclinicSlug);
    return WebResponse.<List<ClinicResponse>>builder().data(responses).build();
  }

  @PutMapping(path = "/{polyclinicId}/clinics/{clinicId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<ClinicResponse> update(User user, @RequestBody ClinicRequest request, @PathVariable("polyclinicId") UUID polyclinicId, @PathVariable("clinicId") UUID clinicId) {
    request.setPolyclinicId(polyclinicId);
    ClinicResponse response = clinicService.update(user, request, clinicId);
    return WebResponse.<ClinicResponse>builder().data(response).build();
  }

  @DeleteMapping(path = "/{polyclinicId}/clinics/{clinicId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<String> delete(User user, @PathVariable("polyclinicId") UUID polyclinicId, @PathVariable("clinicId") UUID clinicId) {
    clinicService.delete(user, polyclinicId, clinicId);
    return WebResponse.<String>builder().data("OK").build();
  }
}
