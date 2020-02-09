package com.mycompany.sample.framework.api.base.logging;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/*
 * A utility to use Spring's mechanism to ensure that the log entry is only created once per request
 * We cannot create a Bean in our LoggerFactory since it does not have the @Component annotation
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@SuppressWarnings(value = "checkstyle:DesignForExtension")
public class LogEntryInjector {

    private final LoggerFactoryImpl loggerFactory;

    public LogEntryInjector(final LoggerFactoryImpl loggerFactory) {
        this.loggerFactory = loggerFactory;
    }

    /*
     * Use a Bean to create the log entry the first time it is asked for during an API request
     */
    @Bean
    @RequestScope
    public LogEntryImpl createLogEntry() {
        return this.loggerFactory.createLogEntry();
    }
}