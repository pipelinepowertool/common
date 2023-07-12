package com.pipelinepowertool.common.core.utils;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

public class TimeUtils {

    public static long secondsBetween(OffsetDateTime startTime, OffsetDateTime endTime) {
        return ChronoUnit.SECONDS.between(startTime, endTime);
    }

}
