package com.pipelinepowertool.common.pipelineplugin.csv;

import com.opencsv.bean.CsvToBeanBuilder;
import com.pipelinepowertool.common.core.database.EnergyReading;
import com.pipelinepowertool.common.pipelineplugin.exceptions.NoReadingFoundException;

import java.io.Reader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CsvServiceImpl implements CsvService {

    @Override
    public EnergyReading aggregateReadings(Reader reader) {
        List<EnergyReadingCSVRecord> records = new CsvToBeanBuilder(
                reader)
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


}
