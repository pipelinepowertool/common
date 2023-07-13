package com.pipelinepowertool.common.core.database.elasticsearch;

import static com.pipelinepowertool.common.core.utils.ElasticSearchConstants.FIELD_RUNTIME;
import static com.pipelinepowertool.common.core.utils.ElasticSearchConstants.FIELD_UTILIZATION;
import static com.pipelinepowertool.common.core.utils.ElasticSearchConstants.FIELD_WATTS;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.pipelinepowertool.common.core.database.models.DatabaseAggregationResponse;
import com.pipelinepowertool.common.core.utils.MathUtils;

public class ElasticSearchAggregationResponse extends DatabaseAggregationResponse {

    public ElasticSearchAggregationResponse(SearchResponse<Void> response) {
        super((long) response.aggregations().get(FIELD_RUNTIME).sum().value(),
            response.hits().total().value(),
            MathUtils.doubleToBigDecimal(
                response.aggregations().get(FIELD_WATTS).sum().value()),
            MathUtils.doubleToBigDecimal(
                response.aggregations().get(FIELD_UTILIZATION).avg().value()));
    }
}
