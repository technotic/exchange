package technotic.exchange.store;

import org.junit.Test;
import technotic.exchange.model.Order;

import java.math.BigDecimal;

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
        assertThat(orderStore.getAverageExecutionPrice("VOD.L"), equalTo(BigDecimal.ZERO));
    }

    @Test
    public void shouldReportAverageExecutionPriceRICAsEmptyWhenNoExecutedOrders() {

        // Given
        orderStore.placeOrder(new Order(SELL, 1000, "VOD.L", bd("100.2"), "User1", 1));
        orderStore.placeOrder(new Order(SELL, 500, "VOD.L", bd("100.2"), "User1", 2));

        // Expect
        assertThat(orderStore.getAverageExecutionPrice("VOD.L"), equalTo(BigDecimal.ZERO));
    }

    @Test
    public void shouldReportAverageExecutionPriceRICWhenOneExecutedOrder() {

        // Given
        orderStore.placeOrder(new Order(SELL, 1000, "VOD.L", bd("90.2"), "User1", 1));
        orderStore.placeOrder(new Order(BUY, 1000, "VOD.L", bd("100.2"), "User1", 2));

        // Expect
        assertThat(orderStore.getAverageExecutionPrice("VOD.L"), equalTo(bd("100.2")));
    }

    @Test
    public void shouldReportAverageExecutionPriceRICWhenMultipleExecutedOrders() {

        // Given
        orderStore.placeOrder(new Order(SELL, 1000, "VOD.L", bd("100"), "User1", 1));
        orderStore.placeOrder(new Order(BUY, 1000, "VOD.L", bd("100"), "User1", 2));

        orderStore.placeOrder(new Order(SELL, 500, "VOD.L", bd("200"), "User1", 1));
        orderStore.placeOrder(new Order(BUY, 500, "VOD.L", bd("200"), "User1", 2));

        // Expect
        assertThat(orderStore.getAverageExecutionPrice("VOD.L"), equalTo(bd("150")));
    }
}