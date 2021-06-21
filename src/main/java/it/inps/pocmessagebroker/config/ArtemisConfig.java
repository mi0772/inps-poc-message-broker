package it.inps.pocmessagebroker.config;

/*
@Configuration
public class ArtemisConfig {

    @Value("${spring.artemis.user}")
    private String artemisUserName;

    @Value("${spring.artemis.password}")
    private String artemisPassword;
/*
    @Bean
    public ConnectionFactory connectionFactory(){
        return new ActiveMQConnectionFactory();
    }
* /
    @Bean
    AMQPConnectionDetails amqpConnection() {
        return new AMQPConnectionDetails("amqp://localhost:5672");
    }

    @Bean
    AMQPConnectionDetails securedAmqpConnection() {
        return new AMQPConnectionDetails("amqp://localhost:5672", artemisUserName, artemisPassword);
    }
}
*/

import org.springframework.context.annotation.Configuration;

@Configuration
public class ArtemisConfig {
/*
    @Value("${activemq.broker-url}")
    private String brokerUrl;

    @Bean
    public ActiveMQConnectionFactory senderActiveMQConnectionFactory() throws JMSException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(brokerUrl);

        return activeMQConnectionFactory;
    }

    @Bean
    public CachingConnectionFactory cachingConnectionFactory() throws JMSException {
        return new CachingConnectionFactory(senderActiveMQConnectionFactory());
    }

    @Bean
    public JmsTemplate jmsTemplate() throws JMSException {
        return new JmsTemplate(cachingConnectionFactory());
    }
*/
}
