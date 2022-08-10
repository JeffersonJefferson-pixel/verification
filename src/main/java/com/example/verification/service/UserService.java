package com.example.verification.service;

import javax.transaction.Transactional;

import com.example.verification.dto.UserDto;
import com.example.verification.exception.UserAlreadyExistException;
import com.example.verification.mapper.MapStructMapper;
import com.example.verification.model.User;
import com.example.verification.repository.UserRepository;
import com.example.verification.util.OtpUtil;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MapStructMapper mapper;

  public UserDto registerNewUser(final UserDto userDto) throws UserAlreadyExistException {
    LOGGER.debug("Registering user: {}", userDto);
    if (emailExists(userDto.getEmail())) {
      throw new UserAlreadyExistException("User already exists");
    }

    final User user = mapper.userDtoToUser(userDto);

    userRepository.save(user);

    return mapper.userToUserDto(user);
  }

  private boolean emailExists(final String email) {
    return userRepository.findByEmail(email) != null;
  }
}