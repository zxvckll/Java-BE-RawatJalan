package com.syamsandi.java_rs_rawat_jalan.controller;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.*;
import com.syamsandi.java_rs_rawat_jalan.model.user_role.PagingUserRoleRequest;
import com.syamsandi.java_rs_rawat_jalan.model.user_role.UserRoleRequest;
import com.syamsandi.java_rs_rawat_jalan.model.user_role.UserRoleResponse;
import com.syamsandi.java_rs_rawat_jalan.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users/roles")
public class UserRoleController {

  @Autowired
  private UserRoleService userRoleService;

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<UserRoleResponse> create(User user, @RequestBody UserRoleRequest request) {
    UserRoleResponse response = userRoleService.create(user, request);
    return WebResponse.<UserRoleResponse>builder()
        .data(response)
        .build();
  }

  @GetMapping(path = "/{userRoleId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<UserRoleResponse> get(User user, @PathVariable UUID userRoleId) {
    UserRoleResponse response = userRoleService.get(user, userRoleId);
    return WebResponse.<UserRoleResponse>builder()
        .data(response)
        .build();
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<List<UserRoleResponse>> getAll(User user,
                                                    @RequestParam(value = "user_id", required = false) UUID userId,
                                                    @RequestParam(value = "role_id", required = false) UUID roleId,
                                                    @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                    @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
    PagingUserRoleRequest request = new PagingUserRoleRequest();
    request.setUserId(userId);
    request.setRoleId(roleId);
    request.setPage(page);
    request.setSize(size);

    Page<UserRoleResponse> responses = userRoleService.getAll(user, request);

    return WebResponse.<List<UserRoleResponse>>builder()
        .data(responses.getContent())
        .paging(PagingResponse.builder()
            .currentPage(responses.getNumber())
            .totalPage(responses.getTotalPages())
            .size(responses.getSize())
            .build())
        .build();
  }

  @PutMapping(path = "/{userRoleId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<UserRoleResponse> update(User user, @RequestBody UserRoleRequest request, @PathVariable UUID userRoleId) {
    UserRoleResponse response = userRoleService.update(user, request, userRoleId);
    return WebResponse.<UserRoleResponse>builder()
        .data(response)
        .build();
  }

  @DeleteMapping(path = "/{userRoleId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<String> delete(User user, @PathVariable UUID userRoleId) {
    userRoleService.delete(user, userRoleId);
    return WebResponse.<String>builder()
        .data("OK")
        .build();
  }
}
