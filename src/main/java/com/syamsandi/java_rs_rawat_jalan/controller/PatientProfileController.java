package com.syamsandi.java_rs_rawat_jalan.controller;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.PagingResponse;
import com.syamsandi.java_rs_rawat_jalan.model.WebResponse;
import com.syamsandi.java_rs_rawat_jalan.model.doctor_profile.*;
import com.syamsandi.java_rs_rawat_jalan.model.patient_profile.*;
import com.syamsandi.java_rs_rawat_jalan.service.DoctorProfileService;
import com.syamsandi.java_rs_rawat_jalan.service.PatientProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class PatientProfileController {

  @Autowired
  private PatientProfileService patientProfileService;

  @PostMapping(path = "/patient",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<PatientProfileResponse> create(@RequestBody CreatePatientProfileRequest request, User user) {

    PatientProfileResponse response = patientProfileService.create(request, user);
    return WebResponse.<PatientProfileResponse>builder()
        .data(response)
        .build();
  }

  @GetMapping(path = "/patient/{patientSlug}/{patientId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<PatientProfileResponse> get(User user,
                                                @PathVariable("patientId") UUID patientId,
                                                @PathVariable("patientSlug") String patientSlug){

    PatientProfilePath patientProfilePath =new PatientProfilePath();
    patientProfilePath.setPatientProfileId(patientId);
    patientProfilePath.setPatientProfileSlug(patientSlug);

    PatientProfileResponse response = patientProfileService.get(patientProfilePath,user);
    return WebResponse.<PatientProfileResponse>builder()
        .data(response).build();
  }

  @PutMapping(path = "/patient/{patientSlug}/{patientId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<PatientProfileResponse> update(User user,
                                                   @RequestBody UpdatePatientProfileRequest request,
                                                   @PathVariable("patientSlug") String patientSlug,
                                                   @PathVariable("patientId") UUID patientId
                                                   ) {
    request.setPatientProfileId(patientId);
    request.setPatientProfileSlug(patientSlug);

    PatientProfileResponse response = patientProfileService.update(request, user);
    return WebResponse.<PatientProfileResponse>builder()
        .data(response)
        .build();
  }

  @DeleteMapping(path = "/patient/{patientSlug}/{patientId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<String> delete(User user,
                                                   @PathVariable("patientSlug") String patientSlug,
                                                   @PathVariable("patientId") UUID patientId
                                                   ){
    PatientProfilePath patientProfilePath = new PatientProfilePath();
    patientProfilePath.setPatientProfileSlug(patientSlug);
    patientProfilePath.setPatientProfileId(patientId);


    patientProfileService.delete(user, patientProfilePath);

    return WebResponse.<String>builder()
        .data("OK")
        .build();
  }

  @GetMapping(path = "/patients",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<List<PatientProfileResponse>> search(
                                                         @RequestParam(value = "medical_history", required = false) String medical_history,
                                                         @RequestParam(value = "current_treatment", required = false) String current_treatment,
                                                         @RequestParam(value = "noRM", required = false) String noRM,
                                                         @RequestParam(value = "page", required = false,defaultValue = "0") Integer page,
                                                         @RequestParam(value = "size", required = false,defaultValue = "10") Integer size){
    SearchPatientProfileRequest request = new SearchPatientProfileRequest();
    request.setPage(page);
    request.setSize(size);
    request.setNoRM(noRM);
    request.setMedical_history(medical_history);
    request.setCurrent_treatment(current_treatment);


    Page<PatientProfileResponse> responses = patientProfileService.search(request);

    return WebResponse.<List<PatientProfileResponse>>builder()
        .data(responses.getContent())
        .paging(PagingResponse.builder()
            .size(responses.getSize())
            .totalPage(responses.getTotalPages())
            .currentPage(responses.getNumber())
            .build())
        .build();
  }


}
