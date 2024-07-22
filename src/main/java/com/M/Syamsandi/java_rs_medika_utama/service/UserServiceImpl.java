package com.M.Syamsandi.java_rs_medika_utama.service;

import com.M.Syamsandi.java_rs_medika_utama.entity.User;
import com.M.Syamsandi.java_rs_medika_utama.entity.UserProfile;
import com.M.Syamsandi.java_rs_medika_utama.model.RegisterUserRequest;
import com.M.Syamsandi.java_rs_medika_utama.repository.UserProfileRepository;
import com.M.Syamsandi.java_rs_medika_utama.repository.UserRepository;
import com.M.Syamsandi.java_rs_medika_utama.security.BCrypt;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserProfileRepository userProfileRepository;

  @Autowired
  private ValidatorService validatorService;



  @Transactional
  @Override
  public void register(RegisterUserRequest request){
    validatorService.validate(request);
    if(userRepository.existsByEmail(request.getEmail())){
      throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email already registered");
    }

    User user = new User();
    user.setId(UUID.randomUUID());
    user.setEmail(request.getEmail());
    user.setPassword(BCrypt.hashpw(request.getPassword(),BCrypt.gensalt()));
    userRepository.save(user);

    UserProfile userProfile = new UserProfile();
    userProfile.setId(UUID.randomUUID());
    userProfile.setUser(user);
    userProfile.setName(request.getName());
    userProfileRepository.save(userProfile);

  }
}
