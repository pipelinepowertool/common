package com.pipelinepowertool.common.core.pipeline.jenkins;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

import java.util.ArrayList;
import java.util.List;

public class JenkinsElasticSearchQuery {

    public static Query get(JenkinsMetadata pipelineMetadata) {
        List<Query> queries = new ArrayList<>();
        String pipelineTool = pipelineMetadata.getPipelineTool();
        String job = pipelineMetadata.getJob();
        String branch = pipelineMetadata.getBranch();
        Long buildNumber = pipelineMetadata.getBuildNumber();

        if (pipelineTool != null) {
            queries.add(createQuery("metadata.pipeline_tool", pipelineTool));
        }
        if (job != null) {
            queries.add(createQuery("metadata.job", job));
        }
        if (branch != null) {
            queries.add(createQuery("metadata.branch", branch));
        }
        if (buildNumber != null) {
            queries.add(createQuery("metadata.build_number", buildNumber));
        }
        return new BoolQuery.Builder().should(queries).build()._toQuery();
    }

    private static Query createQuery(String field, String value) {
        return MatchQuery.of(builder -> builder.field(field).query(value))._toQuery();
    }

    private static Query createQuery(String field, Long value) {
        return MatchQuery.of(builder -> builder.field(field).query(value))._toQuery();
    }


}
