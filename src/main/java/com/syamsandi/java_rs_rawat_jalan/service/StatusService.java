package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.role.CreateRoleRequest;
import com.syamsandi.java_rs_rawat_jalan.model.role.RoleResponse;
import com.syamsandi.java_rs_rawat_jalan.model.status.CreateStatusRequest;
import com.syamsandi.java_rs_rawat_jalan.model.status.StatusPath;
import com.syamsandi.java_rs_rawat_jalan.model.status.StatusResponse;
import com.syamsandi.java_rs_rawat_jalan.model.status.UpdateStatusRequest;

import java.util.List;
import java.util.UUID;


public interface StatusService {

  StatusResponse create(User user, CreateStatusRequest request);

  StatusResponse get(User user, StatusPath statusPath);

  List<StatusResponse> getAll(User user);

  StatusResponse update(User user, UpdateStatusRequest request);

  void delete(User user,StatusPath statusPath);
}
