package com.ls.jr.exceptions.manager;

import com.haulmont.cuba.core.global.SupportedByClient;

@SupportedByClient
public class DeleteReportException extends RuntimeException {
    public DeleteReportException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteReportException(String message) {
        super(message);
    }

}
