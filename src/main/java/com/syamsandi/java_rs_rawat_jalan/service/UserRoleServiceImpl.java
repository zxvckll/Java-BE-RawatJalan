package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.Role;
import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.entity.UserRole;
import com.syamsandi.java_rs_rawat_jalan.model.PagingUserRoleRequest;
import com.syamsandi.java_rs_rawat_jalan.model.user_role.UserRoleRequest;
import com.syamsandi.java_rs_rawat_jalan.model.user_role.UserRoleResponse;
import com.syamsandi.java_rs_rawat_jalan.repository.RoleRepository;
import com.syamsandi.java_rs_rawat_jalan.repository.UserRepository;
import com.syamsandi.java_rs_rawat_jalan.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class UserRoleServiceImpl implements UserRoleService {

  @Autowired
  private ValidatorService validatorService;

  @Autowired
  private UserRoleRepository userRoleRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private UserRoleUtils userRoleUtils;

  @Transactional
  @Override
  public UserRoleResponse create(User checkUser,UserRoleRequest request) {
    validatorService.validate(request);
    userRoleUtils.checkAdminRole(checkUser);

    User user = userRepository.findById(request.getUserId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found")
    );

    Role role = roleRepository.findById(request.getRoleId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found")
    );

    if(userRoleRepository.existsByUserAndRole(user,role)){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "UserRole already exist");
    }

    UserRole userRole = new UserRole();
    userRole.setId(UUID.randomUUID());
    userRole.setUser(user);
    userRole.setRole(role);
    userRoleRepository.save(userRole);

    return toUserRoleResponse(userRole);
  }

  @Transactional(readOnly = true)
  @Override
  public UserRoleResponse get(User user,UUID id) {
    userRoleUtils.checkAdminRole(user);

    UserRole userRole = userRoleRepository.findById(id).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Role not found")
    );
    return toUserRoleResponse(userRole);
  }

  @Transactional(readOnly = true)
  @Override
  public Page<UserRoleResponse> getAll(User checkUser, PagingUserRoleRequest request) {
    validatorService.validate(request);

    userRoleUtils.checkAdminRole(checkUser);

    // Create Pageable object for pagination
    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

    // Find User and Role if provided
    User user = request.getUserId() != null ?
        userRepository.findById(request.getUserId()).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found")
        ) : null;

    Role role = request.getRoleId() != null ?
        roleRepository.findById(request.getRoleId()).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found")
        ) : null;

    // Fetch data based on available filters
    Page<UserRole> userRoles;
    if (user != null && role != null) {
      userRoles = userRoleRepository.findAllByUserAndRole(user, role, pageable);
    } else if (user != null) {
      userRoles = userRoleRepository.findAllByUser(user, pageable);
    } else if (role != null) {
      userRoles = userRoleRepository.findAllByRole(role, pageable);
    } else {
      userRoles = userRoleRepository.findAll(pageable);
    }

    List<UserRoleResponse> userRoleResponses = userRoles.stream()
        .map(this::toUserRoleResponse)
        .toList();

    return new PageImpl<>(userRoleResponses, pageable, userRoles.getTotalElements());
  }


  @Transactional
  @Override
  public UserRoleResponse update(User checkUser,UserRoleRequest request, UUID id) {
    userRoleUtils.checkAdminRole(checkUser);

    UserRole userRole = userRoleRepository.findById(id).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Role not found")
    );
    User user = userRepository.findById(request.getUserId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found")
    );

    Role role = roleRepository.findById(request.getRoleId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found")
    );
    userRole.setUser(user);
    userRole.setRole(role);
    userRoleRepository.save(userRole);
    return toUserRoleResponse(userRole);
  }

  @Transactional
  @Override
  public void delete(User user,UUID id) {
    userRoleUtils.checkAdminRole(user);

    UserRole userRole = userRoleRepository.findById(id).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Role not found")
    );
    userRoleRepository.delete(userRole);
  }

  private UserRoleResponse toUserRoleResponse(UserRole userRole) {
    return UserRoleResponse.builder()
        .userRoleId(userRole.getId())
        .userId(userRole.getUser().getId())
        .roleId(userRole.getRole().getId())
        .build();
  }
}
