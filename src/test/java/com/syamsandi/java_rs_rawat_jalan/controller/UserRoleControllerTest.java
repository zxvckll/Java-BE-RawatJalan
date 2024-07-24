package com.syamsandi.java_rs_rawat_jalan.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syamsandi.java_rs_rawat_jalan.entity.Role;
import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.entity.UserRole;
import com.syamsandi.java_rs_rawat_jalan.model.*;
import com.syamsandi.java_rs_rawat_jalan.repository.RoleRepository;
import com.syamsandi.java_rs_rawat_jalan.repository.UserRepository;
import com.syamsandi.java_rs_rawat_jalan.repository.UserRoleRepository;
import com.syamsandi.java_rs_rawat_jalan.security.BCrypt;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserRoleControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRoleRepository userRoleRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;
  @Autowired
  private ObjectMapper objectMapper;

  private static final UUID USER_ID = UUID.randomUUID();
  private static final UUID ROLE_ID = UUID.randomUUID();
  private static final UUID USER_ROLE_ID = UUID.randomUUID();

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    roleRepository.deleteAll();
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



  }

  @Test
  void createSuccess() throws Exception {
    Role role = new Role();
    role.setId(UUID.randomUUID());
    role.setName("patient");
    roleRepository.save(role);

    UserRoleRequest request = new UserRoleRequest();
    request.setUser_id(USER_ID);
    request.setRole_id(role.getId());



    mockMvc.perform(
        post("/api/users/roles")
            .header("X-API-TOKEN","test")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<UserRoleResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertNotNull(response.getData().getId());
    });
  }

  @Test
  void getSuccess() throws Exception {
    mockMvc.perform(
        get("/api/users/roles/{userRoleId}",USER_ROLE_ID)
            .header("X-API-TOKEN","test")
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<UserRoleResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertNotNull(response.getData().getId());
    });
  }

  @Test
  void getAllSuccess() throws Exception {
    User user = userRepository.findById(USER_ID).orElse(null);
    Role role = new Role();
    role.setId(UUID.randomUUID());
    role.setName("doctor");
    roleRepository.save(role);


    UserRole userRole = new UserRole();
    userRole.setId(UUID.randomUUID());
    userRole.setUser(user);
    userRole.setRole(role);
    userRoleRepository.save(userRole);


    mockMvc.perform(
        get("/api/users/roles")
            .header("X-API-TOKEN","test")
            .queryParam("user_id",USER_ID.toString())

    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<List<UserRoleResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertEquals(response.getData().size(), 2);
    });
  }

  @Test
  void updateSuccess() throws Exception {
    UserRoleRequest request = new UserRoleRequest();
    request.setUser_id(USER_ID);
    request.setRole_id(ROLE_ID);

    mockMvc.perform(
        put("/api/users/roles/{userRoleId}",USER_ROLE_ID)
            .header("X-API-TOKEN","test")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<UserRoleResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertNotNull(response.getData().getId());
    });

  }

  @Test
  void deleteSuccess() throws Exception {
    mockMvc.perform(
        delete("/api/users/roles/{userRoleId}",USER_ROLE_ID)
            .header("X-API-TOKEN","test")
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertEquals(response.getData(), "OK");
    });

  }
}