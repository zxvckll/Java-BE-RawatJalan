package com.syamsandi.java_rs_rawat_jalan.controller;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.LoginUserRequest;
import com.syamsandi.java_rs_rawat_jalan.model.TokenResponse;
import com.syamsandi.java_rs_rawat_jalan.model.WebResponse;
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
    user.setToken("test");
    user.setTokenExpiredAt(System.currentTimeMillis() + 1000000000L);
    userRepository.save(user);
  }

  @Test
  void loginSuccess() throws Exception{
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
      User userdb = userRepository.findFirstByEmail(loginUserRequest.getEmail()).orElse(null);
      assertEquals(userdb.getEmail(),loginUserRequest.getEmail());
      assertTrue(BCrypt.checkpw(loginUserRequest.getPassword(),userdb.getPassword()));
      assertEquals(userdb.getToken(),response.getData().getToken());
      assertEquals(userdb.getTokenExpiredAt(),response.getData().getExpiredAt());

    });
  }

  @Test
  void loginFailedWrongPassword() throws Exception{
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
  @Test
  void logoutSuccess() throws Exception{
    mockMvc.perform(
        delete("/api/auth/logout")
            .header("X-API-TOKEN","test")

    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
    });
  }

  @Test
  void logoutFailedWrongToken() throws Exception{
    mockMvc.perform(
        delete("/api/auth/logout")
            .header("X-API-TOKEN","testa")
    ).andExpectAll(
        status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNotNull(response.getErrors());
    });
  }

  @Test
  void logoutFailedTokenExpired() throws Exception{
    User user = userRepository.findFirstByEmail("syam@gmail.com").orElse(null);
    user.setTokenExpiredAt(System.currentTimeMillis() - 100000L);
    userRepository.save(user);
    mockMvc.perform(
        delete("/api/auth/logout")
            .header("X-API-TOKEN","testa")
    ).andExpectAll(
        status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNotNull(response.getErrors());
    });
  }
}