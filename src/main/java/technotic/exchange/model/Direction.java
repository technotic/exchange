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

        @Override
        public int getSign() {
            return 1;
        }
    },
    SELL {
        @Override
        public Comparator<Order> getComparator() {
            return comparing(Order::getPrice)
                    .thenComparing(Order::getTimePlaced);
        }

        @Override
        public int getSign() {
            return -1;
        }
    };

    public abstract Comparator<Order> getComparator();

    public abstract int getSign();
}
