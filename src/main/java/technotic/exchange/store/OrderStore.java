package technotic.exchange.store;

import technotic.exchange.model.Direction;
import technotic.exchange.model.OpenOrderInterest;
import technotic.exchange.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderStore {

    private List<Order> openOrders = new ArrayList<>();

    public Optional<Order> placeOrder(Order order) {
        List<Order> matchedOrders = findMatch(order);
        if (matchedOrders.size() == 0) {
            openOrders.add(order);
            return Optional.empty();
        } else {
            return Optional.of(order);
        }
    }

    private List<Order> findMatch(Order orderToMatch) {
        return openOrders.stream().filter(order -> order.matches(orderToMatch)).collect(Collectors.toList());
    }

//    public List<OpenOrderInterest> getOpenInterest(String reutersInstrumentCode, Direction direction) {
//        return null;
////        return openOrders.stream().filter(order -> order.getReutersInstrumentCode().equals(reutersInstrumentCode) && order.getDirection().equals(direction))
////                .collect(Collectors.toList());
//    }
}
