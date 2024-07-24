package com.syamsandi.java_rs_rawat_jalan.controller;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.RoleRequest;
import com.syamsandi.java_rs_rawat_jalan.model.RoleResponse;
import com.syamsandi.java_rs_rawat_jalan.model.UserResponse;
import com.syamsandi.java_rs_rawat_jalan.model.WebResponse;
import com.syamsandi.java_rs_rawat_jalan.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class RoleController {

  @Autowired
  private RoleService roleService;

  @PostMapping(path = "/api/roles",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<RoleResponse> create(User user, @RequestBody RoleRequest request) {
    RoleResponse response = roleService.create(user,request);
    return WebResponse.<RoleResponse>builder()
        .data(response)
        .build();
  }

  @GetMapping(path = "/api/roles/{roleId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<RoleResponse> get(User user,@PathVariable("roleId") String roleId) {
    UUID roleUuid = UUID.fromString(roleId);
    RoleResponse response = roleService.get(user,roleUuid);
    return WebResponse.<RoleResponse>builder()
        .data(response)
        .build();
  }

  @GetMapping(path = "/api/roles",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<List<RoleResponse>> getAll(User user) {
    List<RoleResponse> responses = roleService.getAll(user);
    return WebResponse.<List<RoleResponse>>builder()
        .data(responses)
        .build();
  }

  @PutMapping(path = "/api/roles/{roleId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<RoleResponse> update(User user,@RequestBody RoleRequest request, @PathVariable("roleId") String roleId) {
    UUID roleUuid = UUID.fromString(roleId);
    RoleResponse response = roleService.update(user,request, roleUuid);
    return WebResponse.<RoleResponse>builder()
        .data(response)
        .build();
  }

  @DeleteMapping(path ="/api/roles/{roleId}",
  produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<String> delete(User user,@PathVariable("roleId") String roleId){
    UUID roleUuid = UUID.fromString(roleId);
    roleService.delete(user,roleUuid);
    return WebResponse.<String>builder()
        .data("OK")
        .build();
  }
}
