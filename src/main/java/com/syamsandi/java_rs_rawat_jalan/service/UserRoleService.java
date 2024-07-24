package com.syamsandi.java_rs_rawat_jalan.service;


import com.syamsandi.java_rs_rawat_jalan.model.SearchUserRoleRequest;
import com.syamsandi.java_rs_rawat_jalan.model.UserRoleRequest;
import com.syamsandi.java_rs_rawat_jalan.model.UserRoleResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface UserRoleService {

  UserRoleResponse create(UserRoleRequest request);

  UserRoleResponse get(UUID id);

  Page<UserRoleResponse> getAll(SearchUserRoleRequest request);

  UserRoleResponse update(UserRoleRequest request,UUID id);

  void delete(UUID id);

}
