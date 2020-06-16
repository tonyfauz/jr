package com.ls.jr.exceptions.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoSuchReportException extends Exception {
    private static final Logger log = LoggerFactory.getLogger(NoSuchReportException.class);

    public NoSuchReportException(String message, Throwable cause) {
        super(message, cause);
    }
}
