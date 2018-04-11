package technotic.exchange.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.math.BigDecimal;

public class OpenOrderInterest {

    private final int totalOrderQuantity;
    private final BigDecimal orderPrice;

    public OpenOrderInterest(int totalOrderQuantity, BigDecimal orderPrice) {
        this.totalOrderQuantity = totalOrderQuantity;
        this.orderPrice = orderPrice;
    }

    public static OpenOrderInterest openOrderInterest(int totalOrderQuantity, BigDecimal orderPrice) {
        return new OpenOrderInterest(totalOrderQuantity, orderPrice);
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

        OpenOrderInterest that = (OpenOrderInterest) o;

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
}
