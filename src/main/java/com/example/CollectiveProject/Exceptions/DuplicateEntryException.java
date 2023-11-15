package com.example.CollectiveProject.Exceptions;

public class DuplicateEntryException extends Exception {

    public DuplicateEntryException() {
    }

    public DuplicateEntryException(String message) {
        super(message);
    }
}
