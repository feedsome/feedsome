package com.feedsome.service.route;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.feedsome.model.PluginRegistration;
import com.feedsome.service.PluginService;
import com.feedsome.service.route.configuration.EndpointProperties;
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
public class PluginRegistrationRoute {

    @Autowired
    private EndpointProperties endpointProperties;

    @Autowired
    private PluginService pluginService;

    @Bean
    public RouteBuilder pluginRegistrationRouteBuilder() {
        final JacksonDataFormat dataFormat = new JacksonDataFormat(PluginRegistration.class);
        dataFormat.addModule(new Jdk8Module());

        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                onException(ConstraintViolationException.class)
                        .log(LoggingLevel.WARN, "Validation exception on plugin registration route!")
                        .logRetryStackTrace(true)
                        .logStackTrace(true)
                        .logHandled(true)
                        .retryAttemptedLogLevel(LoggingLevel.WARN)
                        .retriesExhaustedLogLevel(LoggingLevel.ERROR)
                        .stop();

                onException(Exception.class)
                        .log(LoggingLevel.ERROR, "Exception on plugin registration route!")
                        .maximumRedeliveries(10)
                        .redeliveryDelay(1000)
                        .useExponentialBackOff()
                        .retryAttemptedLogLevel(LoggingLevel.WARN)
                        .retriesExhaustedLogLevel(LoggingLevel.ERROR);

                from(endpointProperties.getPluginRegistrationUri()).routeId("Plugin registration route")
                        .filter(body().isNotNull())
                        .log("recieved plugin registration request")
                        .unmarshal(dataFormat)
                        .log("parsed plugin registration to " + PluginRegistration.class.getSimpleName())
                        .bean(pluginService, "register")
                        .log("plugin with name: ${body.name} has been registered")
                        .to("mock:plugin:registered");
            }
        };
    }

}
