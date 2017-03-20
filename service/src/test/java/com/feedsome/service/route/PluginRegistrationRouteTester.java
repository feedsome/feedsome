package com.feedsome.service.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedsome.model.Plugin;
import com.feedsome.model.PluginRegistration;
import com.feedsome.service.FeedService;
import com.feedsome.service.PluginService;
import com.feedsome.service.route.configuration.EndpointProperties;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spring.boot.CamelAutoConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        CamelAutoConfiguration.class,
        PluginRegistrationRouteTester.TestConfiguration.class
})
@PropertySource("classpath:application.yml")
public class PluginRegistrationRouteTester {

    @Configuration
    @Import({
            PluginRegistrationRoute.class
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

            props.setPluginRegistrationUri("seda:feedsome:plugin:registration?concurrentConsumers=5");

            return props;
        }

    }


    @Autowired
    private PluginService pluginService;

    @Autowired
    private FeedService feedService;

    @EndpointInject(uri = "seda:feedsome:plugin:registration")
    private ProducerTemplate pluginRegistrationTemplate;

    @EndpointInject(uri = "seda:feedsome:plugin:feed")
    private ProducerTemplate dataFeedTemplate;

    @EndpointInject(uri = "mock:plugin:registered")
    private MockEndpoint pluginRegisteredUriMock;

    @Autowired
    private ObjectMapper mapper;

    @Before
    public void setup() {
        reset(
                pluginService,
                feedService
        );
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(
                pluginService,
                feedService
        );
    }


    @Test
    public void testPluginRegistrationRoute_RegistersNewPlugin() throws Exception {
        final PluginRegistration registration = new PluginRegistration();
        registration.setName("superPlugin");
        registration.getCategories().addAll(Arrays.asList("super", "duper", "universe"));

        final String registrationMessage = mapper.writeValueAsString(registration);


        final Plugin plugin = new Plugin();
        plugin.setId("908as09d8fe3");
        plugin.setActive(true);
        plugin.setName(registration.getName());
        plugin.setCategoryNames(registration.getCategories());

        when(pluginService.register(any(PluginRegistration.class)))
                .thenReturn(plugin);

        pluginRegistrationTemplate.sendBody(registrationMessage);

        pluginRegisteredUriMock.expectedMessageCount(1);
        pluginRegisteredUriMock.await(5L, TimeUnit.SECONDS);

        pluginRegisteredUriMock.assertIsSatisfied();
        verify(pluginService, only()).register(any(PluginRegistration.class));

    }


}
