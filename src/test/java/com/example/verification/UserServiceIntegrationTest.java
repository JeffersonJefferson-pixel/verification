package com.example.verification;

import com.example.verification.dto.UserDto;
import com.example.verification.exception.UserAlreadyExistException;
import com.example.verification.mapper.MapStructMapper;
import com.example.verification.model.User;
import com.example.verification.repository.UserRepository;
import com.example.verification.service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(
    properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5432/verification-test"
    }
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceIntegrationTest {

  private static final String TEST_EMAIL  = "test@test.com";
  private static final String TEST_NONEXISTENT_EMAIL  = "test_nonexistent@test.com";

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MapStructMapper mapper;

  private User testUser;
  private UserDto testUserDto;

  private User testNonExistentUser;
  private UserDto testNonExistentUserDto;

  @BeforeAll
  public void setup() {
    testUser = new User();
    testUser.setEmail(TEST_EMAIL);
    userRepository.save(testUser);
    testUserDto = mapper.userToUserDto(testUser);

    testNonExistentUser = new User();
    testNonExistentUser.setEmail(TEST_NONEXISTENT_EMAIL);
    testNonExistentUserDto = mapper.userToUserDto(testNonExistentUser);
  }

  @AfterAll
  public void teardown() {
    userRepository.deleteAll();
  }

  @Test
  public void givenUserWithNonConflictingEmail_whenRegister_thenSucceed() throws UserAlreadyExistException {
    UserDto userDto = userService.registerNewUser(testNonExistentUserDto);
    assertEquals(testNonExistentUserDto, userDto);
  }

  @Test
  public void givenUserWithConflictingEmail_whenRegister_thenFail() {
    try {
      userService.registerNewUser(testUserDto);
      fail();
    } catch (UserAlreadyExistException e) {
      assertTrue(true);
    }
  }
}
