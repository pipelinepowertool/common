import static org.junit.jupiter.api.Assertions.assertEquals;

import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.pipelinepowertool.common.core.database.EnergyReading;
import com.pipelinepowertool.common.core.database.EnergyReadingRecord;
import com.pipelinepowertool.common.core.database.elasticsearch.AggregatedEnergyReading;
import com.pipelinepowertool.common.core.database.elasticsearch.ElasticSearchService;
import com.pipelinepowertool.common.core.pipeline.jenkins.JenkinsMetadata;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.apache.http.HttpHost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

class ElasticSearchServiceIntegrationTest {

    private ElasticSearchService elasticSearchService;


    @BeforeEach
    void init() {
        DockerImageName dockerImageName = DockerImageName.parse(
            "docker.elastic.co/elasticsearch/elasticsearch").withTag("8.8.2");
        ElasticsearchContainer container = new ElasticsearchContainer(dockerImageName)
            .withExposedPorts(9200)
            .withPassword("password");
        container.start();
        HttpHost host = new HttpHost("localhost",
            container.getMappedPort(9200), "https");
        byte[] certAsBytes = container.copyFileFromContainer(
            "/usr/share/elasticsearch/config/certs/http_ca.crt",
            InputStream::readAllBytes);

        elasticSearchService = new ElasticSearchService("elastic", "password", certAsBytes, host);

    }

    @Test
    void test_jenkins_index() throws ExecutionException, InterruptedException, TimeoutException {
        List<EnergyReadingRecord> energyReadingRecords = createTestRecords(1, 1, 1);
        assertEquals(1, energyReadingRecords.size());
        CompletableFuture<IndexResponse> responseInFuture = elasticSearchService.send(
            energyReadingRecords.get(0));
        IndexResponse response = responseInFuture.get(10, TimeUnit.SECONDS);
        assertEquals("created", response.result().jsonValue());

    }

    @ParameterizedTest
    @MethodSource("provideStringsForIsBlank")
    void test_jenkins_aggregate_all_pipelines(List<EnergyReadingRecord> energyReadingRecords)
        throws InterruptedException, ExecutionException {
        insertTestRecords(energyReadingRecords);

        JenkinsMetadata jenkinsMetadata = new JenkinsMetadata(null, null, null, null, null);
        CompletableFuture<SearchResponse<Void>> responseInFuture = elasticSearchService.aggregate(
            jenkinsMetadata);
        Thread.sleep(2000);
        AggregatedEnergyReading aggregatedEnergyReading = new AggregatedEnergyReading(
            responseInFuture.get());

        assertEquals(120, aggregatedEnergyReading.getRuntime());
        assertEquals(2, aggregatedEnergyReading.getPipelineRuns());
        assertEquals(1.0, aggregatedEnergyReading.getUtilization().doubleValue());
        assertEquals(0.2, aggregatedEnergyReading.getWatts().doubleValue());
    }

    private static Stream<Arguments> provideStringsForIsBlank() {
        return Stream.of(
            Arguments.of(createTestRecords(2, 1, 1)),
            Arguments.of(createTestRecords(1, 2, 1)),
            Arguments.of(createTestRecords(1, 1, 2))
        );
    }

    private void insertTestRecords(List<EnergyReadingRecord> energyReadingRecords)
        throws InterruptedException {
        CompletableFuture<BulkResponse> indexResponseInFuture = elasticSearchService.send(
            energyReadingRecords);
        Thread.sleep(2000);
    }

    private static List<EnergyReadingRecord> createTestRecords(int amountPipelines,
        int amountBranches,
        int amountBuilds) {
        OffsetDateTime pipelineEnd = OffsetDateTime.now();
        OffsetDateTime pipelineStart = pipelineEnd.minusMinutes(1);
        EnergyReading energyReading = new EnergyReading(BigDecimal.valueOf(0.1), BigDecimal.ONE);

        List<EnergyReadingRecord> list = new ArrayList<>();
        IntStream.range(0, amountPipelines).forEach(i -> IntStream.range(0, amountBranches)
            .forEach(j -> IntStream.range(0, amountBuilds).forEach(k -> {
                String job = "pipeline" + i;
                String branch = "master" + i;
                JenkinsMetadata jenkinsMetadata = new JenkinsMetadata(job, branch, (long) i,
                    pipelineStart, pipelineEnd);
                list.add(new EnergyReadingRecord(energyReading, jenkinsMetadata));
            })));
        return list;
    }
}