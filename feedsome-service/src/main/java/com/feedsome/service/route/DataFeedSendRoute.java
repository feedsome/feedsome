package com.feedsome.service.route;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.feedsome.model.FeedNotification;
import com.feedsome.service.route.configuration.EndpointProperties;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableConfigurationProperties(EndpointProperties.class)
public class DataFeedSendRoute {
    private static final String PUBLISH_TOPICS = "publishTopics";
    private static final String TOPICS_COUNT = "topicsCount";

    @Autowired
    private EndpointProperties endpointProperties;

    @Bean
    public RouteBuilder dataFeedSenderRouteBuilder() {
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

                from(endpointProperties.getDataFeedSenderUri()).routeId("data feed notification sender")
                        .filter(body().isNotNull())
                        .log("received data feed message for the send process")
                        .process((processor) -> {
                            final FeedNotification messageBody = processor.getIn().getBody(FeedNotification.class);

                            final Collection<String> categories = messageBody.getCategories();

                            processor.getIn().setHeader(TOPICS_COUNT, categories.size());
                            processor.getIn().setHeader(PUBLISH_TOPICS, new ArrayList<>(categories));
                        })
                        .marshal(dataFormat)
                        .setHeader("CamelRedis.Message", simple("${body}"))
                        .loop(header(TOPICS_COUNT))
                            .process((processor) -> {
                                final int loopIndex = processor.getProperty("CamelLoopIndex", Integer.class);

                                List<String> channels = processor.getIn().getHeader(PUBLISH_TOPICS, ArrayList.class);

                                processor.getIn().setHeader("CamelRedis.Channel", channels.get(loopIndex));
                            })
                            .log("data feed topic  ${header[CamelRedis.Channel]}")
                            .to(endpointProperties.getDataFeedPublishUri())
                        .end()
                        .log("data feed sent to the specified topics");
            }
        };

    }


}
