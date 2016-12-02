package com.feedsome.service.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedsome.model.FeedNotification;
import com.feedsome.service.route.configuration.EndpointProperties;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spring.boot.CamelAutoConfiguration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        CamelAutoConfiguration.class,
        DataFeedSendRouteTester.TestConfiguration.class
})
public class DataFeedSendRouteTester {

    private static final String verifyFeedMockUri = "mock:feed:verify";

    @Configuration
    @Import(DataFeedSendRoute.class)
    static class TestConfiguration {

        @Bean
        public ObjectMapper mapper() {
            return new ObjectMapper();
        }

        @Bean
        public EndpointProperties endpointProperties() {
            final EndpointProperties props = new EndpointProperties();

            props.setDataFeedSenderUri("seda:feed:push");
            props.setDataFeedPublishUri("spring-redis://localhost:6379?command=PUBLISH");

            return props;
        }

        @Bean
        public RouteBuilder dataFeedConsumerRouteBuilder() {
            return new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("spring-redis://localhost:6379?command=SUBSCRIBE&channels=sports,fancy")
                            .log("Message received!!!")
                            .log("Message body is following...")
                            .log("${body}")
                            .to(verifyFeedMockUri);
                }
            };
        }

    }


    private static RedisServer redisServer;

    @BeforeClass
    public static void setupRedis() throws IOException {
        redisServer  = new RedisServer(6379);
        redisServer.start();
    }

    @AfterClass
    public static void stopRedis() {
        redisServer.stop();
    }

    @Autowired
    private ObjectMapper mapper;

    @EndpointInject(uri = "seda:feed:push")
    private ProducerTemplate pushDataFeedTemplate;

    @EndpointInject(uri = verifyFeedMockUri)
    private MockEndpoint verifyFeedMockEndpoint;

    @Test
    public void testSendDataFeedRoute_sendsFeed() throws Exception {
        final String pluginRef = "myFancyPlugin";

        final Map<String, String> body = new HashMap<String, String>() {{
            put("type", "super");
            put("text", "super is awesome");
        }};

        final String bodyString = mapper.writeValueAsString(body);

        final FeedNotification feedNotification = new FeedNotification();

        feedNotification.setBody(bodyString);
        feedNotification.setPluginRef(pluginRef);
        feedNotification.setTitle("fancy title goes here");


        feedNotification.getCategories().addAll(Arrays.asList("sports", "super", "fancy"));

        pushDataFeedTemplate.sendBody(feedNotification);

        verifyFeedMockEndpoint.expectedMessageCount(1);

        verifyFeedMockEndpoint.await(8, TimeUnit.SECONDS);

        verifyFeedMockEndpoint.assertIsSatisfied();
    }


}
