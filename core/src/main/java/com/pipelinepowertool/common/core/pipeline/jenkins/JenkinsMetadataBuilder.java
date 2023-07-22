package com.pipelinepowertool.common.core.pipeline.jenkins;

public class JenkinsMetadataBuilder {

    private String job;
    private String branch;
    private Long buildNumber;

    public JenkinsMetadataBuilder withJob(String job) {
        this.job = job;
        return this;
    }

    public JenkinsMetadataBuilder withBranch(String branch) {
        this.branch = branch;
        return this;
    }

    public JenkinsMetadataBuilder withBuildNumber(Long buildNumber) {
        this.buildNumber = buildNumber;
        return this;
    }

    public JenkinsMetadata build() {
        return new JenkinsMetadata(job, branch, buildNumber, null, null);
    }

}
