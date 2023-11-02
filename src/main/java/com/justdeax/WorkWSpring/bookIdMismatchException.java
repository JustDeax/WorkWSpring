package com.justdeax.WorkWSpring;

public class bookIdMismatchException extends RuntimeException {
    public bookIdMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
