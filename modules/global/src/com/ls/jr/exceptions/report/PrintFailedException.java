package com.ls.jr.exceptions.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintFailedException extends Exception {
    private static final Logger log = LoggerFactory.getLogger(PrintFailedException.class);

    public PrintFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrintFailedException(String message) {
        super(message);
    }
}
