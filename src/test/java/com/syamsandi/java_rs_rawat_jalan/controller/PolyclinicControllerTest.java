package com.syamsandi.java_rs_rawat_jalan.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syamsandi.java_rs_rawat_jalan.entity.Polyclinic;
import com.syamsandi.java_rs_rawat_jalan.entity.Role;
import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.entity.UserRole;
import com.syamsandi.java_rs_rawat_jalan.model.PolyclinicRequest;
import com.syamsandi.java_rs_rawat_jalan.model.PolyclinicRequest;
import com.syamsandi.java_rs_rawat_jalan.model.RoleResponse;
import com.syamsandi.java_rs_rawat_jalan.model.WebResponse;
import com.syamsandi.java_rs_rawat_jalan.repository.PolyclinicRepository;
import com.syamsandi.java_rs_rawat_jalan.repository.RoleRepository;
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
class PolyclinicControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private PolyclinicRepository polyclinicRepository;

  @Autowired
  private ObjectMapper objectMapper;

  private static final UUID USER_ID = UUID.randomUUID();
  private static final UUID ROLE_ID = UUID.randomUUID();
  private static final UUID USER_ROLE_ID = UUID.randomUUID();

  private static final UUID POLYCLINIC_ID = UUID.randomUUID();
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserRoleRepository userRoleRepository;
  
  @Autowired
  private RoleRepository roleRepository;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    roleRepository.deleteAll();
    polyclinicRepository.deleteAll();
    userRoleRepository.deleteAll();


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

    Polyclinic polyclinic = new Polyclinic();
    polyclinic.setId(POLYCLINIC_ID);
    polyclinic.setName("Gigi");
    polyclinicRepository.save(polyclinic);
  }

  @Test
  void createSuccess() throws Exception{
    PolyclinicRequest request = new PolyclinicRequest();
    request.setName("doctor");

    mockMvc.perform(
        post("/api/polyclinics")
            .header("X-API-TOKEN","test")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<RoleResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertNotNull(response.getData().getId());
      assertEquals(response.getData().getName(),request.getName());
    });
  }

  @Test
  void createFailedToken() throws Exception{
    PolyclinicRequest request = new PolyclinicRequest();
    request.setName("doctor");

    mockMvc.perform(
        post("/api/polyclinics")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
        status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<RoleResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNotNull(response.getErrors());
    });
  }

  @Test
  void getSuccess() throws Exception{
    mockMvc.perform(
        get("/api/polyclinics/{polyclinicId}",POLYCLINIC_ID.toString())
            .header("X-API-TOKEN","test")
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<RoleResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertNotNull(response.getData().getId());
    });
  }

  @Test
  void getFailedToken() throws Exception{
    mockMvc.perform(
        get("/api/polyclinics/{polyclinicId}",POLYCLINIC_ID.toString())
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
      Polyclinic polyclinic = new Polyclinic();
      polyclinic.setId(UUID.randomUUID());
      polyclinic.setName("polyclinic ke-" + i);
      polyclinicRepository.save(polyclinic);
    }

    mockMvc.perform(
        get("/api/polyclinics")
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
      Polyclinic polyclinic = new Polyclinic();
      polyclinic.setId(UUID.randomUUID());
      polyclinic.setName("role ke-" + i);
      polyclinicRepository.save(polyclinic);
    }

    mockMvc.perform(
        get("/api/polyclinics")
    ).andExpectAll(
        status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<List<RoleResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNotNull(response.getErrors());
    });
  }

  @Test
  void updateSuccess() throws Exception{
    PolyclinicRequest request = new PolyclinicRequest();
    request.setName("test");

    mockMvc.perform(
        put("/api/polyclinics/{polyclinicId}",POLYCLINIC_ID.toString())
            .header("X-API-TOKEN","test")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<RoleResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertNotNull(response.getData().getId());
      assertEquals(response.getData().getName(),request.getName());
    });

  }

  @Test
  void updateFailedToken() throws Exception{
    PolyclinicRequest request = new PolyclinicRequest();
    request.setName("test");

    mockMvc.perform(
        put("/api/polyclinics/{polyclinicId}",POLYCLINIC_ID.toString())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
        status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<RoleResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNotNull(response.getErrors());
    });

  }

  @Test
  void deleteSuccess() throws Exception{
    mockMvc.perform(
        delete("/api/polyclinics/{polyclinicId}",POLYCLINIC_ID.toString())
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
        delete("/api/polyclinics/{polyclinicId}",POLYCLINIC_ID.toString())
    ).andExpectAll(
        status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNotNull(response.getErrors());
    });

  }
}