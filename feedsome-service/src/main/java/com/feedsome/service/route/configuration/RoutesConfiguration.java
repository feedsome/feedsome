package com.feedsome.service.route.configuration;

import com.feedsome.service.route.DataFeedRoute;
import com.feedsome.service.route.DataFeedSendRoute;
import com.feedsome.service.route.PluginRegistrationRoute;
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
