package com.ls.jr.exceptions.manager;

import com.haulmont.cuba.core.global.SupportedByClient;

@SupportedByClient
public class SaveReportFileException extends RuntimeException {
    public SaveReportFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public SaveReportFileException(String message) {
        super(message);
    }

}
