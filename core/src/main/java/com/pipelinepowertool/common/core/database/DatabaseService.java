package com.pipelinepowertool.common.core.database;

import com.pipelinepowertool.common.core.database.models.DatabaseAggregationResponse;
import com.pipelinepowertool.common.core.database.models.DatabaseHealthCheckResponse;
import com.pipelinepowertool.common.core.database.models.DatabaseInsertResponse;
import com.pipelinepowertool.common.core.pipeline.PipelineMetadata;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DatabaseService {

    CompletableFuture<DatabaseInsertResponse> send(EnergyReadingRecord readingRecord)
        throws IOException;

    CompletableFuture<List<DatabaseInsertResponse>> send(List<EnergyReadingRecord> readingRecord);

    CompletableFuture<DatabaseAggregationResponse> aggregate(PipelineMetadata pipelineMetadata);

    CompletableFuture<DatabaseHealthCheckResponse> healthCheck();
}
