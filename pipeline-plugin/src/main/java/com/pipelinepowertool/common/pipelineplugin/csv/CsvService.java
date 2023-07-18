package com.pipelinepowertool.common.pipelineplugin.csv;

import com.pipelinepowertool.common.core.database.EnergyReading;

import java.io.FileNotFoundException;
import java.io.Reader;

public interface CsvService {

    EnergyReading aggregateReadings(Reader reader) throws FileNotFoundException;

}
