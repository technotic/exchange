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

public class OrderStoreOpenInterestReportTest {

    private OrderStore orderStore = new SimpleOrderStore();

    @Test
    public void shouldReportOpenInterestForRICandDirectionAsEmptyWhenNoOrders() {

        // When
        orderStore.openInterest("VOD.L", SELL);

        // Then
        assertThat(orderStore.getOpenOrders(), equalTo(emptyList()));
    }

    @Test
    public void shouldReportOpenInterestForRICandDirectionForSingleOrder() {

        // Given
        Order originalOrder1 = new Order(SELL, 1000, "VOD.L", bd("100.2"), "User1", 1);
        orderStore.placeOrder(originalOrder1);

        // When
        List<OpenInterest> openInterest = orderStore.openInterest("VOD.L", SELL);

        // Then
        assertThat(openInterest, equalTo(asList(new OpenInterest(1000, bd("100.2")))));
    }

    @Test
    public void shouldReportOpenInterestForRICandDirectionForMultipleOrders() {

        // Given
        orderStore.placeOrder(new Order(SELL, 1000, "VOD.L", bd("100.2"), "User1", 1));
        orderStore.placeOrder(new Order(SELL, 500, "VOD.L", bd("100.2"), "User1", 1));
        orderStore.placeOrder(new Order(SELL, 1000, "VOD.L", bd("90.2"), "User1", 1));
        orderStore.placeOrder(new Order(SELL, 500, "VOD.L", bd("100.2"), "User1", 1));
        orderStore.placeOrder(new Order(SELL, 400, "VOD.L", bd("90.2"), "User1", 1));
        // these should be excluded
        orderStore.placeOrder(new Order(BUY, 1100, "VOD.L", bd("100.0"), "User1", 1));
        orderStore.placeOrder(new Order(SELL, 400, "VOD.X", bd("100.0"), "User1", 1));

        // When
        List<OpenInterest> openInterest = orderStore.openInterest("VOD.L", SELL);

        // Then
        assertThat(openInterest, equalTo(
                asList(
                        new OpenInterest(1400, bd("90.2")),
                        new OpenInterest(2000, bd("100.2")))));
    }
}