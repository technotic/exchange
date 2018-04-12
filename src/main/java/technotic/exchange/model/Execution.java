package technotic.exchange.model;

public class Execution {

    private final Order executedOrder;
    private final Order matchedOrder;

    public Execution(Order executedOrder, Order matchedOrder) {
        this.executedOrder = executedOrder;
        this.matchedOrder = matchedOrder;
    }

    public Order getExecutedOrder() {
        return executedOrder;
    }

    public boolean isRelatedTo(String user) {
        return executedOrder.isForUser(user) || matchedOrder.isForUser(user);
    }

    public Order getRelatedOrder(String user) {
        if (executedOrder.getUser().equals(user)) {
            return executedOrder;
        } else if (matchedOrder.getUser().equals(user)) {
            return matchedOrder;
        } else {
            throw new IllegalArgumentException("User is not a party in this execution");
        }
    }

    public boolean isForRIC(String reutersInstrumentCode) {
        return executedOrder.isForRIC(reutersInstrumentCode);
    }
}
