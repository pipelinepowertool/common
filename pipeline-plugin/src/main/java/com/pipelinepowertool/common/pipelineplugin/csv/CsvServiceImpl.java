package com.pipelinepowertool.common.pipelineplugin.csv;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.bean.CsvToBeanBuilder;
import com.pipelinepowertool.common.core.database.EnergyReading;
import com.pipelinepowertool.common.pipelineplugin.exceptions.NoReadingFoundException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CsvServiceImpl implements CsvService {

    @Override
    public EnergyReading aggregateReadings(File file) throws FileNotFoundException {
        List<EnergyReadingCSVRecord> records = new CsvToBeanBuilder(
            new FileReader(file))
            .withType(EnergyReadingCSVRecord.class)
            .withIgnoreLeadingWhiteSpace(true)
            .build()
            .parse();
        if (records.isEmpty()) {
            throw new NoReadingFoundException();
        }
        return aggregate(records);
    }

    private EnergyReading aggregate(List<EnergyReadingCSVRecord> records) {
        BigDecimal watts = records.stream()
            .map(EnergyReadingCSVRecord::getWatts)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageUtilization = records.stream()
            .map(EnergyReadingCSVRecord::getUtilization)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(new BigDecimal(records.size()), RoundingMode.HALF_UP);

        return new EnergyReading(watts, averageUtilization);
    }

    @Override
    public String convertAggregateToJson(EnergyReading energyReading) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(energyReading);
    }


}
