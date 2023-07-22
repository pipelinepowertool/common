package com.pipelinepowertool.common.core.database.elasticsearch;

import co.elastic.clients.elasticsearch.core.CountResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.pipelinepowertool.common.core.database.models.DatabaseAggregationResponse;
import com.pipelinepowertool.common.core.utils.MathUtils;

import static com.pipelinepowertool.common.core.utils.ElasticSearchConstants.*;

public class ElasticSearchAggregationResponse extends DatabaseAggregationResponse {

    public ElasticSearchAggregationResponse(SearchResponse<Void> response, CountResponse countResponse) {
        super((long) response.aggregations().get(FIELD_RUNTIME).sum().value(),
                countResponse.count(),
                MathUtils.doubleToBigDecimal(
                        response.aggregations().get(FIELD_JOULES).sum().value()),
                MathUtils.doubleToBigDecimal(
                        response.aggregations().get(FIELD_UTILIZATION).avg().value()));
    }
}
