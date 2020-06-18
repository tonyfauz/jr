package com.ls.jr.exceptions.manager;

import com.haulmont.cuba.core.global.SupportedByClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SupportedByClient
public class SaveCategoriaException extends RuntimeException {
    private static final Logger log = LoggerFactory.getLogger(SaveCategoriaException.class);

    public SaveCategoriaException(String message, Throwable cause) {
        super(message, cause);
    }

    public SaveCategoriaException(String message) {
        super(message);
    }
}
