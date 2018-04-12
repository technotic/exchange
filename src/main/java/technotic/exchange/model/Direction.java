package technotic.exchange.model;

import java.util.Comparator;

import static java.util.Comparator.comparing;

public enum Direction {

    BUY {
        @Override
        public Comparator<Order> getComparator() {
            return comparing(Order::getPrice).reversed()
                    .thenComparing(Order::getTimePlaced);
        }
    },
    SELL {
        @Override
        public Comparator<Order> getComparator() {
            return comparing(Order::getPrice)
                    .thenComparing(Order::getTimePlaced);
        }
    };

    public abstract Comparator<Order> getComparator();
}
