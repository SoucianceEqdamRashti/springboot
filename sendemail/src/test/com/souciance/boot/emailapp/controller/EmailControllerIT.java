package com.souciance.boot.emailapp.controller;

import com.souciance.boot.emailapp.controller.controller.EmailController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class EmailControllerIT {
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private EmailController emailController;

    private MockMvc mockMvc;

    @Before
    public  void setUp() throws Exception {
        mockMvc= webAppContextSetup(this.wac).build();

    }
    @Test
    public void testEmailSend() throws Exception {
        String errorResponse = "{\"message\":\"An error occured send email to recpient. Please check the logs for further details!\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/notifications/email")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"recipient\":\"souciance.eqdam.rashti@gmail.com\",\n" +
                        "  \"subject\":\"Test e-mail to Souciance\",\n" +
                        "  \"body\":\"MTIzMTIzMTIzMTMyMTMyMTM=\"\n" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError())
                .andExpect(MockMvcResultMatchers.content().string(errorResponse));
    }
}
