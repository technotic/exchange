package technotic.exchange.matchers;

import technotic.exchange.model.Order;
import technotic.exchange.sorting.OrderSorter;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderMatcher {

    public Optional<Order> findMatch(Order orderToMatch, Set<Order> candidateOrders) {

        List<Order> matchedOrders = candidateOrders.stream().filter(order -> order.matches(orderToMatch)).collect(Collectors.toList());

        if (matchedOrders.size() == 0) {
            return Optional.empty();

        } else if (matchedOrders.size() == 1) {
            return Optional.of(matchedOrders.get(0));

        } else {
            List<Order> ordersSortedByPriceThenTime = new OrderSorter().sortByPriceThenTime(matchedOrders);
            return Optional.of(ordersSortedByPriceThenTime.get(0));
        }
    }
}
