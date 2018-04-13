package technotic.exchange.model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static technotic.exchange.utils.BigDecimalUtils.bd;

public class OpenInterestTest {

    @Test
    public void shouldSummariseAsString() {

        // Expect
        assertThat(new OpenInterest(1000, bd("100.2")).toString(), equalTo("1000 @ 100.2"));
    }
}