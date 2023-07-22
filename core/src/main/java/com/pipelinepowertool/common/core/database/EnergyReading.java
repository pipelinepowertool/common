package com.pipelinepowertool.common.core.database;

import java.math.BigDecimal;

public class EnergyReading {

    private final BigDecimal joules;

    private final BigDecimal utilization;


    public EnergyReading(BigDecimal joules, BigDecimal utilization) {
        this.joules = joules;
        this.utilization = utilization;
    }

    public BigDecimal getJoules() {
        return joules;
    }

    public BigDecimal getUtilization() {
        return utilization;
    }
}
