package com.lswebworld.hl7fixer.processors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.Version;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;

import java.io.IOException;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class Hl7ProcessorTest {

  @EndpointInject(uri = "direct:hl7")
  ProducerTemplate template;

  @EndpointInject(uri = "mock:hl7output")
  MockEndpoint output;

  @MockBean
  HapiContext ctx;

  private static final String RESPONSE_STRING = 
      "MSH|^~\\&|AccMgr||AccMgr||20181203185504||ACK|ADT^A01|T|2.3||\r"
      + "MSA|AA|ADT^A01\r";

  private static final String HL7_MESSAGE = "MSH|^~\\&|AccMgr|1|||20050110045504||ADT^A01|" 
      + "599102|P|2.3|||\r"
      + "EVN|A01|20050110045502|||||\r" 
      + "PID|1||10006579^^^1^MRN^1||DUCK^DONALD^D||19241010|M||1|"
      + "111 DUCK ST^^FOWL^CA^999990000^^M|1|8885551212|8885551212|1|2||"
      + "40007716^^^AccMgr^VN^1|123121234|||||||||||NO\r"
      + "NK1|1|DUCK^HUEY|SO|3583 DUCK RD^^FOWL^CA^999990000|8885552222\n||Y||||||||||||||\r"
      + "PV1|1|I|PREOP^101^1^1^^^S|3|||37^DISNEY^WALT^^^^^^AccMgr^^^^CI|||01||||1|||"
      + "37^DISNEY^WALT^^^^^^AccMgr^^^^CI|2|40007716^^^AccMgr^VN|4" 
      + "|||||||||||||||||||1||G|||20050110045253||||||\r"
      + "GT1|1|8291|DUCK^DONALD^D||111^DUCK ST^^FOWL^CA^999990000|"
      + "8885551212||19241010|M||1|123121234||||#Cartoon Ducks Inc|"
      + "111^DUCK ST^^FOWL^CA^999990000|8885551212||PT|\r"
      + "DG1|1|I9|71596^OSTEOARTHROS NOS-L/LEG ^I9|OSTEOARTHROS NOS-L/LEG ||A|\r"
      + "IN1|1|MEDICARE|3|MEDICARE|||||||Cartoon Ducks Inc|19891001|||4|DUCK^DONALD^D|1|"
      + "19241010|111^DUCK ST^^FOWL^CA^999990000|||||||||||||||||123121234A"
      + "||||||PT|M|111 DUCK ST^^FOWL^CA^999990000|||||8291\r" 
      + "IN2|1||123121234|Cartoon Ducks Inc|||123121234A||||||"
      + "|||||||||||||||||||||||||||||||||||||||||||||||||||8885551212\r"
      + "IN1|2|NON-PRIMARY|9|MEDICAL MUTUAL CALIF.|PO BOX 94776^^HOLLYWOOD^CA^441414776||"
      + "8003621279|PUBSUMB|||Cartoon Ducks Inc||||7|" 
      + "DUCK^DONALD^D|1|19241010|111 DUCK ST^^FOWL^CA^999990000|"
      + "||||||||||||||||056269770||||||PT|M|111^DUCK ST^^FOWL^CA^999990000|||||8291\r"
      + "IN2|2||123121234|Cartoon Ducks Inc||||||||||||||||"
      + "||||||||||||||||||||||||||||||||||||||||||||8885551212\r" 
      + "IN1|3|SELF PAY|1|SELF PAY|||||||||||5||1\r";

  /**
   * Setup Method.
 * @throws IOException IO Exception
 * @throws HL7Exception Hl7 Exception
 * @throws LLPException LLP Exception
   */
  @Before
  public void setup() throws HL7Exception, IOException, LLPException {
    HapiContext context = new DefaultHapiContext();
    Connection conn = mock(Connection.class);
    Initiator initiator = mock(Initiator.class);
    PipeParser parser = mock(PipeParser.class);
    PipeParser psr = context.getPipeParser();
    Message goodMsg = psr.parse(HL7_MESSAGE.replace("\n", ""));

    when(ctx.newClient(anyString(), anyInt(), anyBoolean())).thenReturn(conn);
    when(ctx.getPipeParser()).thenReturn(parser);
    when(conn.getInitiator()).thenReturn(initiator);
    when(parser.encode(any(Message.class))).thenReturn(RESPONSE_STRING);
    when(parser.parse(anyString())).thenReturn(goodMsg);

    
    Message msg = context.newMessage("ADT", "A01", Version.V23).generateACK();
    context.close();

    when(initiator.sendAndReceive(any(Message.class))).thenReturn(msg);
 
  }

  @Test
  @DirtiesContext
  public void testSendMessage() {

    template.sendBody(HL7_MESSAGE);
    Exchange msg = output.getExchanges().get(0);
    assertEquals(RESPONSE_STRING, msg.getIn().getBody().toString());
  }

  @Test
  @DirtiesContext
  public void testSendNullMessage() {
    template.sendBody(null);
    Exchange msg = output.getExchanges().get(0);
    assertNull(msg.getIn().getBody());
  }

}