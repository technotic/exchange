package technotic.exchange.store;

import org.junit.Test;
import technotic.exchange.model.Order;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;
import static technotic.exchange.model.Direction.BUY;
import static technotic.exchange.model.Direction.SELL;
import static technotic.exchange.utils.BigDecimalUtils.bd;

public class OrderStoreMatchTest {

    private OrderStore orderStore = new SimpleOrderStore();

    @Test
    public void shouldNotMatchInitialOrder() {

        // Given
        Order order = new Order(SELL, 1000, "VOD.L", bd("100.2"), "User1", 0);

        // When
        boolean matched = orderStore.placeOrder(order);

        // Then
        assertThat(matched, is(false));
        assertThat(orderStore.getOpenOrders(), equalTo(asList(order)));
    }

    @Test
    public void shouldNotMatchMultipleSellOrders() {

        // Given
        Order originalOrder = new Order(SELL, 1000, "VOD.L", bd("100.2"), "User1", 0);
        orderStore.placeOrder(originalOrder);

        Order order = new Order(SELL, 1000, "VOD.L", bd("100.2"), "User1", 0);

        // When
        boolean matched = orderStore.placeOrder(order);

        // Then
        assertThat(matched, is(false));
        assertThat(orderStore.getOpenOrders(), equalTo(asList(originalOrder, order)));
    }

    @Test
    public void shouldNotMatchMultipleBuyOrders() {

        // Given
        Order originalOrder = new Order(BUY, 1000, "VOD.L", bd("100.2"), "User1", 0);
        orderStore.placeOrder(originalOrder);

        Order order = new Order(BUY, 1000, "VOD.L", bd("100.2"), "User1", 0);

        // When
        boolean matched = orderStore.placeOrder(order);

        // Then
        assertThat(matched, is(false));
        assertThat(orderStore.getOpenOrders(), equalTo(asList(originalOrder, order)));
    }

    @Test
    public void shouldNotMatchBuyAndSellOrdersWithDifferentRICs() {

        // Given
        Order originalOrder = new Order(BUY, 1000, "VOD.L", bd("100.2"), "User1", 0);
        orderStore.placeOrder(originalOrder);

        Order order = new Order(SELL, 1000, "VOD.X", bd("100.2"), "User1", 0);

        // When
        boolean matched = orderStore.placeOrder(order);

        // Then
        assertThat(matched, is(false));
        assertThat(orderStore.getOpenOrders(), equalTo(asList(originalOrder, order)));
    }

    @Test
    public void shouldNotMatchBuyAndSellOrdersWithDifferentQuantities() {

        // Given
        Order originalOrder = new Order(BUY, 1000, "VOD.L", bd("100.2"), "User1", 0);
        orderStore.placeOrder(originalOrder);

        Order order = new Order(SELL, 900, "VOD.L", bd("100.2"), "User1", 0);

        // When
        boolean matched = orderStore.placeOrder(order);

        // Then
        assertThat(matched, is(false));
        assertThat(orderStore.getOpenOrders(), equalTo(asList(originalOrder, order)));
    }

    @Test
    public void shouldNotMatchBuyAndSellOrdersWhenBuyPriceIsHigherThanSellPrice() {

        // Given
        Order originalOrder = new Order(BUY, 1000, "VOD.L", bd("90.2"), "User1", 0);
        orderStore.placeOrder(originalOrder);

        Order order = new Order(SELL, 1000, "VOD.L", bd("100.2"), "User1", 0);

        // When
        boolean matched = orderStore.placeOrder(order);

        // Then
        assertThat(matched, is(false));
        assertThat(orderStore.getOpenOrders(), equalTo(asList(originalOrder, order)));
    }

    @Test
    public void shouldMatchBuyWithNewSellOrderWithEqualRICsAndQuantitiesAndBuyPriceGreaterThanOrEqualToSellPrice() {

        // Given
        Order originalOrder = new Order(BUY, 1000, "VOD.L", bd("100.2"), "User1", 0);
        orderStore.placeOrder(originalOrder);

        Order order = new Order(SELL, 1000, "VOD.L", bd("100.2"), "User2", 0);

        // When
        boolean matched = orderStore.placeOrder(order);

        // Then
        assertThat( matched, is(true));
        assertThat(orderStore.getOpenOrders(), is(empty()));
    }

    @Test
    public void shouldMatchSellWithNewBuyOrderWithEqualRICsAndQuantitiesAndBuyPriceGreaterThanOrEqualToSellPrice() {

        // Given
        Order originalOrder = new Order(SELL, 1000, "VOD.L", bd("100.2"), "User1", 0);
        orderStore.placeOrder(originalOrder);

        Order order = new Order(BUY, 1000, "VOD.L", bd("100.2"), "User2", 0);

        // When
        boolean matched = orderStore.placeOrder(order);

        // Then
        assertThat(matched, is(true));
        assertThat(orderStore.getOpenOrders(), is(empty()));
    }

    @Test
    public void shouldMatchAgainstEarliestExistingOrderWithSamePrice() {

        // Given
        Order originalOrder1 = new Order(SELL, 1000, "VOD.L", bd("100.2"), "User1", 1);
        orderStore.placeOrder(originalOrder1);
        Order originalOrder2 = new Order(SELL, 1000, "VOD.L", bd("100.2"), "User1", 2);
        orderStore.placeOrder(originalOrder2);

        Order order = new Order(BUY, 1000, "VOD.L", bd("100.2"), "User1", 3);

        // When
        boolean matched = orderStore.placeOrder(order);

        // Then
        assertThat(matched, is(true));
        assertThat(orderStore.getOpenOrders(), equalTo(asList(originalOrder2)));
    }

    @Test
    public void shouldMatchSellAgainstOrderWithHighestPriceWhenMultipleMatches() {

        // Given
        Order originalOrder1 = new Order(BUY, 1000, "VOD.L", bd("100.2"), "User1", 1);
        orderStore.placeOrder(originalOrder1);
        Order originalOrder2 = new Order(BUY, 1000, "VOD.L", bd("110.2"), "User1", 2);
        orderStore.placeOrder(originalOrder2);

        Order order = new Order(SELL, 1000, "VOD.L", bd("100.2"), "User1", 3);

        // When
        boolean matched = orderStore.placeOrder(order);

        // Then
        assertThat(matched, is(true));
        assertThat(orderStore.getOpenOrders(), equalTo(asList(originalOrder1)));
    }

    @Test
    public void shouldMatchBuyAgainstOrderWithLowestPriceWhenMultipleMatches() {

        // Given
        Order originalOrder1 = new Order(SELL, 1000, "VOD.L", bd("100.2"), "User1", 1);
        orderStore.placeOrder(originalOrder1);
        Order originalOrder2 = new Order(SELL, 1000, "VOD.L", bd("110.2"), "User1", 2);
        orderStore.placeOrder(originalOrder2);

        Order order = new Order(BUY, 1000, "VOD.L", bd("100.2"), "User1", 3);

        // When
        boolean matched = orderStore.placeOrder(order);

        // Then
        assertThat(matched, is(true));
        assertThat(orderStore.getOpenOrders(), equalTo(asList(originalOrder2)));
    }
}