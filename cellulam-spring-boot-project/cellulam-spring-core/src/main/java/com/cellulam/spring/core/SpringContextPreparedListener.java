package com.cellulam.spring.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 *  spring context-prepared 事件监听
 *
 */
public class SpringContextPreparedListener implements ApplicationListener<ApplicationPreparedEvent>, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(SpringContextPreparedListener.class);

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        logger.info("SpringContextPreparedListener");
        SmartContext.initOrResetContext(event.getApplicationContext());

        // context refresh之后，需要重置environment
        ConfigurableEnvironment env = event.getApplicationContext().getEnvironment();
        SmartEnvironment.initOrResetEnvironment(env);

    }

    @Override
    public int getOrder() {
        return 0;
    }

}
