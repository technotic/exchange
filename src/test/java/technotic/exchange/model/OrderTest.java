package technotic.exchange.model;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static technotic.exchange.model.Direction.BUY;
import static technotic.exchange.model.Direction.SELL;
import static technotic.exchange.utils.BigDecimalUtils.bd;

public class OrderTest {

    @Test
    public void shouldMatchOrdersWhereBuyPriceEqualToSellPrice() {

        // Given
        Order order1 = new Order(SELL, 1000, "VOD.L", bd("100.2"), "User1");
        Order order2 = new Order(BUY, 1000, "VOD.L", bd("100.2"), "User1");

        // Expect
        assertThat(order1.matches(order2), is(true));
    }

    @Test
    public void shouldMatchOrdersWhereSellPriceLessThanSellPrice() {

        // Given
        Order order1 = new Order(SELL, 1000, "VOD.L", bd("90.2"), "User1");
        Order order2 = new Order(BUY, 1000, "VOD.L", bd("100.2"), "User1");

        // Expect
        assertThat(order1.matches(order2), is(true));
    }

    @Test
    public void shouldNotMatchTwoSellOrders() {

        // Given
        Order order1 = new Order(SELL, 1000, "VOD.L", bd("100.2"), "User1");
        Order order2 = new Order(SELL, 1000, "VOD.L", bd("100.2"), "User1");

        // Expect
        assertThat(order1.matches(order2), is(false));
    }

    @Test
    public void shouldNotMatchTwoBuyOrders() {

        // Given
        Order order1 = new Order(BUY, 1000, "VOD.L", bd("100.2"), "User1");
        Order order2 = new Order(BUY, 1000, "VOD.L", bd("100.2"), "User1");

        // Expect
        assertThat(order1.matches(order2), is(false));
    }

    @Test
    public void shouldNotMatchOrdersForDifferentQuantity() {

        // Given
        Order order1 = new Order(SELL, 1000, "VOD.L", bd("100.2"), "User1");
        Order order2 = new Order(BUY, 900, "VOD.L", bd("100.2"), "User1");

        // Expect
        assertThat(order1.matches(order2), is(false));
    }

    @Test
    public void shouldNotMatchOrdersForDifferentReutersInstrumentCode() {

        // Given
        Order order1 = new Order(SELL, 1000, "VOD.L", bd("100.2"), "User1");
        Order order2 = new Order(BUY, 1000, "VOD.I", bd("100.2"), "User1");

        // Expect
        assertThat(order1.matches(order2), is(false));
    }
}