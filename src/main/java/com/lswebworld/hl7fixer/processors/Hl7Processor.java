package com.lswebworld.hl7fixer.processors;

import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;

import com.lswebworld.hl7fixer.configuration.AppSettings;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class Hl7Processor implements Processor {

  @Autowired
  HapiContext ctx;

  @Autowired
  AppSettings settings;

  private static final Logger LOGGER = LoggerFactory.getLogger(Hl7Processor.class);

  @Override
  public void process(Exchange exchange) throws Exception {
  

    if (exchange.getIn().getBody() != null) {
      final String messageBody = 
          exchange.getIn().getBody().toString()
              .replace("\n", "") //Replace errant newlines
              .replaceAll("[^\\p{ASCII}]", "") //Replace non-printable ASCII Characters
              .replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", ""); //Replace ASCII Control Characters
      final PipeParser parser = ctx.getPipeParser();
      final Message msg = parser.parse(messageBody);
      final Connection conn = ctx.newClient(settings.getHost(), settings.getPort(), false);
      final Initiator initiator = conn.getInitiator();
      final Message response = initiator.sendAndReceive(msg);
      final String responseString = parser.encode(response);
      LOGGER.info("Received Response: " + responseString);
      exchange.getIn().setBody(responseString);     

    }

  }

}