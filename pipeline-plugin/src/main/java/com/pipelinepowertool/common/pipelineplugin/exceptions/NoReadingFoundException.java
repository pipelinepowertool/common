package com.pipelinepowertool.common.pipelineplugin.exceptions;

public class NoReadingFoundException extends RuntimeException {

    public NoReadingFoundException() {
        super("No readings found");
    }

}
