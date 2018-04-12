package technotic.exchange.store;

import org.junit.Test;
import technotic.exchange.model.OpenInterest;
import technotic.exchange.model.Order;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static technotic.exchange.model.Direction.BUY;
import static technotic.exchange.model.Direction.SELL;
import static technotic.exchange.utils.BigDecimalUtils.bd;

public class OrderStoreAverageExecutionPriceReportTest {

    private OrderStore orderStore = new OrderStore();

    @Test
    public void shouldReportAverageExecutionPriceRICAsEmptyWhenNoOrders() {

        // Expect
        assertThat(orderStore.getAverageExecutionPrice("VOD.L"), equalTo(emptyList()));
    }
}