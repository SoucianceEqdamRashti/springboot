package com.souciance.boot.emailapp.controller.controller;

import com.souciance.boot.emailapp.controller.model.EmailModel;
import com.souciance.boot.emailapp.controller.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class EmailController {

    @Autowired
    private EmailService email;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //the uri path which the spring boot app will take requests from
    @RequestMapping(value = "/notifications/email", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> process(@Valid @RequestBody EmailModel body) {
        logger.info("request received to send email to " + body.getRecipients());
        if(email.sendEmail(body))
            return new ResponseEntity<>(HttpStatus.OK);
        else
            throw new IllegalStateException(("An error occured when trying to send email!"));
    }
}
