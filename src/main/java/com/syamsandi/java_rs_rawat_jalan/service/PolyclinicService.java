package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.polyclinic.CreatePolyclinicRequest;
import com.syamsandi.java_rs_rawat_jalan.model.polyclinic.PolyclinicPath;
import com.syamsandi.java_rs_rawat_jalan.model.polyclinic.PolyclinicResponse;
import com.syamsandi.java_rs_rawat_jalan.model.polyclinic.UpdatePolyclinicRequest;

import java.util.List;
import java.util.UUID;


public interface PolyclinicService {

  PolyclinicResponse create(User user, CreatePolyclinicRequest request);

  PolyclinicResponse get(User user, PolyclinicPath polyclinicPath);

  List<PolyclinicResponse> getAll(User user);

  PolyclinicResponse update(User user, UpdatePolyclinicRequest request);

  void delete(User user, PolyclinicPath polyclinicPath);
}
