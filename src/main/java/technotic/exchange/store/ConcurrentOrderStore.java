package technotic.exchange.store;

import technotic.exchange.model.Direction;
import technotic.exchange.model.Execution;
import technotic.exchange.model.OpenInterest;
import technotic.exchange.model.Order;
import technotic.exchange.sorting.OrderSorter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.summingInt;

public class ConcurrentOrderStore {

    private ConcurrentHashMap<String, OrderSomething> orderStore;

    public boolean placeOrder(Order order) {

        readWriteLock.writeLock().lock();

        try {
            List<Order> matchedOrders = findMatch(order);

            if (matchedOrders.size() == 0) {
                openOrders.add(order);
                return false;

            } else if (matchedOrders.size() == 1) {
                executeOrder(order, matchedOrders.get(0));
                return true;

            } else {
                List<Order> ordersSortedByPriceThenTime = new OrderSorter().sortByPriceThenTime(openOrders);
                executeOrder(order, ordersSortedByPriceThenTime.get(0));
                return true;
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public List<Order> getOpenOrders() {
        readWriteLock.readLock().lock();
        try {
            return new ArrayList<>(openOrders);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public List<Order> getExecutedOrders() {
        return executions.stream().map(Execution::getExecutedOrder).collect(Collectors.toList());
    }

    private void executeOrder(Order executedOrder, Order matchedOrder) {
//        openOrders.remove(matchedOrder);
        executions.add(new Execution(executedOrder, matchedOrder));
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

    public BigDecimal averageExecutionPrice(String reutersInstrumentCode) {
        List<Order> executedOrdersForRIC = executions
                .stream()
                .map(Execution::getExecutedOrder)
                .filter(order -> order.getReutersInstrumentCode().equals(reutersInstrumentCode))
                .collect(Collectors.toList());

        if (executedOrdersForRIC.isEmpty()) {
            return BigDecimal.ZERO;
        } else {
            BigDecimal totalExecutionPrice = executedOrdersForRIC
                    .stream()
                    .map(order -> new BigDecimal(order.getQuantity()).multiply(order.getPrice()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            int totalUnits = executedOrdersForRIC
                    .stream()
                    .map(Order::getQuantity)
                    .mapToInt(i -> i)
                    .sum();

            return totalExecutionPrice.divide(new BigDecimal(totalUnits), 4, BigDecimal.ROUND_HALF_UP);
        }
    }

    public int executedQuantity(String reutersInstrumentCode, String user) {
        return executions
                .stream()
                .filter(execution -> execution.isForRIC(reutersInstrumentCode) && execution.isRelatedTo(user))
                .map(execution -> execution.getRelatedOrder(user))
                .mapToInt(order -> order.getQuantity() * order.getDirection().getSign())
                .sum();
    }
}
