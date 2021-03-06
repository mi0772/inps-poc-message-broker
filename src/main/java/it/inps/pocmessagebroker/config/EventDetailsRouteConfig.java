package it.inps.pocmessagebroker.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "routes.event-details")
public class EventDetailsRouteConfig {
    private String wsEndpoint;
}
