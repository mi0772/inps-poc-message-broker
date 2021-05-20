package it.inps.pocmessagebroker.config;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.ConnectionFactory;

@Configuration
public class ArtemisConfig {
    @Bean
    public ConnectionFactory connectionFactory(){
        return new ActiveMQConnectionFactory();
    }
}
