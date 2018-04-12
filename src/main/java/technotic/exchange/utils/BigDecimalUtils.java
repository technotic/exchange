package technotic.exchange.utils;

import java.math.BigDecimal;

final public class BigDecimalUtils {

    public static BigDecimal bd(String value) {
        return new BigDecimal(value);
    }

    public static boolean lessThanOrEqual(BigDecimal bd1, BigDecimal bd2) {
        return bd1.compareTo(bd2) <= 0;
    }
}