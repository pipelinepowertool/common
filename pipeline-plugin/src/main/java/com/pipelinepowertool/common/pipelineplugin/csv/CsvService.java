package com.pipelinepowertool.common.pipelineplugin.csv;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pipelinepowertool.common.core.database.EnergyReading;
import java.io.File;
import java.io.FileNotFoundException;

public interface CsvService {

    EnergyReading aggregateReadings(File file) throws FileNotFoundException;
    
    String convertAggregateToJson(EnergyReading energyReading) throws JsonProcessingException;
}
