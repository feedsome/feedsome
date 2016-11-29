package com.feedsome.service;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.sync.RedisCommands;
import com.lambdaworks.redis.pubsub.RedisPubSubAdapter;
import com.lambdaworks.redis.pubsub.api.sync.RedisPubSubCommands;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Validated
@Configuration
public class RedisConfiguration {


    @Bean //"redis://localhost"
    public RedisClient redisClient(@Value("${redis.connection.string}") @NotEmpty final String connectionString) {
        return RedisClient.create(connectionString);
    }

    @Bean
    public RedisCommands<String, String> connect(RedisClient client) {
        RedisPubSubCommands<String, String> connection = client.connectPubSub().sync();

        //TODO: replace with an actual listener implementation
        connection.addListener(new RedisPubSubAdapter<>());

        return connection;
    }

}
