package technotic.exchange.sorting;

import technotic.exchange.model.Order;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OrderSorter {

    public List<Order> sortByPriceThenTime(List<Order> orders) {
        if (orders.isEmpty()) {
            return Collections.emptyList();
        }
        assertAllOrdersOfSameDirection(orders);
        Comparator<Order> comparator = orders.get(0).getDirection().getComparator();
        return orders.stream().sorted(comparator).collect(Collectors.toList());
    }

    private void assertAllOrdersOfSameDirection(List<Order> orders) {
        Order firstOrder = orders.get(0);
        boolean allSameType = orders.stream().allMatch(order -> order.getDirection() == firstOrder.getDirection());
        if (!allSameType) {
            throw new IllegalArgumentException("Expected all orders to have the same direction in " + orders);
        }
    }
}
