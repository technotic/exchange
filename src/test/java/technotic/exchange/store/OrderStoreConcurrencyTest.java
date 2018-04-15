package technotic.exchange.store;

import org.junit.Test;
import technotic.exchange.model.Order;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.google.common.collect.Sets.intersection;
import static com.google.common.collect.Sets.newHashSet;
import static java.lang.System.currentTimeMillis;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static technotic.exchange.model.Direction.BUY;
import static technotic.exchange.model.Direction.SELL;
import static technotic.exchange.utils.BigDecimalUtils.bd;

public class OrderStoreConcurrencyTest {

    private static final int NUMBER_OF_ORDERS = 1000;

    private OrderStore orderStore = new SimpleOrderStore();

    private ExecutorService executor = Executors.newFixedThreadPool(3);

    @Test
    public void shouldNotAllowAnOrderToBeBothOpenAndExecuted() throws Exception {

        runManyTimes(2.0, 1000, () -> {
            orderStore.placeOrder(new Order(BUY, 1000, "VOD.L", bd("100"), "User1", currentTimeMillis()));
        });
        runManyTimes(1.0, 1000, () -> {
            orderStore.placeOrder(new Order(SELL, 1000, "VOD.L", bd("100"), "User1", currentTimeMillis()));
        });
        runManyTimes(1.0, 1000, () -> {
            verifyNoOrdersAreBothOpenAndExecuted();
        });

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);

        verifyAllOrdersAreExecuted(NUMBER_OF_ORDERS);
        verifyNoOrdersAreBothOpenAndExecuted();
    }

    private void verifyNoOrdersAreBothOpenAndExecuted() {
        Set<Order> openOrders = newHashSet(orderStore.getExecutedOrders());
        Set<Order> executedOrders = newHashSet(orderStore.getOpenOrders());
        assertThat(intersection(openOrders, executedOrders).isEmpty(), is(true));
    }

    private void verifyAllOrdersAreExecuted(int numberOfOrders) {
        assertThat("Some orders remained open", orderStore.getOpenOrders(), is(emptyList()));
        assertThat("Some orders were not executed", orderStore.getExecutedOrders().size(), is(numberOfOrders));
    }

    private void runManyTimes(double speed, int count, Runnable runnable) {
        executor.execute(() -> {
            for (int i = 0; i < count; i++) {
                runnable.run();
                randomSleep(speed);
            }
        });
    }

    private void randomSleep(double speed) {
        try {
            long randomSleep = (long) (random(1, 5) * speed);
            Thread.sleep(randomSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Random random = new Random(System.currentTimeMillis());

    private long random(int lower, int upper) {
        return (Math.abs(random.nextInt(upper - lower)) + lower);
    }
}