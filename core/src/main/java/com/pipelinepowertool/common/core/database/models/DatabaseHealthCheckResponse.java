package com.pipelinepowertool.common.core.database.models;

public class DatabaseHealthCheckResponse {

    private final String value;

    public DatabaseHealthCheckResponse(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
