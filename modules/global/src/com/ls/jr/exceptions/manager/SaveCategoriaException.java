package com.ls.jr.exceptions.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveCategoriaException extends Exception {
    private static final Logger log = LoggerFactory.getLogger(SaveCategoriaException.class);

    public SaveCategoriaException(String message, Throwable cause) {
        super(message, cause);
    }
}
