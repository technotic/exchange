package technotic.exchange.acceptance;

import org.junit.Test;
import technotic.exchange.model.Direction;
import technotic.exchange.model.OpenInterest;
import technotic.exchange.model.Order;
import technotic.exchange.store.OrderStore;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static technotic.exchange.model.Direction.BUY;
import static technotic.exchange.model.Direction.SELL;
import static technotic.exchange.utils.BigDecimalUtils.bd;

public class AcceptanceTest {

    private OrderStore orderStore = new OrderStore();

    @Test
    public void acceptanceTest() {

        placeOrder(SELL, 1000, "VOD.L", "100.2", "User1", 1);

        assertThat(orderStore.openInterest("VOD.L", SELL), equalTo(asList(openInterest(1000, "100.2"))));
        assertThat(orderStore.averageExecutionPrice("VOD.L"), equalTo(bd("0")));

        placeOrder(BUY, 1000, "VOD.L", "100.2", "User2", 2);

        assertThat(orderStore.openInterest("VOD.L", SELL), equalTo(emptyList()));
        assertThat(orderStore.averageExecutionPrice("VOD.L"), equalTo(bd("100.2000")));
        assertThat(orderStore.executedQuantity("VOD.L", "User1"), equalTo(-1000));
        assertThat(orderStore.executedQuantity("VOD.L", "User2"), equalTo(1000));

        placeOrder(BUY, 1000, "VOD.L", "99", "User1", 3);

        assertThat(orderStore.openInterest("VOD.L", BUY), equalTo(asList(openInterest(1000, "99"))));
        assertThat(orderStore.averageExecutionPrice("VOD.L"), equalTo(bd("100.2000")));
        assertThat(orderStore.executedQuantity("VOD.L", "User1"), equalTo(-1000));
        assertThat(orderStore.executedQuantity("VOD.L", "User2"), equalTo(1000));

        placeOrder(BUY, 1000, "VOD.L", "101", "User1", 4);

        assertThat(orderStore.openInterest("VOD.L", BUY), equalTo(
                asList(
                        openInterest(1000, "99"),
                        openInterest(1000, "101"))
        ));
        assertThat(orderStore.averageExecutionPrice("VOD.L"), equalTo(bd("100.2000")));
        assertThat(orderStore.executedQuantity("VOD.L", "User1"), equalTo(-1000));
        assertThat(orderStore.executedQuantity("VOD.L", "User2"), equalTo(1000));

        placeOrder(SELL, 500, "VOD.L", "102", "User2", 5);

        assertThat(orderStore.openInterest("VOD.L", BUY), equalTo(
                asList(
                        openInterest(1000, "99"),
                        openInterest(1000, "101"))
        ));
        assertThat(orderStore.openInterest("VOD.L", SELL), equalTo(
                asList(
                        openInterest(500, "102"))
        ));
        assertThat(orderStore.averageExecutionPrice("VOD.L"), equalTo(bd("100.2000")));
        assertThat(orderStore.executedQuantity("VOD.L", "User1"), equalTo(-1000));
        assertThat(orderStore.executedQuantity("VOD.L", "User2"), equalTo(1000));

        placeOrder(BUY, 500, "VOD.L", "103", "User1", 6);

        assertThat(orderStore.openInterest("VOD.L", BUY), equalTo(
                asList(
                        openInterest(1000, "99"),
                        openInterest(1000, "101"))
        ));
        assertThat(orderStore.openInterest("VOD.L", SELL), equalTo(emptyList()));
        assertThat(orderStore.averageExecutionPrice("VOD.L"), equalTo(bd("101.1333")));
        assertThat(orderStore.executedQuantity("VOD.L", "User1"), equalTo(-500));
        assertThat(orderStore.executedQuantity("VOD.L", "User2"), equalTo(500));

        placeOrder(SELL, 1000, "VOD.L", "98", "User2", 7);

        assertThat(orderStore.openInterest("VOD.L", BUY), equalTo(
                asList(
                        openInterest(1000, "99"))
        ));
        assertThat(orderStore.openInterest("VOD.L", SELL), equalTo(emptyList()));
        assertThat(orderStore.averageExecutionPrice("VOD.L"), equalTo(bd("99.8800")));
        assertThat(orderStore.executedQuantity("VOD.L", "User1"), equalTo(500));
        assertThat(orderStore.executedQuantity("VOD.L", "User2"), equalTo(-500));
    }

    private void placeOrder(Direction direction, int quantity, String ric, String price, String user, long timestamp) {
        orderStore.placeOrder(new Order(direction, quantity, ric, bd(price), user, timestamp));
    }

    private OpenInterest openInterest(int totalOrderQuantity, String orderPrice) {
        return new OpenInterest(totalOrderQuantity, bd(orderPrice));
    }
}
