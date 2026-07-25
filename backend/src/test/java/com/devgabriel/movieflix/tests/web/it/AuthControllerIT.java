package com.devgabriel.movieflix.tests.web.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void loginShouldReturnTokenWhenValidCredentials() throws Exception {
    String json = "{\"email\":\"bob@gmail.com\",\"password\":\"123456\"}";

    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.access_token").exists())
        .andExpect(jsonPath("$.token_type").value("Bearer"))
        .andExpect(jsonPath("$.expires_in").value(86400));
  }

  @Test
  public void loginShouldReturnUnauthorizedWhenInvalidCredentials() throws Exception {
    String json = "{\"email\":\"bob@gmail.com\",\"password\":\"wrongpass\"}";

    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void loginShouldReturnUnprocessableEntityWhenInvalidDTOFields() throws Exception {
    String json = "{\"email\":\"\",\"password\":\"\"}";

    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isUnprocessableEntity());
  }

  @Test
  public void loginShouldReturnBadRequestWhenMalformedJson() throws Exception {
    String json = "invalid-json";

    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest());
  }
}
