package com.feedsome.service.route.configuration;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@Configuration
@EnableConfigurationProperties({
        RabbitMQConnectionFactoryProperties.class,
        RedisConnectionFactoryProperties.class
})
public class RouteConnectionConfiguration {

    @Autowired
    private RabbitMQConnectionFactoryProperties rabbitMQConnectionFactoryProperties;

    @Autowired
    private RedisConnectionFactoryProperties redisConnectionFactoryProperties;

    @Bean
    public ConnectionFactory customConnectionFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(rabbitMQConnectionFactoryProperties.getHost());
        connectionFactory.setPort(rabbitMQConnectionFactoryProperties.getPort());
        connectionFactory.setUsername(rabbitMQConnectionFactoryProperties.getUsername());
        connectionFactory.setPassword(rabbitMQConnectionFactoryProperties.getPassword());

        return connectionFactory;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        final JedisConnectionFactory connectionFactory = new JedisConnectionFactory();

        connectionFactory.setHostName(redisConnectionFactoryProperties.getHostname());
        connectionFactory.setPort(redisConnectionFactoryProperties.getPort());

        connectionFactory.setPassword(redisConnectionFactoryProperties.getPassword());

        connectionFactory.setUsePool(true);

        return connectionFactory;
    }

}
