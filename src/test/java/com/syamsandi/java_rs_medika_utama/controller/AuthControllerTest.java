package com.syamsandi.java_rs_medika_utama.controller;

import com.syamsandi.java_rs_medika_utama.entity.User;
import com.syamsandi.java_rs_medika_utama.model.LoginUserRequest;
import com.syamsandi.java_rs_medika_utama.model.TokenResponse;
import com.syamsandi.java_rs_medika_utama.model.WebResponse;
import com.syamsandi.java_rs_medika_utama.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syamsandi.java_rs_medika_utama.security.BCrypt;
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
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;
  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    User user = new User();
    user.setId(UUID.randomUUID());
    user.setEmail("syam@gmail.com");
    user.setPassword(BCrypt.hashpw("rahasia",BCrypt.gensalt()));
    userRepository.save(user);
  }

  @Test
  void LoginSuccess() throws Exception{
    LoginUserRequest loginUserRequest = new LoginUserRequest();
    loginUserRequest.setEmail("syam@gmail.com");
    loginUserRequest.setPassword("rahasia");

    mockMvc.perform(
        post("/api/auth/login")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginUserRequest))
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
    });
  }

  @Test
  void LoginFailedWrongPassword() throws Exception{
    LoginUserRequest loginUserRequest = new LoginUserRequest();
    loginUserRequest.setEmail("syam@gmail.com");
    loginUserRequest.setPassword("rahasiaa");

    mockMvc.perform(
        post("/api/auth/login")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginUserRequest))
    ).andExpectAll(
        status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNotNull(response.getErrors());
    });
  }
}