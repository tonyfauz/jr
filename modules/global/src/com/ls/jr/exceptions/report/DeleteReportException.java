package com.ls.jr.exceptions.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteReportException extends  Exception {
    private static final Logger log = LoggerFactory.getLogger(DeleteReportException.class);

    public DeleteReportException(String message, Throwable cause) {
        super(message, cause);
    }
}
