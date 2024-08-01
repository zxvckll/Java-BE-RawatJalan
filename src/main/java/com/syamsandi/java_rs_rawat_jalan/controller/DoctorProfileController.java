package com.syamsandi.java_rs_rawat_jalan.controller;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.PagingResponse;
import com.syamsandi.java_rs_rawat_jalan.model.WebResponse;
import com.syamsandi.java_rs_rawat_jalan.model.doctor_profile.*;
import com.syamsandi.java_rs_rawat_jalan.service.DoctorProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clinics/{clinicSlug}/{clinicId}")
public class DoctorProfileController {

  @Autowired
  private DoctorProfileService doctorProfileService;

  @PostMapping(path = "/doctor",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<DoctorProfileResponse> create(@RequestBody CreateDoctorProfileRequest request, User user,
                                                   @PathVariable("clinicSlug") String clinicSlug,
                                                   @PathVariable("clinicId") UUID clinicId) {

    request.setClinicId(clinicId);
    request.setClinicSlug(clinicSlug);
    DoctorProfileResponse response = doctorProfileService.create(request, user);
    return WebResponse.<DoctorProfileResponse>builder()
        .data(response)
        .build();
  }

  @GetMapping(path = "/doctors/{doctorSlug}/{doctorId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<DoctorProfileResponse> get(@PathVariable("doctorSlug") String doctorSlug,
                                                @PathVariable("doctorId") UUID doctorId,
                                                @PathVariable("clinicSlug") String clinicSlug,
                                                @PathVariable("clinicId") UUID clinicId) {
    DoctorProfilePath doctorProfilePath = new DoctorProfilePath();
    doctorProfilePath.setDoctorProfileId(doctorId);
    doctorProfilePath.setDoctorProfileSlug(doctorSlug);
    doctorProfilePath.setClinicId(clinicId);
    doctorProfilePath.setClinicSlug(clinicSlug);

    DoctorProfileResponse response = doctorProfileService.get(doctorProfilePath);
    return WebResponse.<DoctorProfileResponse>builder()
        .data(response).build();
  }

  @PutMapping(path = "/doctors/{doctorSlug}/{doctorId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<DoctorProfileResponse> update(User user,
                                                   @RequestBody UpdateDoctorProfileRequest request,
                                                   @PathVariable("doctorSlug") String doctorSlug,
                                                   @PathVariable("doctorId") UUID doctorId,
                                                   @PathVariable("clinicSlug") String clinicSlug,
                                                   @PathVariable("clinicId") UUID clinicId) {
    request.setDoctorProfileSlug(doctorSlug);
    request.setDoctorProfileId(doctorId);
    request.setClinicId(clinicId);
    request.setClinicSlug(clinicSlug);
    DoctorProfileResponse response = doctorProfileService.update(request, user);

    return WebResponse.<DoctorProfileResponse>builder()
        .data(response)
        .build();
  }

  @DeleteMapping(path = "/doctors/{doctorSlug}/{doctorId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<String> delete(User user,
                                                   @PathVariable("doctorSlug") String doctorSlug,
                                                   @PathVariable("doctorId") UUID doctorId,
                                                   @PathVariable("clinicSlug") String clinicSlug,
                                                   @PathVariable("clinicId") UUID clinicId
                                                   ){
    DoctorProfilePath doctorProfilePath = new DoctorProfilePath();
    doctorProfilePath.setClinicId(clinicId);
    doctorProfilePath.setClinicSlug(clinicSlug);
    doctorProfilePath.setDoctorProfileId(doctorId);
    doctorProfilePath.setDoctorProfileSlug(doctorSlug);

    doctorProfileService.delete(user, doctorProfilePath);

    return WebResponse.<String>builder()
        .data("OK")
        .build();
  }

  @GetMapping(path = "/doctors",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<List<DoctorProfileResponse>> search(@PathVariable("clinicSlug") String clinicSlug,
                                                         @PathVariable("clinicId") UUID clinicId,
                                                         @RequestParam(value = "experience", required = false) String experience,
                                                         @RequestParam(value = "course", required = false) String course,
                                                         @RequestParam(value = "education", required = false) String education,
                                                         @RequestParam(value = "organization", required = false) String organization,
                                                         @RequestParam(value = "page", required = false,defaultValue = "0") Integer page,
                                                         @RequestParam(value = "size", required = false,defaultValue = "10") Integer size){
    SearchDoctorProfileRequest request = new SearchDoctorProfileRequest();
    request.setPage(page);
    request.setSize(size);
    request.setExperience(experience);
    request.setCourse(course);
    request.setEducation(education);
    request.setOrganization(organization);

    DoctorProfilePath doctorProfilePath = new DoctorProfilePath();
    doctorProfilePath.setClinicId(clinicId);
    doctorProfilePath.setClinicSlug(clinicSlug);

    Page<DoctorProfileResponse> responses = doctorProfileService.search(request, doctorProfilePath);

    return WebResponse.<List<DoctorProfileResponse>>builder()
        .data(responses.getContent())
        .paging(PagingResponse.builder()
            .size(responses.getSize())
            .totalPage(responses.getTotalPages())
            .currentPage(responses.getNumber())
            .build())
        .build();
  }


}
