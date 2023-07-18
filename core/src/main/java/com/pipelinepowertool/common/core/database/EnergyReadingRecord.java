package com.pipelinepowertool.common.core.database;

import com.pipelinepowertool.common.core.pipeline.PipelineMetadata;

public class EnergyReadingRecord {

    private final EnergyReading data;

    private final PipelineMetadata metadata;

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
