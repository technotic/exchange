package technotic.exchange.store;

import technotic.exchange.matchers.OrderMatcher;
import technotic.exchange.model.*;
import technotic.exchange.sorting.OrderSorter;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class ConcurrentOrderStore implements OrderStore {

    private ConcurrentHashMap<String, OrderStoreValue> orderStore = new ConcurrentHashMap<>();

    private OrderMatcher orderMatcher;

    public ConcurrentOrderStore(OrderMatcher orderMatcher) {
        this.orderMatcher = orderMatcher;
    }

    public boolean placeOrder(Order order) {
        final AtomicBoolean matched = new AtomicBoolean(false);
        orderStore.computeIfAbsent(order.getKey(), (key) -> new OrderStoreValue());
        orderStore.compute(order.getKey(), (key, orders) -> {
            Optional<Order> matchedOrder = orderMatcher.findMatch(order, orders.getOpenOrders());
            if (matchedOrder.isPresent()) {
                orders.markExecuted(order, matchedOrder.get());
                matched.set(true);
                return orders;
            } else {
                orders.addOpenOrder(order);
                return orders;
            }
        });
        return matched.get();
    }

    public List<Order> getOpenOrders() {
        return orderStore
                .values()
                .stream()
                .map(OrderStoreValue::getOpenOrders)
                .flatMap(Collection::stream)
                .collect(toList());
    }

    public List<Order> getExecutedOrders() {
        return orderStore
                .values()
                .stream()
                .map(OrderStoreValue::getExecutions)
                .flatMap(Collection::stream)
                .map(Execution::getExecutedOrder)
                .collect(toList());
    }

    @Override
    public List<OpenInterest> openInterest(String reutersInstrumentCode, Direction direction) {
        return null;
    }

    @Override
    public BigDecimal averageExecutionPrice(String reutersInstrumentCode) {
        return null;
    }

    @Override
    public int executedQuantity(String reutersInstrumentCode, String user) {
        return 0;
    }
}