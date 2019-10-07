package com.lswebworld.hl7fixer.routes;

import com.lswebworld.hl7fixer.configuration.AppSettings;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Hl7Route extends RouteBuilder {

  @Autowired
  AppSettings settings;

  @Override
  public void configure() throws Exception {
    
    from("file://" + settings.getSourceDirectory()
        + "?delete=false"
        + "&preMove=staging"
        + "&move=completed")
      .routeId("hl7-route")
      .convertBodyTo(String.class)
      .to("bean:Hl7Processor")
      .end();

  }


}