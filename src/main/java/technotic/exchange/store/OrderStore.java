package technotic.exchange.store;

import technotic.exchange.model.Direction;
import technotic.exchange.model.OpenInterest;
import technotic.exchange.model.Order;

import java.math.BigDecimal;
import java.util.List;

public interface OrderStore {

    boolean placeOrder(Order order);

    List<Order> getOpenOrders();

    List<Order> getExecutedOrders();

    List<OpenInterest> openInterest(String reutersInstrumentCode, Direction direction);

    BigDecimal averageExecutionPrice(String reutersInstrumentCode);

    int executedQuantity(String reutersInstrumentCode, String user);
}
