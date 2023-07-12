package com.pipelinepowertool.common.core.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils {

    public static BigDecimal doubleToBigDecimal(double doubleVal) {
        return BigDecimal.valueOf(doubleVal).setScale(2, RoundingMode.HALF_UP);
    }

}
