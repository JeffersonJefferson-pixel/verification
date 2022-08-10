package com.example.verification;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.example.verification.model.Otp;
import com.example.verification.model.User;
import com.example.verification.security.jwt.JwtService;
import com.example.verification.util.OtpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = {VerificationApplication.class},
    webEnvironment  = WebEnvironment.RANDOM_PORT
)
@Transactional
public class AuthControllerIntegrationTest {
  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private OtpUtil otpUtil;

  @Autowired
  private JwtService jwtService;

  @PersistenceContext
  private EntityManager entityManager;

  private MockMvc mvc;
  private String otpToken;

  @BeforeEach
  public void setUp() {
    mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    User user = new User();
    user.setEmail("erenthewizard@gmail.com");
    entityManager.persist(user);

    otpToken = otpUtil.generateOtp();
    Otp otp = new Otp(otpToken, user, null);
    otp.setExpiryDate(Date.from(Instant.now().plus(2, ChronoUnit.DAYS)));
    entityManager.persist(otp);

    entityManager.flush();
    entityManager.clear();
  }

  @Test
  public void givenValidOtp_whenVerify_thenSucceeds() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> body = new HashMap<>();
    body.put("token", otpToken);
    RequestBuilder request = MockMvcRequestBuilders
        .post("/auth")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(body))
        .accept(MediaType.APPLICATION_JSON);
    MvcResult result = mvc.perform(request).andExpect(status().isOk()).andReturn();
    String str = result.getResponse().getContentAsString();
    Map<String, Object> map = new ObjectMapper().readValue(str, HashMap.class);
    assertTrue(jwtService.validateToken((String) map.get("id_token")));
  }
}
