package com.syamsandi.java_rs_rawat_jalan.controller;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.entity.UserProfile;
import com.syamsandi.java_rs_rawat_jalan.model.RegisterUserRequest;
import com.syamsandi.java_rs_rawat_jalan.model.UpdateUserRequest;
import com.syamsandi.java_rs_rawat_jalan.model.UserResponse;
import com.syamsandi.java_rs_rawat_jalan.model.WebResponse;
import com.syamsandi.java_rs_rawat_jalan.repository.UserProfileRepository;
import com.syamsandi.java_rs_rawat_jalan.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syamsandi.java_rs_rawat_jalan.security.BCrypt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

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

    UserProfile userProfile = new UserProfile();
    userProfile.setId(UUID.randomUUID());
    userProfile.setUser(user);
    userProfile.setName("sam");
    userProfileRepository.save(userProfile);

  }

  @Test
  void registerSuccess() throws Exception{
    RegisterUserRequest request = new RegisterUserRequest();
    request.setName("test");
    request.setPassword("rahasia");
    request.setEmail("Test@example.com");

    mockMvc.perform(
        post("/api/users")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertEquals("OK", response.getData());
    });
  }

  @Test
  void getUser() throws Exception{
    mockMvc.perform(
        get("/api/users/current")
            .header("X-API-TOKEN","test")
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      User userDb = userRepository.findFirstByToken("test").orElse(null);
      UserProfile userProfileDb = userProfileRepository.findFirstByUser(userDb).orElse(null);
      assertEquals(userDb.getId(),response.getData().getId());
      assertEquals(userDb.getEmail(),response.getData().getEmail());
      assertEquals(userProfileDb.getName(),response.getData().getName());
    });
  }

  @Test
  void getUserFailedTokenNotSent() throws Exception{
    mockMvc.perform(
        get("/api/users/current")

    ).andExpectAll(
        status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNotNull(response.getErrors());
    });
  }

  @Test
  void updateSuccess() throws Exception{
    UpdateUserRequest request = new UpdateUserRequest();
    request.setPassword("rahasia123");
    request.setEmail("Test@example.com");

    mockMvc.perform(
        patch("/api/users/current")
            .header("X-API-TOKEN","test")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      User userDb = userRepository.findFirstByEmail("Test@example.com").orElse(null);
      UserProfile userProfileDb = userProfileRepository.findFirstByUser(userDb).orElse(null);
      assertEquals(request.getEmail(),response.getData().getEmail());
      assertEquals(userProfileDb.getName(),response.getData().getName());

    });
  }


}