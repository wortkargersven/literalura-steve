package com.steveprojects.literalura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;


@Service
public class ConvertData implements IConvertData{
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T getData (String json, Class<T> clase) {
        try {
            return mapper.readValue(json.toString(), clase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error while trying to convert JSON: " +clase.getName(), e);
        }
    }

    //JSON Pretty Printing
    public String getPrettyPrinting (String json) {
        try {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.readTree(json));
    } catch (JsonProcessingException e) {
        throw new RuntimeException("Error formatting JSON", e);
    }

    }
}
