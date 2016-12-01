package com.feedsome.service.route;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.feedsome.model.Category;
import com.feedsome.model.Feed;
import com.feedsome.service.route.configuration.EndpointProperties;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@Configuration
@EnableConfigurationProperties(EndpointProperties.class)
public class DataFeedSendRoute {

    private static final String CHANNELS_PARAM = "${channels}";
    private static final String PUBLISH_URI_HEADER = "PUBLISH_URI";
    private static final String publishUriHeaderExpression = String.format("${header.%s}", PUBLISH_URI_HEADER);

    @Autowired
    private EndpointProperties endpointProperties;

    public RouteBuilder dataFeedSenderRouteBuilder() {
        final JacksonDataFormat dataFormat = new JacksonDataFormat(Feed.class);

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

                from(endpointProperties.getDataFeedSenderUri())
                        .filter(body().isNotNull())
                        .log("received data feed message to send")
                        .process((processor) -> {
                            final Feed messageBody = processor.getIn().getBody(Feed.class);

                            final String channels = messageBody.getCategories().stream()
                                    .map(Category::getName)
                                    .collect(Collectors.joining(","));

                            final String publishUri = endpointProperties.getDataFeedPublishUriRegex()
                                    .replace(CHANNELS_PARAM, channels);

                            processor.getIn().setHeader(PUBLISH_URI_HEADER, publishUri);
                        })
                        .marshal(dataFormat)
                        .log("data feed topics " + publishUriHeaderExpression)
                        .recipientList(simple(publishUriHeaderExpression));
                        //.recipientList(header(PUBLISH_URI_HEADER));
            }
        };

    }


}
