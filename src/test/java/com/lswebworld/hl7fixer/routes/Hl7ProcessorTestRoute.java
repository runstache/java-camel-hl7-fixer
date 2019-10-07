package com.lswebworld.hl7fixer.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class Hl7ProcessorTestRoute extends RouteBuilder {

  @Override
  public void configure() throws Exception {
    from("direct:hl7")
      .routeId("hl7-test")
      .to("bean:Hl7Processor")
      .to("mock:hl7output")
      .end();

  }
  
}