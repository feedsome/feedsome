package com.feedsome.service.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedsome.model.Feed;
import com.feedsome.model.FeedNotification;
import com.feedsome.model.Plugin;
import com.feedsome.service.FeedService;
import com.feedsome.service.PluginService;
import com.feedsome.service.route.configuration.EndpointProperties;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spring.boot.CamelAutoConfiguration;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        CamelAutoConfiguration.class,
        DataFeedRouteTester.TestConfiguration.class
})
@PropertySource("classpath:application.yml")
public class DataFeedRouteTester {


    @Configuration
    @Import({
            DataFeedRoute.class
    })
    static class TestConfiguration {

        @Bean
        public ObjectMapper mapper() {
            return new ObjectMapper();
        }


        @Bean
        public PluginService pluginService() {
            return mock(PluginService.class);
        }

        @Bean
        public FeedService feedService() {
            return mock(FeedService.class);
        }

        @Bean
        public EndpointProperties endpointProperties() {
            EndpointProperties props = new EndpointProperties();

            props.setDataFeedUri("seda:feedsome:plugin:feed");
            props.setDataFeedProcessUri("direct:feed:process");

            props.setDataFeedSenderUri("mock:feedsome:processed");


            return props;
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

    @Autowired
    private FeedService feedService;

    @EndpointInject(uri = "seda:feedsome:plugin:feed")
    private ProducerTemplate sendDataFeedTemplate;

    @EndpointInject(uri = "mock:feedsome:processed")
    private MockEndpoint mockEndpoint;

    @Before
    public void setup() {
        reset(
                feedService
        );
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(
                feedService
        );
    }

    //
    // tests: data feed route
    //

    @Test
    public void testPluginDataFeedRoute_parsesMessage() throws Exception {
        final String pluginRef = "myFancyPlugin";

        final Map<String, String> body = new HashMap<String, String>() {{
            put("type", "super");
            put("text", "super is awesome");
        }};

        String bodyString = mapper.writeValueAsString(body);

        final FeedNotification feedNotification = new FeedNotification();

        feedNotification.setBody(bodyString);
        feedNotification.setPluginRef(pluginRef);
        feedNotification.setTitle("fancy title goes here");


        final Feed feed = Feed.builder().body(bodyString)
                .title("fancy title goes here")
                .plugin(new Plugin())
                .id("32w4wffefef")
                .build();

        when(feedService.persistNotification(any(FeedNotification.class)))
                .thenReturn(feed);

        String feedNotificationMessage = mapper.writeValueAsString(feedNotification);

        sendDataFeedTemplate.sendBody(feedNotificationMessage.getBytes());

        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.await(5L, TimeUnit.SECONDS);

        verify(feedService, only()).persistNotification(any(FeedNotification.class));

    }


}
