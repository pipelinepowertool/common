package com.pipelinepowertool.common.core.database.elasticsearch;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.pipelinepowertool.common.core.database.models.DatabaseAggregationResponse;
import com.pipelinepowertool.common.core.utils.MathUtils;

import static com.pipelinepowertool.common.core.utils.ElasticSearchConstants.*;

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
