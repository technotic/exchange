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

    public Order getMatchedOrder() {
        return matchedOrder;
    }

    public boolean isParty(String user) {
        return executedOrder.getUser().equals(user) || matchedOrder.getUser().equals(user);
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
}
