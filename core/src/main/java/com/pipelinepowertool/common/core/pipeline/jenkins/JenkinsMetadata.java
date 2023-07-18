package com.pipelinepowertool.common.core.pipeline.jenkins;

import com.pipelinepowertool.common.core.pipeline.Pipeline;
import com.pipelinepowertool.common.core.pipeline.PipelineMetadata;

import java.time.OffsetDateTime;

public class JenkinsMetadata extends PipelineMetadata {

    private static final Pipeline pipeline = Pipeline.JENKINS;

    private final String job;
    private final String branch;
    private final Long buildNumber;

    public JenkinsMetadata(String job, String branch, Long buildNumber,
                           OffsetDateTime startTime, OffsetDateTime endTime) {
        super(pipeline, startTime, endTime);
        this.job = job;
        this.branch = branch;
        this.buildNumber = buildNumber;
    }

    public String getJob() {
        return job;
    }

    public String getBranch() {
        return branch;
    }

    public Long getBuildNumber() {
        return buildNumber;
    }
}
