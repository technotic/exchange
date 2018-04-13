package technotic.exchange.acceptance;

import org.junit.Test;
import technotic.exchange.model.Direction;
import technotic.exchange.model.OpenInterest;
import technotic.exchange.model.Order;
import technotic.exchange.store.OrderStore;

import static java.util.stream.Collectors.joining;
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

        verifyOpenInterest("VOD.L", BUY, "");
        verifyOpenInterest("VOD.L", SELL, "1000 @ 100.2");
        verifyAverageExecutionPrice("VOD.L", "0");
        verifyExecutedQuantity("VOD.L", "User1", 0);
        verifyExecutedQuantity("VOD.L", "User2", 0);

        placeOrder(BUY, 1000, "VOD.L", "100.2", "User2", 2);

        verifyOpenInterest("VOD.L", BUY, "");
        verifyOpenInterest("VOD.L", SELL, "");
        verifyAverageExecutionPrice("VOD.L", "100.2000");
        verifyExecutedQuantity("VOD.L", "User1", -1000);
        verifyExecutedQuantity("VOD.L", "User2", 1000);

        placeOrder(BUY, 1000, "VOD.L", "99", "User1", 3);

        verifyOpenInterest("VOD.L", BUY, "1000 @ 99");
        verifyOpenInterest("VOD.L", SELL, "");
        verifyAverageExecutionPrice("VOD.L", "100.2000");
        verifyExecutedQuantity("VOD.L", "User1", -1000);
        verifyExecutedQuantity("VOD.L", "User2", 1000);

        placeOrder(BUY, 1000, "VOD.L", "101", "User1", 4);

        verifyOpenInterest("VOD.L", BUY, "1000 @ 99, 1000 @ 101");
        verifyOpenInterest("VOD.L", SELL, "");
        verifyOpenInterest("VOD.L", BUY, "1000 @ 99, 1000 @ 101");
        verifyOpenInterest("VOD.L", SELL, "");
        verifyAverageExecutionPrice("VOD.L", "100.2000");
        verifyExecutedQuantity("VOD.L", "User1", -1000);
        verifyExecutedQuantity("VOD.L", "User2", 1000);

        placeOrder(SELL, 500, "VOD.L", "102", "User2", 5);

        verifyOpenInterest("VOD.L", BUY, "1000 @ 99, 1000 @ 101");
        verifyOpenInterest("VOD.L", SELL, "500 @ 102");
        verifyAverageExecutionPrice("VOD.L", "100.2000");
        verifyExecutedQuantity("VOD.L", "User1", -1000);
        verifyExecutedQuantity("VOD.L", "User2", 1000);

        placeOrder(BUY, 500, "VOD.L", "103", "User1", 6);

        verifyOpenInterest("VOD.L", BUY, "1000 @ 99, 1000 @ 101");
        verifyOpenInterest("VOD.L", SELL, "");
        verifyAverageExecutionPrice("VOD.L", "101.1333");
        verifyExecutedQuantity("VOD.L", "User1", -500);
        verifyExecutedQuantity("VOD.L", "User2", 500);

        placeOrder(SELL, 1000, "VOD.L", "98", "User2", 7);

        verifyOpenInterest("VOD.L", BUY, "1000 @ 99");
        verifyOpenInterest("VOD.L", SELL, "");
        verifyAverageExecutionPrice("VOD.L", "99.8800");
        verifyExecutedQuantity("VOD.L", "User1", 500);
        verifyExecutedQuantity("VOD.L", "User2", -500);
    }

    private void verifyOpenInterest(String ric, Direction direction, String expectedValues) {
        assertThat(orderStore.openInterest(ric, direction).stream().map(OpenInterest::toString).collect(joining(", ")), equalTo(expectedValues));
    }

    private void verifyAverageExecutionPrice(String ric, String expectedValue) {
        assertThat(orderStore.averageExecutionPrice(ric), equalTo(bd(expectedValue)));
    }

    private void verifyExecutedQuantity(String ric, String user, int expectedValue) {
        assertThat(orderStore.executedQuantity(ric, user), equalTo(expectedValue));
    }

    private void placeOrder(Direction direction, int quantity, String ric, String price, String user, long timestamp) {
        orderStore.placeOrder(new Order(direction, quantity, ric, bd(price), user, timestamp));
    }

    private OpenInterest openInterest(int totalOrderQuantity, String orderPrice) {
        return new OpenInterest(totalOrderQuantity, bd(orderPrice));
    }
}
