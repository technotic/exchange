package technotic.exchange.store;

import technotic.exchange.model.Order;
import technotic.exchange.sorting.OrderSorter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableList;

public class OrderStore {

    private List<Order> openOrders = new ArrayList<>();

    public boolean placeOrder(Order order) {
        List<Order> matchedOrders = findMatch(order);
        if (matchedOrders.size() == 0) {
            openOrders.add(order);
            return false;
        } else if (matchedOrders.size() == 1) {
            openOrders.remove(matchedOrders.get(0));
            return true;
        } else {
            // TODO how to inject?
            List<Order> ordersSortedByPriceThenTime = new OrderSorter().sortByPriceThenTimeReversed(openOrders);
            openOrders.remove(ordersSortedByPriceThenTime.get(0));
            return true;
        }
    }

    public List<Order> getOpenOrders() {
        return unmodifiableList(openOrders);
    }

    private List<Order> findMatch(Order orderToMatch) {
        return openOrders.stream().filter(order -> order.matches(orderToMatch)).collect(Collectors.toList());
    }
}
