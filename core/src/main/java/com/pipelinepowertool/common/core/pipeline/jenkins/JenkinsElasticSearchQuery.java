package com.pipelinepowertool.common.core.pipeline.jenkins;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class JenkinsElasticSearchQuery {

    public static Query get(JenkinsMetadata pipelineMetadata) {
        MatchQuery.Builder builder = new Builder();
        String pipelineTool = pipelineMetadata.getPipelineTool();
        String job = pipelineMetadata.getJob();
        String branch = pipelineMetadata.getBranch();
        Long buildNumber = pipelineMetadata.getBuildNumber();

        if (pipelineTool != null) {
            builder = builder.field("metadata.pipeline_tool").query(pipelineTool);
        }
        if (job != null) {
            builder = builder.field("metadata.job").query(job);
        }
        if (branch != null) {
            builder = builder.field("metadata.branch").query(branch);
        }
        if (buildNumber != null) {
            builder = builder.field("metadata.build_number").query(buildNumber);
        }
        final MatchQuery.Builder matchQuery = builder;
        return MatchQuery.of(m -> matchQuery)._toQuery();
    }


}
