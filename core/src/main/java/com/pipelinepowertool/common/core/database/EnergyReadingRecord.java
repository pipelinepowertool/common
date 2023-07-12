package com.pipelinepowertool.common.core.database;

import com.pipelinepowertool.common.core.pipeline.PipelineMetadata;

public class EnergyReadingRecord {

    private EnergyReading data;

    private PipelineMetadata metadata;

    public EnergyReadingRecord() {}

    public EnergyReadingRecord(EnergyReading data,
        PipelineMetadata metadata) {
        this.data = data;
        this.metadata = metadata;
    }

    public EnergyReading getData() {
        return data;
    }

    public PipelineMetadata getMetadata() {
        return metadata;
    }
}
