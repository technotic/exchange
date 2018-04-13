package technotic.exchange.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.math.BigDecimal;

import static java.lang.String.format;

public class OpenInterest {

    private final int totalOrderQuantity;
    private final BigDecimal orderPrice;

    public OpenInterest(int totalOrderQuantity, BigDecimal orderPrice) {
        this.totalOrderQuantity = totalOrderQuantity;
        this.orderPrice = orderPrice;
    }

    public int getTotalOrderQuantity() {
        return totalOrderQuantity;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        OpenInterest that = (OpenInterest) o;

        return new EqualsBuilder()
                .append(totalOrderQuantity, that.totalOrderQuantity)
                .append(orderPrice, that.orderPrice)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(totalOrderQuantity)
                .append(orderPrice)
                .toHashCode();
    }

    @Override
    public String toString() {
        return format("%d @ %s", totalOrderQuantity, orderPrice);
    }

}
