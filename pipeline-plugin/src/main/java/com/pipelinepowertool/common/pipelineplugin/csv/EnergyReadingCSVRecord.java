package com.pipelinepowertool.common.pipelineplugin.csv;

import com.opencsv.bean.CsvBindByName;

import java.math.BigDecimal;

public class EnergyReadingCSVRecord {

    @CsvBindByName(column = "PID")
    private Integer pid;

    @CsvBindByName(column = "Joules")
    private BigDecimal joules;

    @CsvBindByName(column = "Utilization")
    private BigDecimal utilization;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public BigDecimal getJoules() {
        return joules;
    }

    public void setJoules(BigDecimal joules) {
        this.joules = joules;
    }

    public BigDecimal getUtilization() {
        return utilization;
    }

    public void setUtilization(BigDecimal utilization) {
        this.utilization = utilization;
    }
}
