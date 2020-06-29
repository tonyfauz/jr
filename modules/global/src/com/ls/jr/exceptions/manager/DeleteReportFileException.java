package com.ls.jr.exceptions.manager;

import com.haulmont.cuba.core.global.SupportedByClient;

@SupportedByClient
public class DeleteReportFileException extends RuntimeException {
    public DeleteReportFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteReportFileException(String message) {
        super(message);
    }

}
