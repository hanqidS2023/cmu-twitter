package com.cmux.postservice.handleException;

import java.lang.RuntimeException;

public class IndexingException extends RuntimeException {
    public IndexingException(String message, Throwable cause) {
        super(message, cause);
    }

}