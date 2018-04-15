package technotic.exchange.model;

import java.util.HashSet;
import java.util.Set;

public class OrderStoreValue {

    private Set<Order> openOrders = new HashSet<>();
    private Set<Execution> executions = new HashSet<>();

    public void markExecuted(Order openOrder, Order matchedOrer) {
        executions.add(new Execution(openOrder, matchedOrer));
        openOrders.remove(openOrder);
    }

    public void addOpenOrder(Order order) {
        openOrders.add(order);
    }

    public Set<Order> getOpenOrders() {
        return openOrders;
    }

    public Set<Execution> getExecutions() {
        return executions;
    }
}
