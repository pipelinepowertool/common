package com.pipelinepowertool.common.pipelineplugin.csv;

import com.pipelinepowertool.common.core.database.EnergyReading;
import java.io.File;
import java.io.FileNotFoundException;

public interface CsvService {

    EnergyReading aggregate(File file) throws FileNotFoundException;
}
