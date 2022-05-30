package com.cellulam.spring.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

/**
 *
 */
public class SpringApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent>, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(SpringApplicationReadyListener.class);

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.info("SpringApplicationReadyListener");
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
