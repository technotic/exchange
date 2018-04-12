package technotic.exchange.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.math.BigDecimal;

import static java.util.Arrays.stream;
import static org.apache.commons.lang.builder.ToStringBuilder.reflectionToString;
import static technotic.exchange.model.Direction.BUY;
import static technotic.exchange.model.Direction.SELL;
import static technotic.exchange.utils.BigDecimalUtils.lessThanOrEqual;

public class Order {

    private final Direction direction;
    private final int quantity;
    private final String reutersInstrumentCode;
    private final BigDecimal price;
    private final String user;
    private final long timestampPlaced;

    public Order(Direction direction, int quantity, String reutersInstrumentCode, BigDecimal price, String user, long timestampPlaced) {
        this.direction = direction;
        this.reutersInstrumentCode = reutersInstrumentCode;
        this.quantity = quantity;
        this.price = price;
        this.user = user;
        this.timestampPlaced = timestampPlaced;
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

    public long getTimePlaced() {
        return timestampPlaced;
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
                .append(timestampPlaced, order.timestampPlaced)
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
                .append(timestampPlaced)
                .toHashCode();
    }

    @Override
    public String toString() {
        return reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }

    public boolean matches(Order order) {
        return direction != order.getDirection()
                && reutersInstrumentCode.equals(order.getReutersInstrumentCode())
                && quantity == order.quantity
                && sellPriceLessThanOrEqualToBuy(this, order);
    }

    private static boolean sellPriceLessThanOrEqualToBuy(Order... buySellPair) {
        return lessThanOrEqual(
                orderWithDirection(SELL, buySellPair).price,
                orderWithDirection(BUY, buySellPair).price);
    }

    private static Order orderWithDirection(Direction direction, Order... orders) {
        return stream(orders)
                .filter(order -> order.getDirection() == direction)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No order found for direction " + direction));
    }
}
