package com.souciance.boot.emailapp;


import com.souciance.boot.emailapp.model.EmailModel;
import com.souciance.boot.emailapp.service.EmailService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class EmailServiceTest {
    @Autowired
    private EmailService service;
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void sendEmailWithUnParsableRecipient() throws Exception {
        EmailModel stubModel = new EmailModel();
        stubModel.setBody("random body");
        stubModel.setSubject("random subject");
        String[] badEmailAddress = {"bad email address"};
        stubModel.setRecipients(badEmailAddress);
        exception.expect(IllegalStateException.class);
        service.sendEmail(stubModel);
    }
}