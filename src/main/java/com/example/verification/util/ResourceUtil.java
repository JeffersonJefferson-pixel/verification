package com.example.verification.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@AllArgsConstructor
public class ResourceUtil {
  private final ResourceLoader resourceLoader;

  public String asString(String resourcePath) throws IOException {
    Resource resource = resourceLoader.getResource(resourcePath);
    try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
      return FileCopyUtils.copyToString(reader);
    }
  }

  public byte[] asByte(String resourcePath) throws IOException {
    Resource resource = resourceLoader.getResource(resourcePath);
    byte[] data = resource.getInputStream().readAllBytes();
    return data;
  }


}
