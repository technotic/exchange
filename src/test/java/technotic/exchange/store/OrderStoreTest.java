package technotic.exchange.store;

import org.junit.Test;
import technotic.exchange.model.*;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static technotic.exchange.model.Direction.BUY;
import static technotic.exchange.model.Direction.SELL;
import static technotic.exchange.utils.BigDecimalUtils.bd;

public class OrderStoreTest {

    private OrderStore orderStore = new OrderStore();

    @Test
    public void shouldNotMatchInitialOrder() {

        // Given
        Order order = new Order(SELL, 1000, "VOD.L", bd("100.2"), "User1");

        // When
        Optional<Order> executedOrder = orderStore.placeOrder(order);

        // Then
        assertThat(executedOrder.isPresent(), is(false));
    }

    @Test
    public void shouldNotMatchMultipleSellOrders() {

        // Given
        Order order1 = new Order(SELL, 1000, "VOD.L", bd("100.2"), "User1");
        orderStore.placeOrder(order1);

        Order order2 = new Order(SELL, 1000, "VOD.L", bd("100.2"), "User1");

        // When
        Optional<Order> executedOrder = orderStore.placeOrder(order2);

        // Then
        assertThat(executedOrder.isPresent(), is(false));
    }

    @Test
    public void shouldNotMatchMultipleBuyOrders() {

        // Given
        Order order1 = new Order(BUY, 1000, "VOD.L", bd("100.2"), "User1");
        orderStore.placeOrder(order1);

        Order order2 = new Order(BUY, 1000, "VOD.L", bd("100.2"), "User1");

        // When
        Optional<Order> executedOrder = orderStore.placeOrder(order2);

        // Then
        assertThat(executedOrder.isPresent(), is(false));
    }

    @Test
    public void shouldNotMatchBuyAndSellOrdersWithDifferentRICs() {

        // Given
        Order order1 = new Order(BUY, 1000, "VOD.L", bd("100.2"), "User1");
        orderStore.placeOrder(order1);

        Order order2 = new Order(SELL, 1000, "VOD.X", bd("100.2"), "User1");

        // When
        Optional<Order> executedOrder = orderStore.placeOrder(order2);

        // Then
        assertThat(executedOrder.isPresent(), is(false));
    }

    @Test
    public void shouldNotMatchBuyAndSellOrdersWithDifferentQuantities() {

        // Given
        Order order1 = new Order(BUY, 1000, "VOD.L", bd("100.2"), "User1");
        orderStore.placeOrder(order1);

        Order order2 = new Order(SELL, 900, "VOD.L", bd("100.2"), "User1");

        // When
        Optional<Order> executedOrder = orderStore.placeOrder(order2);

        // Then
        assertThat(executedOrder.isPresent(), is(false));
    }

    @Test
    public void shouldNotMatchBuyAndSellOrdersWhenBuyPriceIsHigherThanSellPrice() {

        // Given
        Order order1 = new Order(BUY, 1000, "VOD.L", bd("90.2"), "User1");
        orderStore.placeOrder(order1);

        Order order2 = new Order(SELL, 1000, "VOD.L", bd("100.2"), "User1");

        // When
        Optional<Order> executedOrder = orderStore.placeOrder(order2);

        // Then
        assertThat(executedOrder.isPresent(), is(false));
    }

    @Test
    public void shouldMatchBuyWithNewSellOrderWithEqualRICsAndQuantitiesAndBuyPriceGreaterThanOrEqualToSellPrice() {

        // Given
        Order order1 = new Order(BUY, 1000, "VOD.L", bd("100.2"), "User1");
        orderStore.placeOrder(order1);

        Order order2 = new Order(SELL, 1000, "VOD.L", bd("100.2"), "User2");

        // When
        Optional<Order> executedOrder = orderStore.placeOrder(order2);

        // Then the matched order is the newly added order
        assertThat("No match found", executedOrder.isPresent(), is(true));
        assertThat(executedOrder.get(), equalTo(order2));
    }

    @Test
    public void shouldMatchSellWithNewBuyOrderWithEqualRICsAndQuantitiesAndBuyPriceGreaterThanOrEqualToSellPrice() {

        // Given
        Order order1 = new Order(SELL, 1000, "VOD.L", bd("100.2"), "User1");
        orderStore.placeOrder(order1);

        Order order2 = new Order(BUY, 1000, "VOD.L", bd("100.2"), "User2");

        // When
        Optional<Order> executedOrder = orderStore.placeOrder(order2);

        // Then the matched order is the newly added order
        assertThat("No match found", executedOrder.isPresent(), is(true));
        assertThat(executedOrder.get(), equalTo(order2));
    }
}