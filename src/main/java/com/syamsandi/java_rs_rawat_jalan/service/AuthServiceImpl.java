package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.LoginUserRequest;
import com.syamsandi.java_rs_rawat_jalan.model.TokenResponse;
import com.syamsandi.java_rs_rawat_jalan.repository.UserRepository;
import com.syamsandi.java_rs_rawat_jalan.security.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
  @Autowired
  private ValidatorService validatorService;

  @Autowired
  private UserRepository userRepository;

  @Transactional
  @Override
  public TokenResponse login(LoginUserRequest request) {
    validatorService.validate(request);
    User user = userRepository.findFirstByEmail(request.getEmail()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password wrong"));

    if (BCrypt.checkpw(request.getPassword(), user.getPassword())){
      user.setToken(UUID.randomUUID().toString());
      user.setTokenExpiredAt(next30Days());
      userRepository.save(user);

      return TokenResponse.builder()
          .token(user.getToken())
          .expiredAt(user.getTokenExpiredAt())
          .build();
    }
    else {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Email or password wrong");
    }

  }

  @Transactional
  @Override
  public void logout(User user) {
    user.setToken(null);
    user.setTokenExpiredAt(null);
    userRepository.save(user);
  }

  private Long next30Days() {
    return System.currentTimeMillis() + (1000*60*60*24*30);
  }
}
