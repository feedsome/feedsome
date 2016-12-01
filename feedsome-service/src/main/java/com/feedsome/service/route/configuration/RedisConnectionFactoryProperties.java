package com.feedsome.service.route.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the Redis connection factory
 */
@Data
@ConfigurationProperties(prefix = "camel.connection.redis")
public class RedisConnectionFactoryProperties {

    private String hostname;

    private int port;

    private String password;

}
