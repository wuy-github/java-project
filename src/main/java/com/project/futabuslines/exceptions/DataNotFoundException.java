package com.project.futabuslines.exceptions;

import lombok.Getter;

@Getter
public class DataNotFoundException extends Exception {
    private final String errorCode;

    public DataNotFoundException(String message) {
        super(message);
        this.errorCode = "DATA_NOT_FOUND";
    }

}
