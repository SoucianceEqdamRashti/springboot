package com.souciance.boot.emailapp.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class EmailModel {
    @NotNull(message = "recipients is a required field")
    @Size(min = 1)
    private String[] recipients;

    private String[] copies;

    @NotNull(message = "subject is a required field")
    @Size(min = 1)
    private String subject;

    @NotNull(message = "body is a required field")
    @Size(min = 1)
    private String body;

    private String errors;


    public String[] getRecipients() {
        return recipients;
    }

    public void setRecipients(String[] recipients) {
        this.recipients = recipients;
    }

    public String[] getCopies() {
        return copies;
    }

    public void setCopies(String[] copies) {
        this.copies = copies;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

}
