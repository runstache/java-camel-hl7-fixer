package com.lswebworld.hl7fixer.configuration;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;

import com.lswebworld.hl7fixer.processors.Hl7Processor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Bean(name = "HapiContext")
  public HapiContext hapiContext() {
    return new DefaultHapiContext();
  }

  @Bean(name = "Hl7Processor")
  public Hl7Processor hl7Processor() {
    return new Hl7Processor();
  }
}