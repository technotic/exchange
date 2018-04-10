package technotic.exchange.model;

public class Order {

    private Direction direction;
    private ReutersInstrumentCode reutersInstrumentCode;
    private OrderQuantity orderQuantity;
    private OrderPrice orderPrice;
    private User user;

    public Order(Direction direction,
                 OrderQuantity orderQuantity,
                 ReutersInstrumentCode reutersInstrumentCode,
                 OrderPrice orderPrice,
                 User user) {
        this.direction = direction;
        this.reutersInstrumentCode = reutersInstrumentCode;
        this.orderQuantity = orderQuantity;
        this.orderPrice = orderPrice;
        this.user = user;
    }

    public Direction getDirection() {
        return direction;
    }

    public ReutersInstrumentCode getReutersInstrumentCode() {
        return reutersInstrumentCode;
    }

    public OrderQuantity getOrderQuantity() {
        return orderQuantity;
    }

    public OrderPrice getOrderPrice() {
        return orderPrice;
    }

    public User getUser() {
        return user;
    }
}
