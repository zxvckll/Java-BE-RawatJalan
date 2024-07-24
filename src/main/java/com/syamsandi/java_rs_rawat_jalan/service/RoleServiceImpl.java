package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.Role;
import com.syamsandi.java_rs_rawat_jalan.model.RoleRequest;
import com.syamsandi.java_rs_rawat_jalan.model.RoleResponse;
import com.syamsandi.java_rs_rawat_jalan.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

  @Autowired
  private ValidatorService validatorService;

  @Autowired
  private RoleRepository roleRepository;

  @Transactional
  @Override
  public RoleResponse create(RoleRequest request) {
    validatorService.validate(request);
    Role role = new Role();
    role.setId(UUID.randomUUID());
    role.setName(request.getName());
    return toRoleResponse(role);
  }

  @Transactional(readOnly = true)
  @Override
  public RoleResponse get(UUID id) {
    Role role = roleRepository.findFirstById(id).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found")
    );

    return toRoleResponse(role);
  }

  @Transactional(readOnly = true)
  @Override
  public List<RoleResponse> getAll() {
    List<Role> roles = roleRepository.findAll();
    return roles.stream().map(this::toRoleResponse).toList();
  }

  @Transactional
  @Override
  public RoleResponse update(RoleRequest request, UUID id) {
    validatorService.validate(request);

    Role role = roleRepository.findFirstById(id).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found")
    );
    role.setName(request.getName());
    return toRoleResponse(role);
  }

  @Transactional
  @Override
  public void delete(UUID id) {
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
