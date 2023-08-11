package com.pipelinepowertool.common.core.pipeline.jenkins;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JenkinsElasticSearchQueryTest {

    @Test
    void test() {
        JenkinsMetadata jenkinsMetadata = new JenkinsMetadataBuilder().build();
        Query query = JenkinsElasticSearchQuery.get(jenkinsMetadata);
        List<Query> should = ((BoolQuery) query._get()).should();
        assertEquals(1, should.size());
        assertEquals("[Query: {\"match\":{\"metadata.pipeline_tool\":{\"query\":\"Jenkins\"}}}]", should.toString());
    }

    @Test
    void test_job() {
        JenkinsMetadata jenkinsMetadata = new JenkinsMetadataBuilder().withJob("job").build();
        Query query = JenkinsElasticSearchQuery.get(jenkinsMetadata);
        List<Query> should = ((BoolQuery) query._get()).should();
        assertEquals(2, should.size());
        assertEquals("[Query: {\"match\":{\"metadata.pipeline_tool\":{\"query\":\"Jenkins\"}}}, Query: {\"match\":{\"metadata.job\":{\"query\":\"job\"}}}]", should.toString());
    }

    @Test
    void test_job_branch() {
        JenkinsMetadata jenkinsMetadata = new JenkinsMetadataBuilder().withJob("job").withBranch("branch").build();
        Query query = JenkinsElasticSearchQuery.get(jenkinsMetadata);
        List<Query> should = ((BoolQuery) query._get()).should();
        assertEquals(3, should.size());
        assertEquals("[Query: {\"match\":{\"metadata.pipeline_tool\":{\"query\":\"Jenkins\"}}}, Query: {\"match\":{\"metadata.job\":{\"query\":\"job\"}}}, Query: {\"match\":{\"metadata.branch\":{\"query\":\"branch\"}}}]", should.toString());
    }

    @Test
    void test_job_build() {
        JenkinsMetadata jenkinsMetadata = new JenkinsMetadataBuilder().withJob("job").withBuildNumber(1L).build();
        Query query = JenkinsElasticSearchQuery.get(jenkinsMetadata);
        List<Query> should = ((BoolQuery) query._get()).should();
        assertEquals(3, should.size());
        assertEquals("[Query: {\"match\":{\"metadata.pipeline_tool\":{\"query\":\"Jenkins\"}}}, Query: {\"match\":{\"metadata.job\":{\"query\":\"job\"}}}, Query: {\"match\":{\"metadata.build_number\":{\"query\":1}}}]", should.toString());
    }

    @Test
    void test_job_branch_build() {
        JenkinsMetadata jenkinsMetadata = new JenkinsMetadataBuilder().withJob("job").withBranch("branch").withBuildNumber(1L).build();
        Query query = JenkinsElasticSearchQuery.get(jenkinsMetadata);
        List<Query> should = ((BoolQuery) query._get()).should();
        assertEquals(4, should.size());
        assertEquals("[Query: {\"match\":{\"metadata.pipeline_tool\":{\"query\":\"Jenkins\"}}}, Query: {\"match\":{\"metadata.job\":{\"query\":\"job\"}}}, Query: {\"match\":{\"metadata.branch\":{\"query\":\"branch\"}}}, Query: {\"match\":{\"metadata.build_number\":{\"query\":1}}}]", should.toString());
    }

}