package com.lswebworld.hl7fixer.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppSettings {

  private String host;
  private int port;
  private String sourceDirectory;

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getSourceDirectory() {
    return sourceDirectory;
  }

  public void setSourceDirectory(String sourceDirectory) {
    this.sourceDirectory = sourceDirectory;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof AppSettings) {
      AppSettings item = (AppSettings)obj;
      return this.host.equalsIgnoreCase(item.getHost())
          && this.port == item.getPort()
          && this.sourceDirectory.equalsIgnoreCase(item.getSourceDirectory());
    }
    return false;
  }


  

}