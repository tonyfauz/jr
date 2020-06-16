package com.ls.jr.exceptions.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteCategoriaException extends Exception {
    private static final Logger log = LoggerFactory.getLogger(DeleteCategoriaException.class);

    public DeleteCategoriaException(String message, Throwable cause) {
        super(message, cause);
    }
}
