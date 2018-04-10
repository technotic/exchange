package technotic.exchange.model;

import java.math.BigDecimal;

public class OrderPrice {

    private BigDecimal value;

    public OrderPrice(BigDecimal value) {
        this.value = value;
    }

    public OrderPrice(String value) {
        this.value = new BigDecimal(value);
    }

    public BigDecimal getValue() {
        return value;
    }
}
