package com.souciance.boot.sendemail;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "emailnotification")
public class EmailXmlRequestMapper {
    private String recipient;

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
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

    private String subject;
    private String body;
}
