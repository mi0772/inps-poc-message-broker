package it.inps.pocmessagebroker.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "routes.event-discovery")
public class EventDiscoveryRouteConfig {
    private String wsEndpoint;
}
