package com.feedsome.service.route.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the rabbit mq connection factory
 */
@Data
@ConfigurationProperties(prefix = "camel.connection.rabbitmq")
public class RabbitMQConnectionFactoryProperties {

    private String host;

    private int port;

    private String username;

    private String password;

}
