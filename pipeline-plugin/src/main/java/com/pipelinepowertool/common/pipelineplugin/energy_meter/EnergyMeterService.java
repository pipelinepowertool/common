package com.pipelinepowertool.common.pipelineplugin.energy_meter;

import com.pipelinepowertool.common.core.pipeline.Pipeline;
import java.io.IOException;

public interface EnergyMeterService {

    void start(Pipeline pipeline) throws IOException;

    void stop();
}
