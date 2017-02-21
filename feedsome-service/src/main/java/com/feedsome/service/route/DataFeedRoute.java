package com.feedsome.service.route;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.feedsome.model.FeedNotification;
import com.feedsome.service.FeedService;
import com.feedsome.service.route.configuration.EndpointProperties;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.ConstraintViolationException;

@Configuration
@EnableConfigurationProperties(EndpointProperties.class)
public class DataFeedRoute {
    private final String dataFeedStoreUri = "seda:feed:store";

    @Autowired
    private EndpointProperties endpointProperties;

    @Autowired
    @Qualifier("feedBuilderProcessor")
    private Processor feedBuilderProcessor;

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

                from(endpointProperties.getDataFeedUri()).routeId("Data feed route")
                        .filter(body().isNotNull())
                        .log("received data feed request from DAQ")
                        .log("JSON message: ${body}")
                        .unmarshal(dataFormat)
                        .log(String.format("parsed message to %s object", FeedNotification.class.getSimpleName()))
                        .to(ExchangePattern.OutOnly, dataFeedStoreUri)
                        .log("feed has been persisted to the system. Now sending it for process...")
                        .to(endpointProperties.getDataFeedProcessUri());
            }
        };
    }

    @Bean
    public RouteBuilder dataFeedStoreRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                onException(Exception.class)
                        .maximumRedeliveries(5)
                        .redeliveryDelay(3000)
                        .useExponentialBackOff()
                        .logRetryStackTrace(true)
                        .logStackTrace(true)
                        .logHandled(true)
                        .retryAttemptedLogLevel(LoggingLevel.WARN)
                        .retriesExhaustedLogLevel(LoggingLevel.ERROR);

                from(dataFeedStoreUri)
                        .log("recieved data feed to store in persistence unit")
                        .process(feedBuilderProcessor)
                        .bean(feedService, "persist")
                        .log("data feed stored to database")
                        .setHeader("feed", simple("${body}"))
                        .setBody(simple("${header.feedNotification}"));
            }
        };
    }

    @Bean
    public RouteBuilder dataFeedProcessRouteBuilder() {
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

                from(endpointProperties.getDataFeedProcessUri()).routeId("Data feed process route")
                        .filter(body().isNotNull())
                        .log("received data feed for processing")
                        .log("currently no process actions exist, sending feed to notification route!")
                        .to(endpointProperties.getDataFeedSenderUri());
            }
        };
    }

}
