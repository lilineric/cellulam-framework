package com.cellulam.spring.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

/**
 *
 */
public class SpringApplicationStartedListener implements ApplicationListener<ApplicationStartedEvent>, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(SpringApplicationStartedListener.class);

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        logger.info("SpringApplicationStartedListener");
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
