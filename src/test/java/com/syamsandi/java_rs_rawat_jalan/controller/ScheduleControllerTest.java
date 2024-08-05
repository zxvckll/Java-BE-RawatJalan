package com.syamsandi.java_rs_rawat_jalan.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syamsandi.java_rs_rawat_jalan.entity.*;
import com.syamsandi.java_rs_rawat_jalan.model.WebResponse;
import com.syamsandi.java_rs_rawat_jalan.model.doctor_profile.CreateDoctorProfileRequest;
import com.syamsandi.java_rs_rawat_jalan.model.doctor_profile.DoctorProfileResponse;
import com.syamsandi.java_rs_rawat_jalan.model.schedule.CreateScheduleRequest;
import com.syamsandi.java_rs_rawat_jalan.model.schedule.ScheduleResponse;
import com.syamsandi.java_rs_rawat_jalan.model.schedule.UpdateScheduleRequest;
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

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ScheduleControllerTest {

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

  private static final String DOCTOR_SLUG = "syamsandi_slug";

  private static final UUID SCHEDULE_ID = UUID.randomUUID();
  private static final String POLYCLINIC_SLUG = "polyclinic-1";
  private static final String CLINIC_SLUG = "clinic-1";
  @Autowired
  private UserProfileRepository userProfileRepository;
  @Autowired
  private PolyclinicRepository polyclinicRepository;
  @Autowired
  private ClinicRepository clinicRepository;
  @Autowired
  private ScheduleRepository scheduleRepository;


  @BeforeEach
  void setUp() {
    roleRepository.deleteAll();
    userRepository.deleteAll();
    userRoleRepository.deleteAll();
    polyclinicRepository.deleteAll();
    clinicRepository.deleteAll();
    scheduleRepository.deleteAll();

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

    DoctorProfile doctorProfile = new DoctorProfile();
    doctorProfile.setId(DOCTOR_ID);
    doctorProfile.setCourse("course");
    doctorProfile.setClinic(clinic);
    doctorProfile.setUser(user);
    doctorProfile.setSlug(DOCTOR_SLUG);
    doctorProfile.setEducation("education");
    doctorProfile.setExperience("experience");
    doctorProfile.setOrganization("organization");
    doctorProfileRepository.save(doctorProfile);


    Schedule schedule = new Schedule();
    schedule.setId(SCHEDULE_ID);
    schedule.setDay("monday");
    schedule.setTime(LocalTime.of(9,30));
    schedule.setDoctorProfile(doctorProfile);
    scheduleRepository.save(schedule);

  }


  @Test
  void create() throws Exception{
    CreateScheduleRequest request = new CreateScheduleRequest();
    request.setDay("monday");
    request.setTime(LocalTime.of(10,30));


    mockMvc.perform(
        post("/api/doctors/{doctorSlug}/{doctorId}/schedules",DOCTOR_SLUG,DOCTOR_ID.toString())
            .header("X-API-TOKEN","syamsandi")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<ScheduleResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });

      assertNotNull(response.getData().getDoctorProfileId());
      assertEquals(request.getDay(),response.getData().getDay());
      assertEquals(request.getTime(),response.getData().getTime());

    });
  }

  @Test
  void getSuccess() throws Exception{
    DoctorProfile doctorProfile = doctorProfileRepository.findFirstByIdAndSlug(DOCTOR_ID, DOCTOR_SLUG).orElse(null);

    Schedule schedule = new Schedule();
    schedule.setId(SCHEDULE_ID);
    schedule.setDay("monday");
    schedule.setTime(LocalTime.of(9,30));
    schedule.setDoctorProfile(doctorProfile);
    scheduleRepository.save(schedule);

    mockMvc.perform(
        get("/api/doctors/{doctorSlug}/{doctorId}/schedules/{scheduleId}"
            ,DOCTOR_SLUG,DOCTOR_ID.toString(),SCHEDULE_ID)
            .header("X-API-TOKEN","syamsandi")
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<ScheduleResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertNotNull(response.getData().getDoctorProfileId());
      assertEquals(schedule.getDay(),response.getData().getDay());
      assertEquals(schedule.getTime(),response.getData().getTime());
      assertEquals(schedule.getId(),response.getData().getScheduleId());
    });
  }

  @Test
  void updateSuccess() throws Exception{
    DoctorProfile doctorProfile = doctorProfileRepository.findFirstByIdAndSlug(DOCTOR_ID, DOCTOR_SLUG).orElse(null);

    Schedule schedule = new Schedule();
    schedule.setId(SCHEDULE_ID);
    schedule.setDay("monday");
    schedule.setTime(LocalTime.of(9,30));
    schedule.setDoctorProfile(doctorProfile);
    scheduleRepository.save(schedule);

    UpdateScheduleRequest request = new UpdateScheduleRequest();
    request.setDay("sunday");
    request.setTime(LocalTime.of(10,30));


    mockMvc.perform(
        put("/api/doctors/{doctorSlug}/{doctorId}/schedules/{scheduleId}"
            ,DOCTOR_SLUG,DOCTOR_ID.toString(),SCHEDULE_ID)
            .header("X-API-TOKEN","syamsandi")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<ScheduleResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });

      assertNotNull(response.getData().getDoctorProfileId());
      assertEquals(request.getDay(),response.getData().getDay());
      assertEquals(request.getTime(),response.getData().getTime());
      assertEquals(schedule.getId(),response.getData().getScheduleId());

    });

  }

  @Test
  void deleteSuccess() throws Exception{
    DoctorProfile doctorProfile = doctorProfileRepository.findFirstByIdAndSlug(DOCTOR_ID, DOCTOR_SLUG).orElse(null);

    Schedule schedule = new Schedule();
    schedule.setId(SCHEDULE_ID);
    schedule.setDay("monday");
    schedule.setTime(LocalTime.of(9,30));
    schedule.setDoctorProfile(doctorProfile);
    scheduleRepository.save(schedule);

    mockMvc.perform(
        delete("/api/doctors/{doctorSlug}/{doctorId}/schedules/{scheduleId}"
            ,DOCTOR_SLUG,DOCTOR_ID.toString(),SCHEDULE_ID)
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