package com.pipelinepowertool.common.pipelineplugin.energy_meter;

import java.io.File;
import java.io.IOException;

public interface EnergyMeterService {

    File download() throws IOException;

    Process start(File file) throws IOException;
}
