package com.feedsome.service.route;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "camel.endpoint.plugin")
public class EndpointProperties {

    private String pluginRegistrationUri;

    private String dataFeedUir;

}
