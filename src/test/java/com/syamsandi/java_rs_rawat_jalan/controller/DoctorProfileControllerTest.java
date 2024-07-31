package com.syamsandi.java_rs_rawat_jalan.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syamsandi.java_rs_rawat_jalan.entity.*;
import com.syamsandi.java_rs_rawat_jalan.model.WebResponse;
import com.syamsandi.java_rs_rawat_jalan.model.doctor_profile.CreateDoctorProfileRequest;
import com.syamsandi.java_rs_rawat_jalan.model.doctor_profile.DoctorProfileResponse;
import com.syamsandi.java_rs_rawat_jalan.model.user_profile.UserProfileRequest;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DoctorProfileControllerTest {

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

  private static final UUID USER_ID = UUID.randomUUID();

  private static final UUID CLINIC_ID = UUID.randomUUID();
  private static final UUID POLYCLINIC_ID = UUID.randomUUID();

  private static final UUID DOCTOR_ID = UUID.randomUUID();
  private static final String POLYCLINIC_SLUG = "polyclinic-1";
  private static final String CLINIC_SLUG = "clinic-1";
  @Autowired
  private UserProfileRepository userProfileRepository;
  @Autowired
  private PolyclinicRepository polyclinicRepository;
  @Autowired
  private ClinicRepository clinicRepository;


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
    user.setPassword(BCrypt.hashpw("rahasia",BCrypt.gensalt()));
    user.setToken("syamsandi");
    user.setTokenExpiredAt(System.currentTimeMillis() + 1000000000L);
    userRepository.save(user);

    UserProfile userProfile = new UserProfile();
    userProfile.setId(UUID.randomUUID());
    userProfile.setName("Syamsandi");
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
  void create() throws Exception{
    CreateDoctorProfileRequest request = new CreateDoctorProfileRequest();
    request.setCourse("course");
    request.setUserId(USER_ID);
    request.setEducation("education");
    request.setExperience("experience");
    request.setOrganization("organization");


    mockMvc.perform(
        post("/api/clinics/{clinicSlug}/{clinicId}/doctor",CLINIC_SLUG,CLINIC_ID.toString())
            .header("X-API-TOKEN","syamsandi")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<DoctorProfileResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });

      assertNotNull(response.getData().getDoctorProfileId());
      assertEquals(request.getOrganization(),response.getData().getOrganization());
      assertEquals(request.getExperience(),response.getData().getExperience());
      assertEquals(request.getEducation(),response.getData().getEducation());
      assertEquals(request.getCourse(),response.getData().getCourse());
    });
  }

  @Test
  void getSuccess() throws Exception{
    User user = userRepository.findFirstByEmail("syam@gmail.com").orElse(null);
    Clinic clinic = clinicRepository.findById(CLINIC_ID).orElse(null);

    DoctorProfile doctorProfile = new DoctorProfile();
    doctorProfile.setId(UUID.randomUUID());
    doctorProfile.setCourse("course");
    doctorProfile.setClinic(clinic);
    doctorProfile.setUser(user);
    doctorProfile.setSlug("syamsandi");
    doctorProfile.setEducation("education");
    doctorProfile.setExperience("experience");
    doctorProfile.setOrganization("organization");

    doctorProfileRepository.save(doctorProfile);

    mockMvc.perform(
        get("/api/clinics/{clinicSlug}/{clinicId}/doctors/{doctorSlug}/{doctorId}",
            CLINIC_SLUG, CLINIC_ID.toString(),
            doctorProfile.getSlug(), doctorProfile.getId().toString())
            .header("X-API-TOKEN","syamsandi")
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<DoctorProfileResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertNotNull(response.getData().getDoctorProfileId());
      assertEquals(doctorProfile.getOrganization(),response.getData().getOrganization());
      assertEquals(doctorProfile.getExperience(),response.getData().getExperience());
      assertEquals(doctorProfile.getEducation(),response.getData().getEducation());
      assertEquals(doctorProfile.getCourse(),response.getData().getCourse());
    });
  }

  @Test
  void updateSuccess() throws Exception{
    User user = userRepository.findFirstByEmail("syam@gmail.com").orElse(null);
    Clinic clinic = clinicRepository.findById(CLINIC_ID).orElse(null);

    DoctorProfile doctorProfile = new DoctorProfile();
    doctorProfile.setId(UUID.randomUUID());
    doctorProfile.setCourse("course");
    doctorProfile.setClinic(clinic);
    doctorProfile.setUser(user);
    doctorProfile.setSlug("syamsandi");
    doctorProfile.setEducation("education");
    doctorProfile.setExperience("experience");
    doctorProfile.setOrganization("organization");

    doctorProfileRepository.save(doctorProfile);

    CreateDoctorProfileRequest request = new CreateDoctorProfileRequest();
    request.setCourse("course update");
    request.setUserId(USER_ID);
    request.setEducation("education update");
    request.setExperience("experience update");
    request.setOrganization("organization update");


    mockMvc.perform(
        put("/api/clinics/{clinicSlug}/{clinicId}/doctors/{doctorSlug}/{doctorId}",
            CLINIC_SLUG, CLINIC_ID.toString(),
            doctorProfile.getSlug(), doctorProfile.getId().toString())
            .header("X-API-TOKEN","syamsandi")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<DoctorProfileResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });

      assertNotNull(response.getData().getDoctorProfileId());
      assertEquals(request.getOrganization(),response.getData().getOrganization());
      assertEquals(request.getExperience(),response.getData().getExperience());
      assertEquals(request.getEducation(),response.getData().getEducation());
      assertEquals(request.getCourse(),response.getData().getCourse());
    });

  }

  @Test
  void deleteSuccess() throws Exception{
    User user = userRepository.findFirstByEmail("syam@gmail.com").orElse(null);
    Clinic clinic = clinicRepository.findById(CLINIC_ID).orElse(null);

    DoctorProfile doctorProfile = new DoctorProfile();
    doctorProfile.setId(UUID.randomUUID());
    doctorProfile.setCourse("course");
    doctorProfile.setClinic(clinic);
    doctorProfile.setUser(user);
    doctorProfile.setSlug("syamsandi");
    doctorProfile.setEducation("education");
    doctorProfile.setExperience("experience");
    doctorProfile.setOrganization("organization");

    doctorProfileRepository.save(doctorProfile);

    mockMvc.perform(
        delete("/api/clinics/{clinicSlug}/{clinicId}/doctors/{doctorSlug}/{doctorId}",
            CLINIC_SLUG, CLINIC_ID.toString(),
            doctorProfile.getSlug(), doctorProfile.getId().toString())
            .header("X-API-TOKEN","syamsandi")
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertEquals("OK",response.getData());
    });

  }

  @Test
  void searchSuccess() throws Exception{
    Clinic clinic = clinicRepository.findById(CLINIC_ID).orElse(null);
    for (int i = 0; i < 10; i++) {
      User user = new User();
      user.setId(UUID.randomUUID());
      user.setEmail(i+"syam@gmail.com");
      user.setPassword(BCrypt.hashpw("rahasia",BCrypt.gensalt()));
      user.setToken("test"+i);
      user.setTokenExpiredAt(System.currentTimeMillis() + 1000000000L);
      userRepository.save(user);

      DoctorProfile doctorProfile = new DoctorProfile();
      doctorProfile.setId(UUID.randomUUID());
      doctorProfile.setClinic(clinic);
      doctorProfile.setUser(user);
      doctorProfile.setSlug("Syamsandi"+i);
      doctorProfile.setCourse("Ahli Gigi "+i);
      doctorProfileRepository.save(doctorProfile);
    }



      mockMvc.perform(
          get("/api/clinics/{clinicSlug}/{clinicId}/doctors", CLINIC_SLUG, CLINIC_ID.toString())
              .queryParam("course", "gi")
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
        assertEquals(response.getData().size(),10);
      });


    }
}