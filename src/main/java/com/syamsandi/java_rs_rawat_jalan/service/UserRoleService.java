package com.syamsandi.java_rs_rawat_jalan.service;


import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.SearchUserRoleRequest;
import com.syamsandi.java_rs_rawat_jalan.model.UserRoleRequest;
import com.syamsandi.java_rs_rawat_jalan.model.UserRoleResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface UserRoleService {

  UserRoleResponse create(User user, UserRoleRequest request);

  UserRoleResponse get(User user,UUID id);

  Page<UserRoleResponse> getAll(User user,SearchUserRoleRequest request);

  UserRoleResponse update(User user,UserRoleRequest request,UUID id);

  void delete(User user,UUID id);

}
