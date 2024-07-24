package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.Role;
import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.entity.UserRole;
import com.syamsandi.java_rs_rawat_jalan.model.RoleRequest;
import com.syamsandi.java_rs_rawat_jalan.model.RoleResponse;
import com.syamsandi.java_rs_rawat_jalan.repository.RoleRepository;
import com.syamsandi.java_rs_rawat_jalan.repository.UserProfileRepository;
import com.syamsandi.java_rs_rawat_jalan.repository.UserRepository;
import com.syamsandi.java_rs_rawat_jalan.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

  @Autowired
  private ValidatorService validatorService;

  @Autowired
  private RoleRepository roleRepository;
  @Autowired
  private UserRoleRepository userRoleRepository;

  @Autowired
  private UserRoleUtils userRoleUtils;


  @Transactional
  @Override
  public RoleResponse create(User user,RoleRequest request) {
    validatorService.validate(request);
    userRoleUtils.checkAdminRole(user);

    Role role = new Role();
    role.setId(UUID.randomUUID());
    role.setName(request.getName());
    return toRoleResponse(role);
  }

  @Transactional(readOnly = true)
  @Override
  public RoleResponse get(User user,UUID id) {
    userRoleUtils.checkAdminRole(user);

    Role role = roleRepository.findFirstById(id).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found")
    );

    return toRoleResponse(role);
  }

  @Transactional(readOnly = true)
  @Override
  public List<RoleResponse> getAll(User user) {
    userRoleUtils.checkAdminRole(user);

    List<Role> roles = roleRepository.findAll();
    return roles.stream().map(this::toRoleResponse).toList();
  }

  @Transactional
  @Override
  public RoleResponse update(User user,RoleRequest request, UUID id) {
    validatorService.validate(request);

    Role role = roleRepository.findFirstById(id).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found")
    );
    role.setName(request.getName());
    return toRoleResponse(role);
  }

  @Transactional
  @Override
  public void delete(User user,UUID id) {
    userRoleUtils.checkAdminRole(user);

    Role role = roleRepository.findFirstById(id).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found")
    );
    roleRepository.delete(role);
  }


  private RoleResponse toRoleResponse(Role role) {
    return RoleResponse.builder()
        .id(role.getId())
        .name(role.getName())
        .build();
  }
}
