package technotic.exchange.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.math.BigDecimal;

import static technotic.exchange.model.Direction.SELL;
import static technotic.exchange.utils.BigDecimalUtils.lessThanOrEqual;

public class Order {

    private final Direction direction;
    private final int quantity;
    private final String reutersInstrumentCode;
    private final BigDecimal price;
    private final String user;

    public Order(Direction direction, int quantity, String reutersInstrumentCode, BigDecimal price, String user) {
        this.direction = direction;
        this.reutersInstrumentCode = reutersInstrumentCode;
        this.quantity = quantity;
        this.price = price;
        this.user = user;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getReutersInstrumentCode() {
        return reutersInstrumentCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return new EqualsBuilder()
                .append(quantity, order.quantity)
                .append(direction, order.direction)
                .append(reutersInstrumentCode, order.reutersInstrumentCode)
                .append(price, order.price)
                .append(user, order.user)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(direction)
                .append(reutersInstrumentCode)
                .append(quantity)
                .append(price)
                .append(user)
                .toHashCode();
    }

    public boolean matches(Order order) {
        return direction != order.getDirection()
                && reutersInstrumentCode.equals(order.getReutersInstrumentCode())
                && quantity == order.quantity
                && sellPriceLessThanOrEqualToBuy(this, order);
    }

    /**
     * Assumes order1 and order2 are opposite directions.
     */
    private static boolean sellPriceLessThanOrEqualToBuy(Order order1, Order order2) {
        if (order1.getDirection() == SELL) {
            return lessThanOrEqual(order1.price, order2.price);
        } else {
            return lessThanOrEqual(order2.price, order1.price);
        }
    }
}
