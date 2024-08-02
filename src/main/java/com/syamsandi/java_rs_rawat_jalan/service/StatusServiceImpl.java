package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.Status;
import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.status.CreateStatusRequest;
import com.syamsandi.java_rs_rawat_jalan.model.status.StatusPath;
import com.syamsandi.java_rs_rawat_jalan.model.status.StatusResponse;
import com.syamsandi.java_rs_rawat_jalan.model.status.UpdateStatusRequest;
import com.syamsandi.java_rs_rawat_jalan.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StatusServiceImpl implements StatusService {

  @Autowired
  private ValidatorService validatorService;

  @Autowired
  private UserRoleUtils userRoleUtils;
  @Autowired
  private StatusRepository statusRepository;

  @Override
  public StatusResponse create(User user, CreateStatusRequest request) {
    validatorService.validate(request);
    userRoleUtils.isAdmin(user);

    Status status = new Status();
    status.setId(UUID.randomUUID());
    status.setName(request.getName());

    statusRepository.save(status);
    return toStatusResponse(status);
  }

  @Override
  public StatusResponse get(User user, StatusPath statusPath) {

    Status status = statusRepository.findFirstById(statusPath.getStatusId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "status not found")
    );

    return toStatusResponse(status);
  }

  @Override
  public List<StatusResponse> getAll(User user) {
    List<Status> statuses = statusRepository.findAll().stream().toList();

    return statuses.stream().map(this::toStatusResponse).collect(Collectors.toList());
  }

  @Override
  public StatusResponse update(User user, UpdateStatusRequest request) {
    validatorService.validate(request);
    userRoleUtils.isAdmin(user);

    Status status = statusRepository.findById(request.getStatusId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "status not found")
    );
    status.setName(request.getName());
    statusRepository.save(status);

    return toStatusResponse(status);
  }

  @Override
  public void delete(User user, StatusPath statusPath) {
    userRoleUtils.isAdmin(user);

    Status status = statusRepository.findById(statusPath.getStatusId()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "status not found")
    );
    statusRepository.delete(status);
  }

  private StatusResponse toStatusResponse(Status status){
    return StatusResponse.builder()
        .statusId(status.getId())
        .name(status.getName())
        .build();
  }
}
