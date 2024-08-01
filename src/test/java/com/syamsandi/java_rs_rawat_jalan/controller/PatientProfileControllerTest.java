package com.syamsandi.java_rs_rawat_jalan.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syamsandi.java_rs_rawat_jalan.entity.*;
import com.syamsandi.java_rs_rawat_jalan.model.WebResponse;
import com.syamsandi.java_rs_rawat_jalan.model.doctor_profile.CreateDoctorProfileRequest;
import com.syamsandi.java_rs_rawat_jalan.model.doctor_profile.DoctorProfileResponse;
import com.syamsandi.java_rs_rawat_jalan.model.patient_profile.CreatePatientProfileRequest;
import com.syamsandi.java_rs_rawat_jalan.model.patient_profile.PatientProfileResponse;
import com.syamsandi.java_rs_rawat_jalan.model.patient_profile.UpdatePatientProfileRequest;
import com.syamsandi.java_rs_rawat_jalan.model.user_profile.UserProfileResponse;
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
class PatientProfileControllerTest {

  private static final UUID USER_ID = UUID.randomUUID();
  private static final UUID PATIENT_ID = UUID.randomUUID();
  private static final String PATIENT_SLUG = "syamsandi";
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private DoctorProfileRepository doctorProfileRepository;
  @Autowired
  private RoleRepository roleRepository;
  @Autowired
  private UserRoleRepository userRoleRepository;
  @Autowired
  private UserProfileRepository userProfileRepository;
  @Autowired
  private PolyclinicRepository polyclinicRepository;
  @Autowired
  private ClinicRepository clinicRepository;
  @Autowired
  private PatientProfileRepository patientProfileRepository;


  @BeforeEach
  void setUp() {
    roleRepository.deleteAll();
    userRepository.deleteAll();
    userRoleRepository.deleteAll();
    polyclinicRepository.deleteAll();
    clinicRepository.deleteAll();

    User user = new User();
    user.setId(USER_ID);
    user.setEmail("syam@gmail.com");
    user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
    user.setToken("syamsandi");
    user.setTokenExpiredAt(System.currentTimeMillis() + 1000000000L);
    userRepository.save(user);

    UserProfile userProfile = new UserProfile();
    userProfile.setId(UUID.randomUUID());
    userProfile.setName("syamsandi");
    userProfile.setUser(user);
    userProfileRepository.save(userProfile);

    Role roleAdmin = new Role();
    roleAdmin.setId(UUID.randomUUID());
    roleAdmin.setName("admin");
    roleRepository.save(roleAdmin);

    Role roleDoctor = new Role();
    roleDoctor.setId(UUID.randomUUID());
    roleDoctor.setName("doctor");
    roleRepository.save(roleDoctor);

    UserRole userRoleAdmin = new UserRole();
    userRoleAdmin.setId(UUID.randomUUID());
    userRoleAdmin.setUser(user);
    userRoleAdmin.setRole(roleAdmin);
    userRoleRepository.save(userRoleAdmin);

    UserRole userRoleDoctor = new UserRole();
    userRoleDoctor.setId(UUID.randomUUID());
    userRoleDoctor.setUser(user);
    userRoleDoctor.setRole(roleDoctor);
    userRoleRepository.save(userRoleDoctor);

  }


  @Test
  void create() throws Exception {
    CreatePatientProfileRequest request = new CreatePatientProfileRequest();
    request.setNoRM("noRM");
    request.setUserId(USER_ID);
    request.setMedical_history("Medical_history");
    request.setCurrent_treatment("Current_treatment");


    mockMvc.perform(
        post("/api/users/patient")
            .header("X-API-TOKEN", "syamsandi")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<PatientProfileResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });

