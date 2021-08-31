package it.inps.pocmessagebroker.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class ReadResource {
  
  public String getResourceAsString(String resourceName) throws IOException {
    String out = null;
    Resource resource = new ClassPathResource(resourceName);
    InputStream resourceInputStream = resource.getInputStream();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceInputStream))) {
      out = reader.lines().collect(Collectors.joining("\n"));
    }
    return out;
  }

}
