package com.pipelinepowertool.common.core.database.elasticsearch;

import static com.pipelinepowertool.common.core.utils.ElasticSearchConstants.FIELD_RUNTIME;
import static com.pipelinepowertool.common.core.utils.ElasticSearchConstants.FIELD_UTILIZATION;
import static com.pipelinepowertool.common.core.utils.ElasticSearchConstants.FIELD_WATTS;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.pipelinepowertool.common.core.utils.MathUtils;
import java.math.BigDecimal;

public class AggregatedEnergyReading {

    private final long runtime;
    private final long pipelineRuns;
    private final BigDecimal watts;
    private final BigDecimal utilization;

    public AggregatedEnergyReading(long runtime, long pipelineRuns, BigDecimal watts,
        BigDecimal utilization) {
        this.runtime = runtime;
        this.pipelineRuns = pipelineRuns;
        this.watts = watts;
        this.utilization = utilization;
    }

    public AggregatedEnergyReading(SearchResponse<Void> response) {
        double wattsDouble = response.aggregations().get(FIELD_WATTS).sum().value();
        double utilizationDouble = response.aggregations().get(FIELD_UTILIZATION).avg().value();
        pipelineRuns = response.hits().total().value();
        runtime = (long) response.aggregations().get(FIELD_RUNTIME).sum().value();
        this.watts = MathUtils.doubleToBigDecimal(wattsDouble);
        this.utilization = MathUtils.doubleToBigDecimal(utilizationDouble);
    }

    public long getRuntime() {
        return runtime;
    }

    public BigDecimal getWatts() {
        return watts;
    }

    public BigDecimal getUtilization() {
        return utilization;
    }

    public long getPipelineRuns() {
        return pipelineRuns;
    }
}
