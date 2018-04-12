package technotic.exchange.sorting;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import technotic.exchange.model.Order;

import java.util.Collections;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static technotic.exchange.model.Direction.BUY;
import static technotic.exchange.model.Direction.SELL;
import static technotic.exchange.utils.BigDecimalUtils.bd;

public class OrderSorterTest {

    private OrderSorter orderSorter = new OrderSorter();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldSortEmptyListAsEmptyList() {

        // Expect
        assertThat(orderSorter.sortByPriceThenTimeReversed(Collections.emptyList()), equalTo(Collections.emptyList()));
    }

    @Test
    public void shouldSortBuyOrdersBtTimeAscendingWhenPriceIsSame() {

        // Given
        Order o1 = new Order(BUY, 1000, "VOD.L", bd("101.0"), "User1", 2);
        Order o2 = new Order(BUY, 1000, "VOD.L", bd("101.0"), "User1", 3);
        Order o3 = new Order(BUY, 1000, "VOD.L", bd("101.0"), "User1", 4);
        Order o4 = new Order(BUY, 1000, "VOD.L", bd("101.0"), "User1", 1);

        // Expect
        assertThat(orderSorter.sortByPriceThenTimeReversed(asList(o1, o2, o3, o4)), equalTo(asList(o4, o1, o2, o3)));
    }

    @Test
    public void shouldSortBuyOrdersByPriceAscendingThenByTime() {

        // Given
        Order o1 = new Order(BUY, 1000, "VOD.L", bd("100.2"), "User1", 1);
        Order o2 = new Order(BUY, 1000, "VOD.L", bd("101.2"), "User1", 2);
        Order o3 = new Order(BUY, 1000, "VOD.L", bd("101.2"), "User1", 3);
        Order o4 = new Order(BUY, 1000, "VOD.L", bd("100.2"), "User1", 4);

        // Expect
        assertThat(orderSorter.sortByPriceThenTimeReversed(asList(o1, o2, o3, o4)), equalTo(asList(o2, o3, o1, o4)));
    }

    @Test
    public void shouldSortSellOrdersByPriceDescendingThenByTime() {

        // Given
        Order o1 = new Order(SELL, 1000, "VOD.L", bd("101.2"), "User1", 1);
        Order o2 = new Order(SELL, 1000, "VOD.L", bd("101.2"), "User1", 2);
        Order o3 = new Order(SELL, 1000, "VOD.L", bd("101.0"), "User1", 3);
        Order o4 = new Order(SELL, 1000, "VOD.L", bd("100.2"), "User1", 4);

        // Expect
        assertThat(orderSorter.sortByPriceThenTimeReversed(asList(o1, o2, o3, o4)), equalTo(asList(o4, o3, o1, o2)));
    }

    @Test
    public void shouldFailToSortOrdersOfMixedType() {

        // Given
        Order o1 = new Order(BUY, 1000, "VOD.L", bd("101.2"), "User1", 1);
        Order o2 = new Order(SELL, 1000, "VOD.L", bd("101.2"), "User1", 2);

        // Expect
        thrown.expect(IllegalArgumentException.class);

        // When
        orderSorter.sortByPriceThenTimeReversed(asList(o1, o2));
    }

}