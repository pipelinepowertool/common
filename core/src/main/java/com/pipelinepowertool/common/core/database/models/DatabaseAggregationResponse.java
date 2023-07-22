package com.pipelinepowertool.common.core.database.models;

import java.math.BigDecimal;

public abstract class DatabaseAggregationResponse {
    private final long runtime;
    private final long pipelineRuns;
    private final BigDecimal joules;
    private final BigDecimal utilization;

    protected DatabaseAggregationResponse(long runtime, long pipelineRuns, BigDecimal joules,
                                          BigDecimal utilization) {
        this.runtime = runtime;
        this.pipelineRuns = pipelineRuns;
        this.joules = joules;
        this.utilization = utilization;
    }

    public long getRuntime() {
        return runtime;
    }

    public long getPipelineRuns() {
        return pipelineRuns;
    }

    public BigDecimal getJoules() {
        return joules;
    }

    public BigDecimal getUtilization() {
        return utilization;
    }
}
