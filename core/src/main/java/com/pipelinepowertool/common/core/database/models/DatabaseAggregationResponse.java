package com.pipelinepowertool.common.core.database.models;

import java.math.BigDecimal;

public abstract class DatabaseAggregationResponse {
    private final long runtime;
    private final long pipelineRuns;
    private final BigDecimal watts;
    private final BigDecimal utilization;

    public DatabaseAggregationResponse(long runtime, long pipelineRuns, BigDecimal watts,
                                       BigDecimal utilization) {
        this.runtime = runtime;
        this.pipelineRuns = pipelineRuns;
        this.watts = watts;
        this.utilization = utilization;
    }

    public long getRuntime() {
        return runtime;
    }

    public long getPipelineRuns() {
        return pipelineRuns;
    }

    public BigDecimal getWatts() {
        return watts;
    }

    public BigDecimal getUtilization() {
        return utilization;
    }
}
