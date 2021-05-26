package it.inps.pocmessagebroker.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ws.arcaintra")
@Getter
public class ArcaIntraWSConfig {
    private String appKey;
    private String appName;
    private String userId;
    private String identityProvider;

}
