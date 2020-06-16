package com.ls.jr.exceptions.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveReportException extends Exception {
    private static final Logger log = LoggerFactory.getLogger(SaveReportException.class);

    public SaveReportException(String message, Throwable cause) {
        super(message, cause);
    }
}
