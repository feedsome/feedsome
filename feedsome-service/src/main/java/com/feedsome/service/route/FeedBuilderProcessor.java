package com.feedsome.service.route;

import com.feedsome.model.Feed;
import com.feedsome.model.FeedNotification;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Represents a component that is responsible for building {@link Feed} instances.
 */
public class FeedBuilderProcessor implements Processor {

    @Override
    public void process(final Exchange exchange) throws Exception {
        FeedNotification feedNotification = exchange.getIn().getBody(FeedNotification.class);


    }
}
