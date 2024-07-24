package com.syamsandi.java_rs_rawat_jalan.controller;

import com.syamsandi.java_rs_rawat_jalan.model.*;
import com.syamsandi.java_rs_rawat_jalan.service.RoleService;
import com.syamsandi.java_rs_rawat_jalan.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class UserRoleController {

  @Autowired
  private UserRoleService userRoleService;

  @PostMapping(path = "/api/users/roles",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<UserRoleResponse> create(@RequestBody UserRoleRequest request) {
    UserRoleResponse response = userRoleService.create(request);
    return WebResponse.<UserRoleResponse>builder()
        .data(response)
        .build();
  }

  @GetMapping(path = "/api/users/roles/{userRoleId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<UserRoleResponse> get(@PathVariable("userRoleId") String userRoleId) {
    UUID roleUuid = UUID.fromString(userRoleId);
    UserRoleResponse response = userRoleService.get(roleUuid);
    return WebResponse.<UserRoleResponse>builder()
        .data(response)
        .build();
  }

  @GetMapping(path = "/api/users/roles",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<List<UserRoleResponse>> getAll(@RequestParam(value = "user_id", required = false) String userId,
                                                    @RequestParam(value = "role_id", required = false) String roleId,
                                                    @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                    @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
    UUID roleUuid = roleId != null ? UUID.fromString(roleId) : null;
    UUID userUuid = userId != null ? UUID.fromString(userId) : null;

    SearchUserRoleRequest request = new SearchUserRoleRequest();
    request.setUserId(userUuid);
    request.setRoleId(roleUuid);
    request.setPage(page);
    request.setSize(size);

    Page<UserRoleResponse> responses = userRoleService.getAll(request);

    return WebResponse.<List<UserRoleResponse>>builder()
        .data(responses.getContent())
        .paging(PagingResponse.builder()
            .currentPage(responses.getNumber())
            .totalPage(responses.getTotalPages())
            .size(responses.getSize())
            .build())
        .build();
  }

  @PutMapping(path = "/api/users/roles/{userRoleId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<UserRoleResponse> update(@RequestBody UserRoleRequest request, @PathVariable("userRoleId") String userRoleId) {
    UUID roleUuid = UUID.fromString(userRoleId);
    UserRoleResponse response = userRoleService.update(request, roleUuid);
    return WebResponse.<UserRoleResponse>builder()
        .data(response)
        .build();
  }

  @DeleteMapping(path = "/api/users/roles/{userRoleId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<String> delete(@PathVariable("userRoleId") String userRoleId) {
    UUID roleUuid = UUID.fromString(userRoleId);
    userRoleService.delete(roleUuid);
    return WebResponse.<String>builder()
        .data("OK")
        .build();
  }
}
