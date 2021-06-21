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
