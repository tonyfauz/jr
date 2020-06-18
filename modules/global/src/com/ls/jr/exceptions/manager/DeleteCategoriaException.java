package com.ls.jr.exceptions.manager;

import com.haulmont.cuba.core.global.SupportedByClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SupportedByClient
public class DeleteCategoriaException extends RuntimeException {
    private static final Logger log = LoggerFactory.getLogger(DeleteCategoriaException.class);

    public DeleteCategoriaException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteCategoriaException(String message) {
        super(message);
    }
}
