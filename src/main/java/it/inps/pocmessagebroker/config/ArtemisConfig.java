package it.inps.pocmessagebroker.config;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ArtemisConfig {

  @Value("${spring.artemis.host}")
  private String host;

  @Value("${spring.artemis.port}")
  private String port;

  @Value("${spring.artemis.user:#{null}}")
  private String user;

  @Value("${spring.artemis.password:#{null}}")
  private String password;

  @Bean
  public ConnectionFactory connectionFactory() {
    String msg = String.format("Connecting to ActiveMQ Artemis Broker at '%s:%s'", host, port);
    if (user != null) {
      msg += String.format(" - (%s)", user);
    }
    log.info(msg);
    ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://" + host + ":" + port, user, password);
    Connection conn = null;
    Session session = null;
    try {
      conn = factory.createConnection();
      session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
    } catch (JMSException e) {
      log.error(e.getMessage());
      throw new RuntimeException(e);
    } finally {
      try {
        session.close();
        conn.close();
      }
      catch (Exception e) {}
    }
    return factory;
  }
}
