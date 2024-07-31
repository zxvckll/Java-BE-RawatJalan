package com.syamsandi.java_rs_rawat_jalan.controller;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.PagingResponse;
import com.syamsandi.java_rs_rawat_jalan.model.user_profile.SearchUserProfileRequest;
import com.syamsandi.java_rs_rawat_jalan.model.user_profile.UserProfileRequest;
import com.syamsandi.java_rs_rawat_jalan.model.user_profile.UserProfileResponse;
import com.syamsandi.java_rs_rawat_jalan.model.WebResponse;
import com.syamsandi.java_rs_rawat_jalan.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/current")
public class UserProfileController {

  @Autowired
  private UserProfileService userProfileService;


  @PostMapping(path = "/profile")
  public WebResponse<String> create(@RequestBody UserProfileRequest request, User user) {
    userProfileService.create(request, user);
    return WebResponse.<String>builder()
        .data("OK")
        .build();
  }

  @GetMapping(path = "/profile")
  public WebResponse<UserProfileResponse> get(User user) {
    UserProfileResponse response = userProfileService.get(user);
    return WebResponse.<UserProfileResponse>builder()
        .data(response)
        .build();
  }

  @PutMapping(path = "/profile",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<UserProfileResponse> update(@RequestBody UserProfileRequest request, User user) {
    UserProfileResponse response = userProfileService.update(request, user);

    return WebResponse.<UserProfileResponse>builder()
        .data(response)
        .build();
  }

  @GetMapping(path = "/profiles")
  public WebResponse<List<UserProfileResponse>> search(User user,
                                                       @RequestParam(value = "name", required = false) String name,
                                                       @RequestParam(value = "name", required = false) String dateOfBirth,
                                                       @RequestParam(value = "name", required = false) String address,
                                                       @RequestParam(value = "name", required = false,defaultValue = "0") Integer page,
                                                       @RequestParam(value = "name", required = false,defaultValue = "10") Integer size){

    SearchUserProfileRequest request = new SearchUserProfileRequest();
    request.setName(name);
    request.setAddress(address);
    request.setDateOfBirth(dateOfBirth);
    request.setSize(size);
    request.setPage(page);

    Page<UserProfileResponse> responses = userProfileService.search(user, request);

    return WebResponse.<List<UserProfileResponse>>builder()
        .data(responses.getContent())
        .paging(PagingResponse.builder()
            .currentPage(responses.getNumber())
            .totalPage(responses.getTotalPages())
            .size(responses.getSize())
            .build()
        )
        .build();
  }

}
