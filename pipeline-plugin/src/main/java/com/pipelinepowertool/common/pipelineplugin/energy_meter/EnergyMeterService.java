package com.pipelinepowertool.common.pipelineplugin.energy_meter;

import java.io.File;
import java.io.IOException;

public interface EnergyMeterService {

    void start() throws IOException;

    void stop();
}
