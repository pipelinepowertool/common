package com.pipelinepowertool.common.core.pipeline;

public enum Pipeline {
    JENKINS("Jenkins");

    private final String tool;

    Pipeline(String pipelineTool) {
        this.tool = pipelineTool;
    }

    public String getTool() {
        return tool;
    }
}
