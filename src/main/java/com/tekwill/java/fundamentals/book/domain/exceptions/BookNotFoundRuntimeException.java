package com.tekwill.java.fundamentals.book.domain.exceptions;

public class BookNotFoundRuntimeException extends RuntimeException {
    public BookNotFoundRuntimeException(String s) {
        super(s);
    }
}
