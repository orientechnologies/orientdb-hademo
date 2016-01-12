package com.orientechnologies.spring.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Enrico Risa on 10/11/15.
 */
@Component
@ConfigurationProperties(prefix = "daemon")
public class DaemonConfig {

  public int threads;

  public int getThreads() {
    return threads;
  }

  public void setThreads(int threads) {
    this.threads = threads;
  }
}
