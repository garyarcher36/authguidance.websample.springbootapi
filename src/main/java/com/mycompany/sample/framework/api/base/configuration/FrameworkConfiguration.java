package com.mycompany.sample.framework.api.base.configuration;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

/*
 * Framework configuration data
 */
public final class FrameworkConfiguration {

    @Getter
    @Setter
    private String apiName;

    @Getter
    @Setter
    private ObjectNode logging;
}