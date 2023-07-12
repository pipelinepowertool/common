package com.pipelinepowertool.common.core.database.elasticsearch;

import static com.pipelinepowertool.common.core.pipeline.Pipeline.JENKINS;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.pipelinepowertool.common.core.pipeline.PipelineMetadata;
import com.pipelinepowertool.common.core.pipeline.jenkins.JenkinsElasticSearchQuery;
import com.pipelinepowertool.common.core.pipeline.jenkins.JenkinsMetadata;

public interface ElasticSearchQuery {

    static Query createQuery(PipelineMetadata pipelineMetadata) {
        if (JENKINS.getTool().equals(pipelineMetadata.getPipelineTool())) {
            return JenkinsElasticSearchQuery.get((JenkinsMetadata) pipelineMetadata);
        }
        return null;
    }


}
