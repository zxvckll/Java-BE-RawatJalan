package com.syamsandi.java_rs_rawat_jalan.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syamsandi.java_rs_rawat_jalan.entity.*;
import com.syamsandi.java_rs_rawat_jalan.model.*;
import com.syamsandi.java_rs_rawat_jalan.model.clinic.ClinicResponse;
import com.syamsandi.java_rs_rawat_jalan.model.clinic.CreateClinicRequest;
import com.syamsandi.java_rs_rawat_jalan.repository.*;
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
class ClinicControllerTest {

  private static final UUID USER_ID = UUID.randomUUID();
  private static final UUID ROLE_ID = UUID.randomUUID();
  private static final UUID USER_ROLE_ID = UUID.randomUUID();
  private static final UUID CLINIC_ID = UUID.randomUUID();
  private static final UUID POLYCLINIC_ID = UUID.randomUUID();
  private static final String POLYCLINIC_SLUG = "polyclinic-1";
  private static final String CLINIC_SLUG = "clinic-1";
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private RoleRepository roleRepository;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserRoleRepository userRoleRepository;
  @Autowired
  private ClinicRepository clinicRepository;
  @Autowired
  private PolyclinicRepository polyclinicRepository;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    roleRepository.deleteAll();
    userRoleRepository.deleteAll();
    clinicRepository.deleteAll();
    polyclinicRepository.deleteAll();


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
    polyclinic.setName("polyclinic 1");
    polyclinic.setSlug(POLYCLINIC_SLUG);
    polyclinicRepository.save(polyclinic);

    Clinic clinic = new Clinic();
    clinic.setId(CLINIC_ID);
    clinic.setPolyclinic(polyclinic);
    clinic.setSlug(CLINIC_SLUG);
    clinic.setName("clinic 1");
    clinicRepository.save(clinic);
  }

  @Test
  void createSuccess() throws Exception {
    CreateClinicRequest request = new CreateClinicRequest();
    request.setName("clinic 2");

    mockMvc.perform(
        post("/api/polyclinics/{polyclinicSlug}/{polyclinicId}/clinics", POLYCLINIC_SLUG, POLYCLINIC_ID.toString())
            .header("X-API-TOKEN", "test")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<ClinicResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertNotNull(response.getData().getClinicId());
      assertEquals(response.getData().getName(), request.getName());
    });
  }

  @Test
  void createFailedToken() throws Exception {
    CreateClinicRequest request = new CreateClinicRequest();
    request.setName("doctor");

    mockMvc.perform(
        post("/api/polyclinics/{polyclinicSlug}/{polyclinicId}/clinics", POLYCLINIC_SLUG, POLYCLINIC_ID.toString())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
        status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<ClinicResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNotNull(response.getErrors());
    });
  }

  @Test
  void getSuccess() throws Exception {
    mockMvc.perform(
        get("/api/polyclinics/{polyclinicSlug}/{polyclinicId}/clinics/{clinicSlug}/{clinicId}",
            POLYCLINIC_SLUG,
            POLYCLINIC_ID.toString(),
            CLINIC_SLUG,
            CLINIC_ID.toString())
            .header("X-API-TOKEN", "test")
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<ClinicResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertNotNull(response.getData().getClinicId());
    });
  }

  @Test
  void getFailedToken() throws Exception {
    mockMvc.perform(
        get("/api/polyclinics/{polyclinicSlug}/{polyclinicId}/clinics/{clinicSlug}/{clinicId}",
            POLYCLINIC_SLUG,
            POLYCLINIC_ID.toString(),
            CLINIC_SLUG,
            CLINIC_ID.toString())
    ).andExpectAll(
        status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<ClinicResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNotNull(response.getErrors());
    });
  }

  @Test
  void getAllSuccess() throws Exception {
    Polyclinic polyclinic = polyclinicRepository.findById(POLYCLINIC_ID).orElse(null);
    Clinic clinic = new Clinic();
    clinic.setId(UUID.randomUUID());
    clinic.setPolyclinic(polyclinic);
    clinic.setName("clinic 2");
    clinic.setSlug("clinic-2");
    clinicRepository.save(clinic);


    mockMvc.perform(
        get("/api/polyclinics/{polyclinicSlug}/{polyclinicId}/clinics",
            POLYCLINIC_SLUG,
            POLYCLINIC_ID.toString())
            .header("X-API-TOKEN", "test")
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<List<ClinicResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertEquals(response.getData().size(), 2);
    });
  }

  @Test
  void getAllFailedToken() throws Exception {
    Polyclinic polyclinic = polyclinicRepository.findById(POLYCLINIC_ID).orElse(null);
    Clinic clinic = new Clinic();
    clinic.setId(UUID.randomUUID());
    clinic.setPolyclinic(polyclinic);
    clinic.setName("clinic 2");
    clinic.setSlug("clinic-2");
    clinicRepository.save(clinic);
    mockMvc.perform(
        get("/api/polyclinics/{polyclinicSlug}/{polyclinicId}/clinics",
            POLYCLINIC_SLUG,
            POLYCLINIC_ID.toString())
    ).andExpectAll(
        status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<List<ClinicResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNotNull(response.getErrors());
    });
  }

  @Test
  void updateSuccess() throws Exception {
    CreateClinicRequest request = new CreateClinicRequest();
    request.setName("test");

    mockMvc.perform(
        put("/api/polyclinics/{polyclinicSlug}/{polyclinicId}/clinics/{clinicSlug}/{clinicId}",
            POLYCLINIC_SLUG,
            POLYCLINIC_ID.toString(),
            CLINIC_SLUG,
            CLINIC_ID.toString())
            .header("X-API-TOKEN", "test")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<ClinicResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertNotNull(response.getData().getClinicId());
      assertEquals(response.getData().getName(), request.getName());
    });

  }

  @Test
  void updateFailedToken() throws Exception {
    CreateClinicRequest request = new CreateClinicRequest();
    request.setName("test");

    mockMvc.perform(
        put("/api/polyclinics/{polyclinicSlug}/{polyclinicId}/clinics/{clinicSlug}/{clinicId}",
            POLYCLINIC_SLUG,
            POLYCLINIC_ID.toString(),
            CLINIC_SLUG,
            CLINIC_ID.toString())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
        status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<ClinicResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNotNull(response.getErrors());
    });

  }

  @Test
  void deleteSuccess() throws Exception {
    mockMvc.perform(
        delete("/api/polyclinics/{polyclinicSlug}/{polyclinicId}/clinics/{clinicSlug}/{clinicId}",
            POLYCLINIC_SLUG,
            POLYCLINIC_ID.toString(),
            CLINIC_SLUG,
            CLINIC_ID.toString())
            .header("X-API-TOKEN", "test")
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertEquals(response.getData(), "OK");
    });
  }

  @Test
  void deleteFailedToken() throws Exception {
    mockMvc.perform(
        delete("/api/polyclinics/{polyclinicSlug}/{polyclinicId}/clinics/{clinicSlug}/{clinicId}",
            POLYCLINIC_SLUG,
            POLYCLINIC_ID.toString(),
            CLINIC_SLUG,
            CLINIC_ID.toString())
    ).andExpectAll(
        status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNotNull(response.getErrors());
    });

  }
}