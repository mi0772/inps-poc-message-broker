package it.inps.pocmessagebroker.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ws.arcaintra")
@Getter
@Setter
public class ArcaIntraWSConfig {
    private String appKey;
    private String appName;
    private String userId;
    private String identityProvider;
    private String profilo;
    private String tipoChiave;
}
