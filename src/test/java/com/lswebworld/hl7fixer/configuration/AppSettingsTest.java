package com.lswebworld.hl7fixer.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class AppSettingsTest {

  private AppSettings settings;

  /**
   * Setup Method.
   */
  @Before
  public void setup() {
    settings = new AppSettings();
    settings.setHost("localhost");
    settings.setPort(6666);
    settings.setSourceDirectory("c:/docker/data");
  }

  @Test
  public void testGetHost() {
    assertEquals("localhost", settings.getHost());
  }

  @Test
  public void testGetPort() {
    assertEquals(6666, settings.getPort());
  }

  @Test
  public void testGetSourceDirectory() {
    assertEquals("c:/docker/data", settings.getSourceDirectory());
  }

  @Test
  public void testEquals() {
    AppSettings settings2 = new AppSettings();
    settings2.setHost("localhost");
    settings2.setPort(6666);
    settings2.setSourceDirectory("c:/docker/data");
    assertTrue(settings.equals(settings2));
  }

  @Test
  public void testNotEqualsHost() {
    AppSettings settings2 = new AppSettings();
    settings2.setHost("localhost2");
    settings2.setPort(6666);
    settings2.setSourceDirectory("c:/docker/data");
    assertFalse(settings.equals(settings2));
  }

  @Test
  public void testNotEqualsPost() {
    AppSettings settings2 = new AppSettings();
    settings2.setHost("localhost");
    settings2.setPort(6665);
    settings2.setSourceDirectory("c:/docker/data");
    assertFalse(settings.equals(settings2));
  }

  @Test
  public void testNotEqualsSourceDirectory() {
    AppSettings settings2 = new AppSettings();
    settings2.setHost("localhost");
    settings2.setPort(6666);
    settings2.setSourceDirectory("c:/docker/data/fhir");
    assertFalse(settings.equals(settings2));
  }

  @Test
  public void testNotEqualsWrongObject() {
    assertFalse(settings.equals(new Object()));
  }
}