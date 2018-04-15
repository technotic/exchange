package technotic.exchange.store;

import org.junit.Test;
import technotic.exchange.model.Order;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static technotic.exchange.model.Direction.BUY;
import static technotic.exchange.model.Direction.SELL;
import static technotic.exchange.utils.BigDecimalUtils.bd;

public class OrderStoreExecutedQuantityReportTest {

    private OrderStore orderStore = new SimpleOrderStore();

    @Test
    public void shouldReportExecutedQuantityForRICAndUserAsZeroWhenNoOrders() {

        // Expect
        assertThat(orderStore.executedQuantity("VOD.L", "User1"), equalTo(0));
    }

    @Test
    public void shouldReportExecutedQuantityForRICAndUserAsZeroWhenNoExecutedOrders() {

        // Given
        orderStore.placeOrder(new Order(SELL, 1000, "VOD.L", bd("100.2"), "User1", 1));
        orderStore.placeOrder(new Order(SELL, 500, "VOD.L", bd("100.2"), "User1", 2));

        // Expect
        assertThat(orderStore.executedQuantity("VOD.L", "User1"), equalTo(0));
    }

    @Test
    public void shouldReportExecutedQuantityForRICAndUserWhenOneExecutedOrder() {

        // Given
        orderStore.placeOrder(new Order(SELL, 1000, "VOD.L", bd("100.2"), "User1", 1));
        orderStore.placeOrder(new Order(BUY, 1000, "VOD.L", bd("100.2"), "User2", 2));

        // Expect
        assertThat(orderStore.executedQuantity("VOD.L", "User1"), equalTo(-1000));
        assertThat(orderStore.executedQuantity("VOD.L", "User2"), equalTo(1000));
    }

    @Test
    public void shouldReportExecutedQuantityForRICAndUserWhenMultipleExecutedOrders() {

        // Given
        orderStore.placeOrder(new Order(SELL, 1000, "VOD.L", bd("100.2"), "User1", 1));
        orderStore.placeOrder(new Order(BUY, 1000, "VOD.L", bd("100.2"), "User2", 2));

        orderStore.placeOrder(new Order(SELL, 500, "VOD.L", bd("100.2"), "User3", 1));
        orderStore.placeOrder(new Order(BUY, 500, "VOD.L", bd("100.2"), "User1", 2));

        orderStore.placeOrder(new Order(SELL, 1200, "VOD.L", bd("100.2"), "User1", 1));
        orderStore.placeOrder(new Order(BUY, 1200, "VOD.L", bd("100.2"), "User3", 2));

        // Expect
        assertThat(orderStore.executedQuantity("VOD.L", "User1"), equalTo(-1700));
        assertThat(orderStore.executedQuantity("VOD.L", "User2"), equalTo(1000));
        assertThat(orderStore.executedQuantity("VOD.L", "User3"), equalTo(700));
    }
}