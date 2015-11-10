package com.orientechnologies.spring.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Enrico Risa on 10/11/15.
 */
@Component
@ConfigurationProperties(prefix = "server")
public class ServerConfig {

  private int port;

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }
}
