package com.pipelinepowertool.common.core.database;

import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.pipelinepowertool.common.core.pipeline.PipelineMetadata;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DatabaseService {

    CompletableFuture<IndexResponse> send(EnergyReadingRecord readingRecord)
        throws IOException;

    CompletableFuture<BulkResponse> send(List<EnergyReadingRecord> readingRecord);

    CompletableFuture<SearchResponse<Void>> aggregate(PipelineMetadata pipelineMetadata);
}
