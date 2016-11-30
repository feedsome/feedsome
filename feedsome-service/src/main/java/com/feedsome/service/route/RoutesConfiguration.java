package com.feedsome.service.route;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        PluginRegistrationRoute.class,
        DataFeedRoute.class,
        DataFeedSendRoute.class
})
public class RoutesConfiguration {

}
