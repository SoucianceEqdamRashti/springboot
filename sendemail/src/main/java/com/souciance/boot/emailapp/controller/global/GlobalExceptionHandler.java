package com.souciance.boot.emailapp.controller.global;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({org.springframework.mail.MailSendException.class, java.net.ConnectException.class})
    @ResponseBody
    public String handleThrowable(final Throwable ex) {
        log.error("Unexpected error", ex);
        String errorResponse = "{\"message\":\"An error occured whilst processing the request to send email. Please check the logs for further details!\"}";
        return  errorResponse;
    }


}

