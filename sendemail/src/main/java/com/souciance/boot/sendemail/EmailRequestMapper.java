package com.souciance.boot.sendemail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class EmailRequestMapper {
    private Map<?,?> empMap = new HashMap<>();
    private boolean isRequestBodyJson = true;
    private EmailXmlRequestMapper xmlRequestMapper;

    public EmailRequestMapper(String request, String contentType) throws IOException {
        if (contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
            isRequestBodyJson = true;
            ObjectMapper objectMapper = new ObjectMapper();
            empMap = objectMapper.readValue(request, Map.class);
        }
        else {
            isRequestBodyJson = false;
            XmlMapper xmlMapper = new XmlMapper();
            xmlRequestMapper  = xmlMapper.readValue(request,EmailXmlRequestMapper.class);

        }
    }

    public String getEmailSubject() {
        if (isRequestBodyJson)
            return empMap.get("subject").toString();
        else
            return xmlRequestMapper.getSubject();
    }

    public String getEmailRecipient() {
        if (isRequestBodyJson)
        return empMap.get("recipient").toString();
        else
            return xmlRequestMapper.getRecipient();
    }

    public String getEmailBody() {
        byte[] decodedBytes;
        if (isRequestBodyJson)
            decodedBytes   = Base64.getDecoder().decode(empMap.get("body").toString());
        else
            decodedBytes = Base64.getDecoder().decode(xmlRequestMapper.getBody());

        return new String(decodedBytes);
    }
}