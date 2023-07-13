package com.pipelinepowertool.common.pipelineplugin.csv;

import com.pipelinepowertool.common.core.database.EnergyReading;
import java.io.FileNotFoundException;

public interface CsvService {

    EnergyReading aggregateReadings() throws FileNotFoundException;
}
