package technotic.exchange.store;

import technotic.exchange.model.Direction;
import technotic.exchange.model.OpenInterest;
import technotic.exchange.model.Order;
import technotic.exchange.sorting.OrderSorter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.summingInt;

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
            List<Order> ordersSortedByPriceThenTime = new OrderSorter().sortByPriceThenTime(openOrders);
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

    public List<OpenInterest> openInterest(String reutersInstrumentCode, Direction direction) {
        Map<BigDecimal, Integer> filteredOrdersGroupedByPrice = openOrders
                .stream()
                .filter(order -> order.getDirection() == direction && order.getReutersInstrumentCode().equals(reutersInstrumentCode))
                .collect(Collectors.groupingBy(Order::getPrice, summingInt(Order::getQuantity)));

        return filteredOrdersGroupedByPrice
                .keySet()
                .stream()
                .sorted()
                .map(price -> new OpenInterest(filteredOrdersGroupedByPrice.get(price), price))
                .collect(Collectors.toList());
    }

    public BigDecimal getAverageExecutionPrice(String reutersInstrumentCode) {
        return BigDecimal.ZERO;
    }
}
