package technotic.exchange.sorting;

import technotic.exchange.model.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderSorter {

    public List<Order> sortByPriceThenTime(List<Order> openOrders) {
        return openOrders.stream().sorted().collect(Collectors.toList());
    }
}
