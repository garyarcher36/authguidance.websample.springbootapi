package com.mycompany.sample.plumbing.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/*
 * A layout containing only a custom object with no fields generated by logback
 */
public final class BareJsonLoggingLayout extends LayoutBase<ILoggingEvent> {

    private final boolean prettyPrint;

    /*
     * We use pretty printing for console output but not file output
     */
    public BareJsonLoggingLayout(final boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    /*
     * Log from the second parameter passed into method calls such as logger.info
     */
    public String doLayout(final ILoggingEvent event) {

        var args = event.getArgumentArray();
        if (args.length > 0) {

            // Get the object node back, which was sent from the writeDataItem method of our log entry class
            var data = (ObjectNode) args[0];
            var mapper = new ObjectMapper();

            try {
                if (this.prettyPrint) {

                    // Write a property per line and an array item per line for exception stack traces
                    var prettyPrinter = new DefaultPrettyPrinter();
                    prettyPrinter.indentArraysWith(new CustomArrayIndenter());
                    return mapper.writer(prettyPrinter).writeValueAsString(data) + System.lineSeparator();

                } else {

                    // Set output options to write the JSON object on a single line
                    return mapper.writer().writeValueAsString(data) + System.lineSeparator();
                }
            } catch (JsonProcessingException ex) {
                throw new IllegalStateException("BareJsonLoggingLayout.doLayout exception: " + ex.getMessage());
            }
        }

        return "";
    }
}
