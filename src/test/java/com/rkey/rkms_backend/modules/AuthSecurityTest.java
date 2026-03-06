package com.rkey.rkms_backend.modules;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void registration_ShouldBePermitted() throws Exception {
        // We send an empty body just to see if we get a 400 (Bad Request) 
        // instead of a 403 (Forbidden). 400 means we passed Security!
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andDo(print()) // This prints the full request/response trace
                .andExpect(status().is4xxClientError()); 
    }
}