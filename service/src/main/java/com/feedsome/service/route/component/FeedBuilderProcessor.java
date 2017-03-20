package com.feedsome.service.route.component;

import com.feedsome.model.Category;
import com.feedsome.model.Feed;
import com.feedsome.model.FeedNotification;
import com.feedsome.model.Plugin;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.Collection;
import java.util.stream.Collectors;

public class FeedBuilderProcessor implements Processor {
    
    @Override
    public void process(Exchange exchange) throws Exception {
        final FeedNotification feedNotification = exchange.getIn().getBody(FeedNotification.class);

        final Plugin plugin = new Plugin();
        plugin.setName(feedNotification.getPluginRef());

        final Collection<Category> categories = feedNotification.getCategories().stream()
                .map(categoryName -> {
                    final Category category = new Category();
                    category.setName(categoryName);

                    return category;
                }).collect(Collectors.toList());

        final Feed feed = new Feed();
        feed.setPlugin(plugin);
        feed.setTitle(feedNotification.getTitle());
        feed.setBody(feedNotification.getBody());

        feed.getCategories().addAll(categories);

        exchange.getIn().setBody(feed, Feed.class);
        exchange.getIn().setHeader("feedNotification", feedNotification);
    }
}
