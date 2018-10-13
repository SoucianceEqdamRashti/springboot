package com.souciance.boot.sendemail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Controller
public class EmailController {

    @Autowired
    private JavaMailSender sender;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //the uri path which the spring boot app will take requests from
    //also setting the content-typ for request and response
    @RequestMapping(value = "/mailnotify", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody String payload, @RequestHeader("Accept") String accept, @RequestHeader("Content-Type") String contentType) {
        boolean acceptJson = false;
        logger.info("SendEmail application received a request with Accept header" + accept + " and request content-type " + contentType);
        if (MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(accept)) {
            acceptJson = true;
            logger.info("Accept header indicates response content-type will be application/json");
        }
        else {
            acceptJson = false;
            logger.info("Accept header indicates response content-type will be application/xml");
        }
        try {
            EmailRequestMapper requestMapper = new EmailRequestMapper(payload, contentType);

            sendEmail(requestMapper);
            logger.info("Email sent to " + requestMapper.getEmailRecipient());
            logger.info("Client will receive http status code 200 back!");
            if (acceptJson)
                return new ResponseEntity<>(jsonResponse(true), HttpStatus.OK);
            else
                return new ResponseEntity<>(xmlResponse(true), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("An error occured parsing the request or sending the e-mail: " + ex);
            logger.error("Client will receive http status code 500 back!");
            if (acceptJson)
                return new ResponseEntity<>(jsonResponse(false), HttpStatus.INTERNAL_SERVER_ERROR);
            else
                return new ResponseEntity<>(xmlResponse(false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Map<String, Object> jsonResponse(boolean status) {
        Map<String, Object> response = new HashMap<String, Object>();
        if (status == true) {
            response.put("status", "success");
            response.put("message", "email notification sent!");
        } else {
            response.put("status", "failure");
            response.put("message", "email notification was not sent!");
        }
        return response;
    }

    private String xmlResponse(boolean status) {
        String failureResponse = "<emailNotification>\n" +
                "    <message>email notification was not sent!</message>\n" +
                "    <status>failure</status>\n" +
                "</emailNotification>";
        String successResponse = "<emailNotification>\n" +
                "    <message>email notification was sent!</message>\n" +
                "    <status>success</status>\n" +
                "</emailNotification>";

        if (status == true)
            return successResponse;
        else
            return failureResponse;
    }

    //send the e-email
    private void sendEmail(EmailRequestMapper requestMapper) throws Exception {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(requestMapper.getEmailRecipient());
        helper.setSubject(requestMapper.getEmailSubject());
        helper.setText(requestMapper.getEmailBody());
        sender.send(message);
    }
}
