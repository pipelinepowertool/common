package com.pipelinepowertool.common.pipelineplugin.csv;

import com.opencsv.bean.CsvBindByPosition;
import java.math.BigDecimal;

class EnergyReadingCSVRecord {

    @CsvBindByPosition(position = 0)
    private Integer pid;

    @CsvBindByPosition(position = 1)
    private BigDecimal watts;

    @CsvBindByPosition(position = 2)
    private BigDecimal utilization;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public BigDecimal getWatts() {
        return watts;
    }

    public void setWatts(BigDecimal watts) {
        this.watts = watts;
    }

    public BigDecimal getUtilization() {
        return utilization;
    }

    public void setUtilization(BigDecimal utilization) {
        this.utilization = utilization;
    }
}
