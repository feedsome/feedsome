package com.feedsome.service.route;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "camel.endpoint.plugin")
public class EndpointProperties {

    private String pluginRegistrationUri;

    private String dataFeedUri;

    private String dataFeedProcessUri = "direct:feed:process";

    private String dataFeedSenderUri = "seda:feed:send";

    private String dataFeedPublishUriRegex = "redis://localhost:6379?command=PUBLISH&channels=${channels}";

}
