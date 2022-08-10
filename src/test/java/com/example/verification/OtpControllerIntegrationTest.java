package com.example.verification;

import java.util.HashMap;
import java.util.Map;

import com.example.verification.model.User;
import com.example.verification.repository.OtpRepository;
import com.example.verification.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
//@ContextConfiguration(classes = {WebConfig.class})
//@WebMvcTest(OtpController.class)
@SpringBootTest(
    properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5432/verification-test"
    }
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class OtpControllerIntegrationTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private OtpRepository otpRepository;

  @BeforeAll
  public void setup() {
    User testUser = new User();
    testUser.setEmail("erenthewizard@gmail.com");
    userRepository.save(testUser);
  }

  @AfterAll
  public void teardown() {
    otpRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  public void sendOtpTest() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> body = new HashMap<>();
    body.put("email", "erenthewizard@gmail.com");
    RequestBuilder request = MockMvcRequestBuilders.post("/otp")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(body))
        .accept(MediaType.APPLICATION_JSON);
    mvc.perform(request).andExpect(status().isOk());
  }

}
