package com.pipelinepowertool.common.core.pipeline;

import com.pipelinepowertool.common.core.utils.TimeUtils;

import java.time.OffsetDateTime;

public abstract class PipelineMetadata {

    private final String pipelineTool;
    private final OffsetDateTime startTime;
    private final OffsetDateTime endTime;
    private long runtime;

    protected PipelineMetadata(Pipeline pipeline, OffsetDateTime startTime, OffsetDateTime endTime) {
        this.pipelineTool = pipeline.getTool();
        this.startTime = startTime;
        this.endTime = endTime;
        if (startTime != null && endTime != null) {
            this.runtime = TimeUtils.secondsBetween(startTime, endTime);
        }
    }

    public OffsetDateTime getStartTime() {
        return startTime;
    }

    public OffsetDateTime getEndTime() {
        return endTime;
    }

    public String getPipelineTool() {
        return pipelineTool;
    }

    public long getRuntime() {
        return runtime;
    }
}
