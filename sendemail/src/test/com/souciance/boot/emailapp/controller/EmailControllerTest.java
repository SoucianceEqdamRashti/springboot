package com.souciance.boot.emailapp.controller;

import com.souciance.boot.emailapp.controller.controller.EmailController;
import com.souciance.boot.emailapp.controller.model.EmailModel;
import com.souciance.boot.emailapp.controller.service.EmailService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class EmailControllerTest {

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private EmailController emailController;
    @Autowired
    private EmailService emailSender;
    private MockMvc mockMvc;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public  void setUp() throws Exception {
        emailSender = mock(EmailService.class);
        mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    //should result in a 415 response code
    public void testEmailControllerEmptyContentType() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders.post("/notifications/email")).andExpect(MockMvcResultMatchers.status().is(415));
    }

    @Test
    public void testEmailSubjectIsMissing() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/notifications/email")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"recipient\":\"souciance.rashti@alfalaval.com\",\n" +
                        "  \"subject\":\"\",\n" +
                        "  \"body\":\"MTIzMTIzMTIzMTMyMTMyMTM=\"\n" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    public void testEmailRecipientIsMissing() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/notifications/email")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"recipient\":\"\",\n" +
                        "  \"subject\":\"Test e-mail to Souciance\",\n" +
                        "  \"body\":\"MTIzMTIzMTIzMTMyMTMyMTM=\"\n" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testEmailBodyIsMissing() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/notifications/email")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"recipient\":\"souciance.rashti@alfalaval.com\",\n" +
                        "  \"subject\":\"Test e-mail to Souciance\",\n" +
                        "  \"body\":\"\"\n" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testEmailSend() throws Exception {
        EmailModel stubModel = new EmailModel();
        stubModel.setBody("random body");
        stubModel.setSubject("random subject");
        stubModel.setRecipients(new String[]{"test@test.com"});
        when(emailSender.sendEmail(stubModel)).thenReturn(true);
        assertEquals(emailSender.sendEmail(stubModel), true);
    }
}