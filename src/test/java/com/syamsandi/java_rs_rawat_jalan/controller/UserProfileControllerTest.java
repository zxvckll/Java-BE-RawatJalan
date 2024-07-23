package com.syamsandi.java_rs_rawat_jalan.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.entity.UserProfile;
import com.syamsandi.java_rs_rawat_jalan.model.CreateUserProfileRequest;
import com.syamsandi.java_rs_rawat_jalan.model.RegisterUserRequest;
import com.syamsandi.java_rs_rawat_jalan.model.WebResponse;
import com.syamsandi.java_rs_rawat_jalan.repository.UserProfileRepository;
import com.syamsandi.java_rs_rawat_jalan.repository.UserRepository;
import com.syamsandi.java_rs_rawat_jalan.security.BCrypt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
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

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    User user = new User();
    user.setId(UUID.randomUUID());
    user.setEmail("syam@gmail.com");
    user.setPassword(BCrypt.hashpw("rahasia",BCrypt.gensalt()));
    user.setToken("test");
    user.setTokenExpiredAt(System.currentTimeMillis() + 1000000000L);
    userRepository.save(user);

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
    CreateUserProfileRequest request = new CreateUserProfileRequest();
    request.setName("test");
    request.setName("sam");
    request.setImageUrl("example.com/img");
    request.setAddress("Sidoarjo Wage");


    mockMvc.perform(
        post("/api/users/current/profile")
            .header("X-API-TOKEN","test")
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
      assertEquals(request.getName(),userProfile.getName());
      assertEquals(request.getAddress(),userProfile.getAddress());
      assertEquals(request.getDateOfBirth(),userProfile.getDateOfBirth());
      assertEquals(request.getImageUrl(),userProfile.getImageUrl());
    });
  }
}