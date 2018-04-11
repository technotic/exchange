package technotic.exchange.utils;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static technotic.exchange.utils.BigDecimalUtils.bd;
import static technotic.exchange.utils.BigDecimalUtils.lessThanOrEqual;

public class BigDecimalUtilsTest {

    @Test
    public void shouldCreateNewBigDecimals() {

        // Expect
        assertThat(bd("100.01"), equalTo(new BigDecimal("100.01")));
    }

    @Test
    public void shouldCompareBigDecimals() {

        // Expect
        assertThat(lessThanOrEqual(bd("100.01"), bd("100.01")), is(true));
        assertThat(lessThanOrEqual(bd("90.01"), bd("100.01")), is(true));
        assertThat(lessThanOrEqual(bd("100.01"), bd("90.01")), is(false));
    }

}
