package com.syamsandi.java_rs_rawat_jalan.controller;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.clinic.ClinicPath;
import com.syamsandi.java_rs_rawat_jalan.model.clinic.CreateClinicRequest;
import com.syamsandi.java_rs_rawat_jalan.model.clinic.ClinicResponse;
import com.syamsandi.java_rs_rawat_jalan.model.WebResponse;
import com.syamsandi.java_rs_rawat_jalan.model.clinic.UpdateClinicRequest;
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

  @PostMapping(path = "/{polyclinicSlug}/{polyclinicId}/clinics", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<ClinicResponse> create(User user,
                                            @RequestBody CreateClinicRequest request,
                                            @PathVariable("polyclinicId") UUID polyclinicId,
                                            @PathVariable("polyclinicSlug") String polyclinicSlug) {
                                            {
      request.setPolyclinicId(polyclinicId);
      request.setPolyclinicSlug(polyclinicSlug);

      ClinicResponse response = clinicService.create(user, request);
      return WebResponse.<ClinicResponse>builder().data(response).build();
    }
  }

  @GetMapping(path = "/{polyclinicSlug}/{polyclinicId}/clinics/{clinicSlug}/{clinicId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<ClinicResponse> get(User user,
                                         @PathVariable("polyclinicId") UUID polyclinicId,
                                         @PathVariable("polyclinicSlug") String polyclinicSlug,
                                         @PathVariable("clinicId") UUID clinicId,
                                         @PathVariable("clinicSlug") String clinicSlug) {
    ClinicPath clinicPath = new ClinicPath();
    clinicPath.setClinicSlug(clinicSlug);
    clinicPath.setClinicId(clinicId);
    clinicPath.setPolyclinicSlug(polyclinicSlug);
    clinicPath.setPolyclinicId(polyclinicId);

    ClinicResponse response = clinicService.get(user, clinicPath);
    return WebResponse.<ClinicResponse>builder().data(response).build();
  }

  @GetMapping(path = "/{polyclinicSlug}/{polyclinicId}/clinics", produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<List<ClinicResponse>> getAll(User user,
                                                  @PathVariable("polyclinicId") UUID polyclinicId,
                                                  @PathVariable("polyclinicSlug") String polyclinicSlug) {
    ClinicPath clinicPath = new ClinicPath();
    clinicPath.setPolyclinicId(polyclinicId);
    clinicPath.setPolyclinicSlug(polyclinicSlug);

    List<ClinicResponse> responses = clinicService.getAll(user, clinicPath);
    return WebResponse.<List<ClinicResponse>>builder().data(responses).build();
  }

  @PutMapping(path = "/{polyclinicSlug}/{polyclinicId}/clinics/{clinicSlug}/{clinicId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<ClinicResponse> update(User user,
                                            @RequestBody UpdateClinicRequest request,
                                            @PathVariable("polyclinicId") UUID polyclinicId,
                                            @PathVariable("polyclinicSlug") String polyclinicSlug,
                                            @PathVariable("clinicId") UUID clinicId,
                                            @PathVariable("clinicSlug") String clinicSlug) {
    request.setPolyclinicId(polyclinicId);
    request.setPolyclinicSlug(polyclinicSlug);
    request.setClinicId(clinicId);
    request.setClinicSlug(clinicSlug);

    ClinicResponse response = clinicService.update(user, request);
    return WebResponse.<ClinicResponse>builder().data(response).build();
  }

  @DeleteMapping(path = "/{polyclinicSlug}/{polyclinicId}/clinics/{clinicSlug}/{clinicId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<String> delete(User user,
                                    @PathVariable("polyclinicId") UUID polyclinicId,
                                    @PathVariable("polyclinicSlug") String polyclinicSlug,
                                    @PathVariable("clinicId") UUID clinicId,
                                    @PathVariable("clinicSlug") String clinicSlug) {

    ClinicPath clinicPath = new ClinicPath();
    clinicPath.setPolyclinicSlug(polyclinicSlug);
    clinicPath.setPolyclinicId(polyclinicId);
    clinicPath.setClinicSlug(clinicSlug);
    clinicPath.setClinicId(clinicId);

    clinicService.delete(user, clinicPath);
    return WebResponse.<String>builder().data("OK").build();
  }
}
