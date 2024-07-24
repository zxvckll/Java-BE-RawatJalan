package com.syamsandi.java_rs_rawat_jalan.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syamsandi.java_rs_rawat_jalan.entity.Role;
import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.entity.UserProfile;
import com.syamsandi.java_rs_rawat_jalan.model.RoleRequest;
import com.syamsandi.java_rs_rawat_jalan.model.RoleResponse;
import com.syamsandi.java_rs_rawat_jalan.model.WebResponse;
import com.syamsandi.java_rs_rawat_jalan.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RoleControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private ObjectMapper objectMapper;

  private static final UUID ID = UUID.randomUUID();

  @BeforeEach
  void setUp() {
    roleRepository.deleteAll();
    Role role = new Role();
    role.setId(ID);
    role.setName("admin");
    roleRepository.save(role);
  }

  @Test
  void createSuccess() throws Exception{
    RoleRequest request = new RoleRequest();
    request.setName("doctor");

    mockMvc.perform(
        post("/api/roles")
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
  void getSuccess() throws Exception{
    mockMvc.perform(
        get("/api/roles/{roleId}",ID.toString())
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
  void getAllSuccess() throws Exception{
    for (int i = 0; i < 10; i++) {
      Role role = new Role();
      role.setId(UUID.randomUUID());
      role.setName("role ke-" + i);
      roleRepository.save(role);
    }

    mockMvc.perform(
        get("/api/roles")
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
    RoleRequest request = new RoleRequest();
    request.setName("test");

    mockMvc.perform(
        put("/api/roles/{roleId}",ID.toString())
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
  void deleteSuccess() throws Exception{
    mockMvc.perform(
        delete("/api/roles/{roleId}",ID.toString())
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertEquals(response.getData(),"OK");
    });

  }
}