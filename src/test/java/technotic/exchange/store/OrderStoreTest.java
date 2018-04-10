package technotic.exchange.store;

import org.junit.Test;
import technotic.exchange.model.*;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static technotic.exchange.model.Direction.SELL;

public class OrderStoreTest {

    private OrderStore orderStore = new OrderStore();

    private User user1 = new User("User1");

    @Test
    public void shouldAddOrder() {

        // Given
        Order order = new Order(SELL, new OrderQuantity(1000), new ReutersInstrumentCode("VOD.L"), new OrderPrice("100.2"), user1);

        // When
        orderStore.add(order);

        // Then
        assertThat(orderStore.getOpenOrders(user1), equalTo(Collections.<Order>emptyList()));
    }
}