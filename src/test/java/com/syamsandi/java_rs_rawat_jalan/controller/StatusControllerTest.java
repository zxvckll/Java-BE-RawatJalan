package com.syamsandi.java_rs_rawat_jalan.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syamsandi.java_rs_rawat_jalan.entity.Role;
import com.syamsandi.java_rs_rawat_jalan.entity.Status;
import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.entity.UserRole;
import com.syamsandi.java_rs_rawat_jalan.model.WebResponse;
import com.syamsandi.java_rs_rawat_jalan.model.role.CreateRoleRequest;
import com.syamsandi.java_rs_rawat_jalan.model.role.RoleResponse;
import com.syamsandi.java_rs_rawat_jalan.model.status.CreateStatusRequest;
import com.syamsandi.java_rs_rawat_jalan.model.status.StatusResponse;
import com.syamsandi.java_rs_rawat_jalan.model.status.UpdateStatusRequest;
import com.syamsandi.java_rs_rawat_jalan.repository.RoleRepository;
import com.syamsandi.java_rs_rawat_jalan.repository.StatusRepository;
import com.syamsandi.java_rs_rawat_jalan.repository.UserRepository;
import com.syamsandi.java_rs_rawat_jalan.repository.UserRoleRepository;
import com.syamsandi.java_rs_rawat_jalan.security.BCrypt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StatusControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private ObjectMapper objectMapper;

  private static final UUID USER_ID = UUID.randomUUID();
  private static final UUID ROLE_ID = UUID.randomUUID();
  private static final UUID USER_ROLE_ID = UUID.randomUUID();
  private static final UUID STATUS_ID = UUID.randomUUID();
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserRoleRepository userRoleRepository;
  @Autowired
  private StatusRepository statusRepository;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    roleRepository.deleteAll();
    userRoleRepository.deleteAll();
    statusRepository.deleteAll();


    User user = new User();
    user.setId(USER_ID);
    user.setEmail(".example@example.com");
    user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
    user.setToken("test");
    user.setTokenExpiredAt(System.currentTimeMillis() + 1000000000L);
    userRepository.save(user);

    Role role = new Role();
    role.setId(ROLE_ID);
    role.setName("admin");
    roleRepository.save(role);

    UserRole userRole = new UserRole();
    userRole.setId(USER_ROLE_ID);
    userRole.setUser(user);
    userRole.setRole(role);
    userRoleRepository.save(userRole);

    Status status = new Status();
    status.setId(STATUS_ID);
    status.setName("status");
    statusRepository.save(status);
  }

  @Test
  void createSuccess() throws Exception{
    CreateStatusRequest request = new CreateStatusRequest();
    request.setName("status1");

    mockMvc.perform(
        post("/api/status")
            .header("X-API-TOKEN","test")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<StatusResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertNotNull(response.getData().getStatusId());
      assertEquals(response.getData().getName(),request.getName());
    });
  }

  @Test
  void createFailedToken() throws Exception{
    CreateStatusRequest request = new CreateStatusRequest();
    request.setName("status1");

    mockMvc.perform(
        post("/api/status")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
        status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<StatusResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNotNull(response.getErrors());

    });
  }

  @Test
  void getSuccess() throws Exception{
    mockMvc.perform(
        get("/api/status/{statusId}",STATUS_ID.toString())
            .header("X-API-TOKEN","test")
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<StatusResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertNotNull(response.getData().getStatusId());
    });
  }

  @Test
  void getFailedToken() throws Exception{
    mockMvc.perform(
        get("/api/status/{statusId}",STATUS_ID.toString())
    ).andExpectAll(
        status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<RoleResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNotNull(response.getErrors());
    });
  }

  @Test
  void getAllSuccess() throws Exception{
    for (int i = 0; i < 10; i++) {
      Status status = new Status();
      status.setId(UUID.randomUUID());
      status.setName("status ke-" + i);
      statusRepository.save(status);
    }

    mockMvc.perform(
        get("/api/statuses")
            .header("X-API-TOKEN","test")
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<List<RoleResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertEquals(response.getData().size(),11);
    });
  }

  @Test
  void getAllFailedToken() throws Exception{
    for (int i = 0; i < 10; i++) {
      Status status = new Status();
      status.setId(UUID.randomUUID());
      status.setName("status ke-" + i);
      statusRepository.save(status);
    }

    mockMvc.perform(
        get("/api/statuses")
            .header("X-API-TOKEN","test")
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<List<RoleResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertEquals(response.getData().size(),11);
    });
  }

  @Test
  void updateSuccess() throws Exception{
    UpdateStatusRequest request = new UpdateStatusRequest();
    request.setName("status update");
    mockMvc.perform(
        put("/api/status/{statusId}",STATUS_ID.toString())
            .header("X-API-TOKEN","test")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<StatusResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertNotNull(response.getData().getStatusId());
      assertEquals(response.getData().getName(),request.getName());
    });
  }

  @Test
  void updateFailedToken() throws Exception{
    UpdateStatusRequest request = new UpdateStatusRequest();
    request.setName("status update");
    mockMvc.perform(
        put("/api/status/{statusId}",STATUS_ID.toString())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
        status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<StatusResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNotNull(response.getErrors());
    });
  }

  @Test
  void deleteSuccess() throws Exception{
    mockMvc.perform(
        delete("/api/status/{statusId}",STATUS_ID.toString())
            .header("X-API-TOKEN","test")
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertEquals(response.getData(),"OK");
    });
  }

  @Test
  void deleteFailedToken() throws Exception{
    mockMvc.perform(
        delete("/api/status/{statusId}",STATUS_ID.toString())
    ).andExpectAll(
        status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNotNull(response.getErrors());
    });

  }
}