package com.souciance.boot.emailapp.controller.service;

import com.souciance.boot.emailapp.controller.model.EmailModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender sender;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private MimeMessage message;
    private MimeMessageHelper mimeMessage;
    private String[] validCopiesAddresses;
    private String[] validRecipientAddresseses;

    public boolean sendEmail(EmailModel parameters) {
        try {
            message = sender.createMimeMessage();
            mimeMessage = new MimeMessageHelper(message);
            if (validAddressesFound(parameters.getRecipients(), false))
                mimeMessage.setTo(validRecipientAddresseses);
            else {
                logger.error("Error sending e-mail to " + parameters.getRecipients());
                throw new IllegalStateException("Could not send email, unable to parse any of recipients addresses!");
            }
            if (parameters.getCopies().length > 0) {
                if (validAddressesFound(parameters.getCopies(), true))
                    mimeMessage.setCc(validCopiesAddresses);
            }
            mimeMessage.setText(setBody(parameters.getBody(), parameters.getErrors()));
            mimeMessage.setSubject(parameters.getSubject());
            logger.info("Sending e-mail...");
            sender.send(message);
            logger.info("Email successfully sent to: " + Arrays.toString(validRecipientAddresseses) + " with copies to: " + Arrays.toString(validCopiesAddresses));
            return true;
        } catch (MessagingException e) {
            logger.error("Error sending e-mail to: " + Arrays.toString(validRecipientAddresseses) + " with copies to: " + Arrays.toString(validCopiesAddresses));
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String formattedStacktrace = sw.toString();
            logger.error(formattedStacktrace);
            throw new IllegalStateException("Error creating mime message to send the email to: " + Arrays.toString(validRecipientAddresseses) + " with copies to: " + Arrays.toString(validCopiesAddresses) + formattedStacktrace);
        }
    }

    private boolean validAddressesFound(String[] addresses, boolean copies) {
        String[] validAddresses = emailAddressesValidator(addresses);
        if (validAddresses.length > 0) {
            if (copies)
                this.validCopiesAddresses = validAddresses;
            else
                this.validRecipientAddresseses = validAddresses;
            return true;
        } else
            return false;
    }

    private String setBody(String body, String errors) {
        return formatEmailBody(body, errors);
    }

    private String formatEmailBody(String body, String errors) {
        StringBuffer emailBody = new StringBuffer();
        emailBody.append("Main body:")
                .append("\r\n")
                .append(body)
                .append("\r\n")
                .append("\r\n");
        if ((null == errors) || errors.isEmpty())
            return emailBody.toString();
        else {
            emailBody.append("Errors:")
                    .append("\r\n")
                    .append(errors);
            return emailBody.toString();
        }

    }

    private String[] emailAddressesValidator(String[] addresses) {
        List<String> validAddresses = new ArrayList<>();
        for (String email : addresses) {
            boolean isValid = emailAddressValidator(email);
            if (isValid)
                validAddresses.add(email);
        }
        String[] finalValidAddresses = validAddresses.toArray(new String[0]);
        return finalValidAddresses;
    }

    private boolean emailAddressValidator(String email) {
        boolean isValid = false;
        try {
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
            isValid = true;
        } catch (AddressException e) {
            logger.error("Invalid e-mail address found:: " + email + ". Could not parse email address!");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String formattedStacktrace = sw.toString();
            logger.error(formattedStacktrace);
        }
        return isValid;
    }
}
