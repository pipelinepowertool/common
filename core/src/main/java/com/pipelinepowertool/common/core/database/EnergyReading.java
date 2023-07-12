package com.pipelinepowertool.common.core.database;

import java.math.BigDecimal;

public class EnergyReading {

    private final BigDecimal watts;

    private final BigDecimal utilization;


    public EnergyReading(BigDecimal watts, BigDecimal utilization) {
        this.watts = watts;
        this.utilization = utilization;
    }

    public BigDecimal getWatts() {
        return watts;
    }

    public BigDecimal getUtilization() {
        return utilization;
    }
}
