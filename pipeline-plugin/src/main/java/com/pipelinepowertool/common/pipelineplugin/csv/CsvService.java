package com.pipelinepowertool.common.pipelineplugin.csv;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pipelinepowertool.common.core.database.EnergyReading;
import com.pipelinepowertool.common.core.pipeline.PipelineMetadata;
import com.pipelinepowertool.common.core.pipeline.jenkins.JenkinsMetadata;
import java.io.File;
import java.io.FileNotFoundException;

public interface CsvService {

    EnergyReading aggregateReadings(File file) throws FileNotFoundException;
    String convertAggregateToJson(EnergyReading energyReading, PipelineMetadata jenkinsMetadata) throws JsonProcessingException;
}
