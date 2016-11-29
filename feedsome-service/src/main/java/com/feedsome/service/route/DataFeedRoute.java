package com.feedsome.service.route;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.feedsome.model.FeedNotification;
import com.feedsome.service.FeedService;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.ConstraintViolationException;

@Configuration
@EnableConfigurationProperties(EndpointProperties.class)
public class DataFeedRoute {

    @Autowired
    private EndpointProperties endpointProperties;

    @Autowired
    private FeedService feedService;

    @Bean
    public RouteBuilder dataFeedRouteBuilder() {
        final JacksonDataFormat dataFormat = new JacksonDataFormat(FeedNotification.class);

        dataFormat.addModule(new JavaTimeModule());
        dataFormat.addModule(new Jdk8Module());
        dataFormat.disableFeature(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                onException(ConstraintViolationException.class)
                        .logRetryStackTrace(true)
                        .logStackTrace(true)
                        .logHandled(true)
                        .retryAttemptedLogLevel(LoggingLevel.WARN)
                        .retriesExhaustedLogLevel(LoggingLevel.ERROR)
                        .stop();

                onException(Exception.class)
                        .maximumRedeliveries(5)
                        .redeliveryDelay(3000)
                        .useExponentialBackOff()
                        .logRetryStackTrace(true)
                        .logStackTrace(true)
                        .logHandled(true)
                        .retryAttemptedLogLevel(LoggingLevel.WARN)
                        .retriesExhaustedLogLevel(LoggingLevel.ERROR);

                from(endpointProperties.getDataFeedUir()).routeId("Data feed route")
                        .filter(body().isNotNull())
                        .log("received plugin registration request")
                        .log("JSON message: ${body}")
                        .unmarshal(dataFormat)
                        .log("parsed message to " + FeedNotification.class.getSimpleName())
                        .bean(feedService, "persistNotification")

                        .log("plugin with name: ${body.name} has been registered");
            }
        };
    }


}
