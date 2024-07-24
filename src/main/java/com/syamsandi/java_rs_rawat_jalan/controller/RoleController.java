package com.syamsandi.java_rs_rawat_jalan.controller;

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
  public WebResponse<RoleResponse> create(@RequestBody RoleRequest request) {
    RoleResponse response = roleService.create(request);
    return WebResponse.<RoleResponse>builder()
        .data(response)
        .build();
  }

  @GetMapping(path = "/api/roles/{roleId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<RoleResponse> get(@PathVariable("roleId") String roleId) {
    UUID roleUuid = UUID.fromString(roleId);
    RoleResponse response = roleService.get(roleUuid);
    return WebResponse.<RoleResponse>builder()
        .data(response)
        .build();
  }

  @GetMapping(path = "/api/roles",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<List<RoleResponse>> getAll() {
    List<RoleResponse> responses = roleService.getAll();
    return WebResponse.<List<RoleResponse>>builder()
        .data(responses)
        .build();
  }

  @PutMapping(path = "/api/roles/{roleId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<RoleResponse> update(@RequestBody RoleRequest request, @PathVariable("roleId") String roleId) {
    UUID roleUuid = UUID.fromString(roleId);
    RoleResponse response = roleService.update(request, roleUuid);
    return WebResponse.<RoleResponse>builder()
        .data(response)
        .build();
  }

  @DeleteMapping(path ="/api/roles/{roleId}",
  produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<String> delete(@PathVariable("roleId") String roleId){
    UUID roleUuid = UUID.fromString(roleId);
    roleService.delete(roleUuid);
    return WebResponse.<String>builder()
        .data("OK")
        .build();
  }
}