      assertNotNull(response.getData().getPatientProfileId());
      assertEquals(request.getCurrent_treatment(), response.getData().getCurrent_treatment());
      assertEquals(request.getMedical_history(), response.getData().getMedical_history());
      assertEquals(request.getNoRM(), response.getData().getNoRM());
    });
  }

  @Test
  void getSuccess() throws Exception {
    User user = userRepository.findFirstByEmail("syam@gmail.com").orElse(null);

    PatientProfile patientProfile = new PatientProfile();
    patientProfile.setId(PATIENT_ID);
    patientProfile.setNoRM("noRM");
    patientProfile.setSlug("syamsandi");
    patientProfile.setMedicalHistory("MedicalHistory");
    patientProfile.setCurrentTreatment("CurrentTreatment");
    patientProfile.setSlug(PATIENT_SLUG);
    patientProfile.setUser(user);
    patientProfileRepository.save(patientProfile);


    patientProfileRepository.save(patientProfile);

    mockMvc.perform(
        get("/api/users/patient/{patientSlug}/{patientId}",
            PATIENT_SLUG, PATIENT_ID.toString())
            .header("X-API-TOKEN", "syamsandi")
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<PatientProfileResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertEquals(patientProfile.getSlug(), response.getData().getPatientProfileSlug());
      assertEquals(patientProfile.getCurrentTreatment(), response.getData().getCurrent_treatment());
      assertEquals(patientProfile.getMedicalHistory(), response.getData().getMedical_history());
      assertEquals(patientProfile.getNoRM(), response.getData().getNoRM());
      assertEquals(patientProfile.getId(), response.getData().getPatientProfileId());
    });
  }

  @Test
  void updateSuccess() throws Exception {
    User user = userRepository.findFirstByEmail("syam@gmail.com").orElse(null);

    PatientProfile patientProfile = new PatientProfile();
    patientProfile.setId(PATIENT_ID);
    patientProfile.setNoRM("noRM");
    patientProfile.setSlug("syamsandi");
    patientProfile.setMedicalHistory("MedicalHistory");
    patientProfile.setCurrentTreatment("CurrentTreatment");
    patientProfile.setSlug(PATIENT_SLUG);
    patientProfile.setUser(user);
    patientProfileRepository.save(patientProfile);

    UpdatePatientProfileRequest request = new UpdatePatientProfileRequest();
    request.setPatientProfileSlug("syamsandi update");
    request.setCurrent_treatment("education update");
    request.setNoRM("experience update");
    request.setMedical_history("organization update");


    mockMvc.perform(
        put("/api/users/patient/{patientSlug}/{patientId}",
            PATIENT_SLUG, PATIENT_ID.toString())
            .header("X-API-TOKEN", "syamsandi")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<PatientProfileResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });

      assertNotNull(response.getData().getPatientProfileId());
      assertEquals(request.getNoRM(), response.getData().getNoRM());
      assertEquals(request.getMedical_history(), response.getData().getMedical_history());
      assertEquals(request.getCurrent_treatment(), response.getData().getCurrent_treatment());
    });

  }

  @Test
  void deleteSuccess() throws Exception {
    User user = userRepository.findFirstByEmail("syam@gmail.com").orElse(null);

    PatientProfile patientProfile = new PatientProfile();
    patientProfile.setId(PATIENT_ID);
    patientProfile.setNoRM("noRM");
    patientProfile.setSlug("syamsandi");
    patientProfile.setMedicalHistory("MedicalHistory");
    patientProfile.setCurrentTreatment("CurrentTreatment");
    patientProfile.setSlug(PATIENT_SLUG);
    patientProfile.setUser(user);
    patientProfileRepository.save(patientProfile);

    mockMvc.perform(
        delete("/api/users/patient/{patientSlug}/{patientId}",
            PATIENT_SLUG, PATIENT_ID.toString())
            .header("X-API-TOKEN", "syamsandi")
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertEquals("OK", response.getData());
    });

  }

  @Test
  void searchSuccess() throws Exception {
    for (int i = 0; i < 10; i++) {
      User user = new User();
      user.setId(UUID.randomUUID());
      user.setEmail(i + "syam@gmail.com");
      user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
      user.setToken("test" + i);
      user.setTokenExpiredAt(System.currentTimeMillis() + 1000000000L);
      userRepository.save(user);

      PatientProfile patientProfile = new PatientProfile();
      patientProfile.setId(PATIENT_ID);
      patientProfile.setNoRM("noRM" + i);
      patientProfile.setSlug("syamsandi" + i);
      patientProfile.setMedicalHistory("MedicalHistory" + i);
      patientProfile.setCurrentTreatment("CurrentTreatment" + i);
      patientProfile.setSlug(PATIENT_SLUG);
      patientProfile.setUser(user);
      patientProfileRepository.save(patientProfile);
    }


    mockMvc.perform(
        get("/api/users/patients")
            .queryParam("noRM", "no")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-API-TOKEN", "syamsandi")
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<List<PatientProfileResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertEquals(0, response.getPaging().getCurrentPage());
      assertEquals(response.getPaging().getTotalPage(), 1);
      assertEquals(response.getPaging().getSize(),10);
    });


  }
}