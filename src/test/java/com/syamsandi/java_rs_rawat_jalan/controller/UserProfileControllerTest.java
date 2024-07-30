package com.syamsandi.java_rs_rawat_jalan.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syamsandi.java_rs_rawat_jalan.entity.Role;
import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.entity.UserProfile;
import com.syamsandi.java_rs_rawat_jalan.entity.UserRole;
import com.syamsandi.java_rs_rawat_jalan.model.*;
import com.syamsandi.java_rs_rawat_jalan.model.user_profile.UserProfileRequest;
import com.syamsandi.java_rs_rawat_jalan.model.user_profile.UserProfileResponse;
import com.syamsandi.java_rs_rawat_jalan.repository.RoleRepository;
import com.syamsandi.java_rs_rawat_jalan.repository.UserProfileRepository;
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


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserProfileControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserProfileRepository userProfileRepository;
  @Autowired
  private RoleRepository roleRepository;
  @Autowired
  private UserRoleRepository userRoleRepository;

  @BeforeEach
  void setUp() {
    userRoleRepository.deleteAll();
    roleRepository.deleteAll();
    userRepository.deleteAll();
    User user = new User();
    user.setId(UUID.randomUUID());
    user.setEmail("syam@gmail.com");
    user.setPassword(BCrypt.hashpw("rahasia",BCrypt.gensalt()));
    user.setToken("syamsandi");
    user.setTokenExpiredAt(System.currentTimeMillis() + 1000000000L);
    userRepository.save(user);

    Role role = new Role();
    role.setId(UUID.randomUUID());
    role.setName("admin");
    roleRepository.save(role);

    UserRole userRole = new UserRole();
    userRole.setId(UUID.randomUUID());
    userRole.setUser(user);
    userRole.setRole(role);
    userRoleRepository.save(userRole);

//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//    LocalDate dateOfBirth = LocalDate.parse("01-01-2001", formatter);
//
//    UserProfile userProfile = new UserProfile();
//    userProfile.setId(UUID.randomUUID());
//    userProfile.setUser(user);
//    userProfile.setName("sam");
//    userProfile.setImageUrl("example.com/img");
//    userProfile.setAddress("Sidoarjo Wage");
//
//    userProfile.setDateOfBirth(dateOfBirth);
//    userProfileRepository.save(userProfile);
  }

  @Test
  void createSuccess() throws Exception{
    UserProfileRequest request = new UserProfileRequest();
    request.setName("test");
    request.setName("sam");
    request.setImageUrl("example.com/img");
    request.setAddress("Sidoarjo Wage");
    request.setDateOfBirth("01-01-2001");


    mockMvc.perform(
        post("/api/users/current/profile")
            .header("X-API-TOKEN","syamsandi")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertEquals("OK", response.getData());
      User user = userRepository.findFirstByEmail("syam@gmail.com").orElse(null);
      UserProfile userProfile = userProfileRepository.findFirstByUser(user).orElse(null);
      assertNotNull(userProfile.getId());
      assertEquals(request.getName(),userProfile.getName());
      assertEquals(request.getAddress(),userProfile.getAddress());
      assertEquals(request.getDateOfBirth(),"01-01-2001");
      assertEquals(request.getImageUrl(),userProfile.getImageUrl());
    });
  }

  @Test
  void getSuccess() throws Exception{
    User user = userRepository.findFirstByEmail("syam@gmail.com").orElse(null);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    LocalDate dateOfBirth = LocalDate.parse("01-01-2001", formatter);

    UserProfile userProfile = new UserProfile();
    userProfile.setId(UUID.randomUUID());
    userProfile.setUser(user);
    userProfile.setName("sam");
    userProfile.setImageUrl("example.com/img");
    userProfile.setAddress("Sidoarjo Wage");
    userProfile.setDateOfBirth(dateOfBirth);
    userProfileRepository.save(userProfile);

    mockMvc.perform(
        get("/api/users/current/profile")
            .header("X-API-TOKEN","syamsandi")
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<UserProfileResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertNotNull(response.getData().getUserProfileId());
      assertEquals(response.getData().getName(),userProfile.getName());
      assertEquals(response.getData().getAddress(),userProfile.getAddress());
      assertEquals(response.getData().getDateOfBirth(),"01-01-2001");
      assertEquals(response.getData().getImageUrl(),userProfile.getImageUrl());
    });
  }

  @Test
  void updateSuccess() throws Exception{
    User user = userRepository.findFirstByEmail("syam@gmail.com").orElse(null);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    LocalDate dateOfBirth = LocalDate.parse("01-01-2001", formatter);

    UserProfile userProfile = new UserProfile();
    userProfile.setId(UUID.randomUUID());
    userProfile.setUser(user);
    userProfile.setName("sam");
    userProfile.setImageUrl("example.com/img");
    userProfile.setAddress("Sidoarjo Wage");
    userProfile.setDateOfBirth(dateOfBirth);
    userProfileRepository.save(userProfile);

    UserProfileRequest request = new UserProfileRequest();
    request.setName("test");
    request.setName("test");
    request.setImageUrl("example.com/img");
    request.setAddress("test test");
    request.setDateOfBirth("02-02-2002");

    mockMvc.perform(
        put("/api/users/current/profile")
            .header("X-API-TOKEN","syamsandi")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<UserProfileResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertEquals(request.getName(),response.getData().getName());
      assertEquals(request.getAddress(),response.getData().getAddress());
      assertEquals(request.getDateOfBirth(),response.getData().getDateOfBirth());
      assertEquals(request.getImageUrl(),response.getData().getImageUrl());
      assertNotNull(response.getData().getUserProfileId());
    });
  }

  @Test
  void search() throws Exception{
    for (int i = 0; i < 3; i++) {
      User user = new User();
      user.setId(UUID.randomUUID());
      user.setEmail(i+"syam@gmail.com");
      user.setPassword(BCrypt.hashpw("rahasia",BCrypt.gensalt()));
      user.setToken("test"+i);
      user.setTokenExpiredAt(System.currentTimeMillis() + 1000000000L);
      userRepository.save(user);

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
      LocalDate dateOfBirth = LocalDate.parse("01-01-2001", formatter);

      UserProfile userProfile = new UserProfile();
      userProfile.setId(UUID.randomUUID());
      userProfile.setUser(user);
      userProfile.setName("sam"+i);
      userProfile.setImageUrl("example.com/img");
      userProfile.setAddress("Sidoarjo Wage");
      userProfile.setDateOfBirth(dateOfBirth);
      userProfileRepository.save(userProfile);
    }
    for (int i = 3; i < 6; i++) {
      User user = new User();
      user.setId(UUID.randomUUID());
      user.setEmail(i+"syam@gmail.com");
      user.setPassword(BCrypt.hashpw("rahasia",BCrypt.gensalt()));
      user.setToken("test"+i);
      user.setTokenExpiredAt(System.currentTimeMillis() + 1000000000L);
      userRepository.save(user);

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
      LocalDate dateOfBirth = LocalDate.parse("10-10-2010", formatter);

      UserProfile userProfile = new UserProfile();
      userProfile.setId(UUID.randomUUID());
      userProfile.setUser(user);
      userProfile.setName("syam"+i);
      userProfile.setImageUrl("example.com/img");
      userProfile.setAddress("tangerang selatan");
      userProfile.setDateOfBirth(dateOfBirth);
      userProfileRepository.save(userProfile);


      mockMvc.perform(
          get("/api/users/current/profiles")
              .queryParam("address","tangerang")
              .accept(MediaType.APPLICATION_JSON)
              .contentType(MediaType.APPLICATION_JSON)
              .header("X-API-TOKEN", "syamsandi")
      ).andExpectAll(
          status().isOk()
      ).andDo(result -> {
        WebResponse<List<UserProfileResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertNull(response.getErrors());
        assertEquals(1, response.getPaging().getTotalPage());
        assertEquals(0, response.getPaging().getCurrentPage());
        assertEquals(10, response.getPaging().getSize());
      });
    }




  }
}