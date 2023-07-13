package com.pipelinepowertool.common.core.database.models;

public class DatabaseInsertResponse {

    private final String status;

    public DatabaseInsertResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
