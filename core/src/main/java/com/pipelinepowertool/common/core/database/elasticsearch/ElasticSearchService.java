package com.pipelinepowertool.common.core.database.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkRequest.Builder;
import co.elastic.clients.elasticsearch.core.CountResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pipelinepowertool.common.core.database.DatabaseService;
import com.pipelinepowertool.common.core.database.EnergyReadingRecord;
import com.pipelinepowertool.common.core.database.models.DatabaseAggregationResponse;
import com.pipelinepowertool.common.core.database.models.DatabaseHealthCheckResponse;
import com.pipelinepowertool.common.core.database.models.DatabaseInsertResponse;
import com.pipelinepowertool.common.core.pipeline.PipelineMetadata;
import com.pipelinepowertool.common.core.utils.SslUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.NodeSelector;
import org.elasticsearch.client.RestClient;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.pipelinepowertool.common.core.utils.ElasticSearchConstants.*;

public class ElasticSearchService implements DatabaseService {

    private final ElasticsearchAsyncClient esClient;

    private static final NodeSelector INGEST_NODE_SELECTOR = nodes -> {
        final Iterator<Node> iterator = nodes.iterator();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            // roles may be null if we don't know, thus we keep the node in then...
            if (node.getRoles() != null && !node.getRoles().isIngest()) {
                iterator.remove();
            }
        }
    };

    public ElasticSearchService(HttpHost host) {

        RestClient restClient = RestClient
                .builder(host)
                .setNodeSelector(INGEST_NODE_SELECTOR)
                .build();
        JacksonJsonpMapper jsonMapper = getJacksonJsonpMapper();
        ElasticsearchTransport elasticsearchTransport =
                new RestClientTransport(restClient, jsonMapper);
        esClient = new ElasticsearchAsyncClient(elasticsearchTransport);
    }

    public ElasticSearchService(String userName, String password, byte[] certAsBytes,
                                HttpHost host) {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(userName, password)
        );

        RestClient restClient = RestClient
                .builder(host)
                .setHttpClientConfigCallback(
                        httpClientBuilder -> httpClientBuilder
                                .setSSLContext(SslUtils.createContextFromCaCert(certAsBytes))
                                .setDefaultCredentialsProvider(credentialsProvider))
                .setNodeSelector(INGEST_NODE_SELECTOR)
                .build();
        JacksonJsonpMapper jsonMapper = getJacksonJsonpMapper();
        ElasticsearchTransport elasticsearchTransport =
                new RestClientTransport(restClient, jsonMapper);
        esClient = new ElasticsearchAsyncClient(elasticsearchTransport);
    }

    private static JacksonJsonpMapper getJacksonJsonpMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new JacksonJsonpMapper(mapper);
    }

    @Override
    public CompletableFuture<DatabaseHealthCheckResponse> healthCheck() {
        return esClient
                .cluster()
                .health()
                .thenApply(r -> new DatabaseHealthCheckResponse(r.status().jsonValue()));
    }

    @Override
    public CompletableFuture<DatabaseInsertResponse> send(EnergyReadingRecord readingRecord) {
        IndexRequest<EnergyReadingRecord> request = IndexRequest.of(i -> i
                .index(INDEX)
                .document(readingRecord)
        );
        return esClient.index(request).thenApply(r -> new DatabaseInsertResponse(r.result().jsonValue()));
    }

    @Override
    public CompletableFuture<List<DatabaseInsertResponse>> send(List<EnergyReadingRecord> readingRecords) {
        BulkRequest.Builder br = new Builder();
        for (EnergyReadingRecord readingRecord : readingRecords) {
            br.operations(op -> op.index(idx -> idx.index(INDEX).document(readingRecord)));
        }
        return esClient.bulk(br.build()).thenApply(r -> r.items().stream().map(i -> new DatabaseInsertResponse(i.result())).collect(
                Collectors.toList()));
    }

    @Override
    public CompletableFuture<DatabaseAggregationResponse> aggregate(PipelineMetadata pipelineMetadata) {
        Query query = ElasticSearchQuery.createQuery(pipelineMetadata);
        CompletableFuture<CountResponse> count = esClient
                .count(s -> s
                        .index(INDEX)
                        .query(query));

        CompletableFuture<SearchResponse<Void>> search = esClient.search(s -> s
                .index(INDEX)
                .size(0)
                .query(query)
                .aggregations(aggregationMap()), Void.class);
        return search.thenCombine(count, ElasticSearchAggregationResponse::new);
    }

    private Map<String, Aggregation> aggregationMap() {
        return Map.of(
                FIELD_RUNTIME, AggregationBuilders.sum().field(NODE_METADATA + "." + FIELD_RUNTIME).build()._toAggregation(),
                FIELD_JOULES, AggregationBuilders.sum().field(NODE_DATA + "." + FIELD_JOULES).build()._toAggregation(),
                FIELD_UTILIZATION, AggregationBuilders.avg().field(NODE_DATA + "." + FIELD_UTILIZATION).build()._toAggregation());
    }


}



