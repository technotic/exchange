package technotic.exchange.store;

import org.junit.Test;
import technotic.exchange.model.Order;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.System.currentTimeMillis;
import static org.junit.Assert.assertTrue;
import static technotic.exchange.model.Direction.BUY;
import static technotic.exchange.model.Direction.SELL;
import static technotic.exchange.utils.BigDecimalUtils.bd;

public class OrderStoreConcurrencyTest {

    private OrderStore orderStore = new OrderStore();

    ExecutorService executor = Executors.newFixedThreadPool(3);

    @Test
    public void shouldNotAllowAndOrderTobeBothOpenAndExecuted() throws Exception {

        // Given
        runManyTimes(10.0, 1000, () -> {
            orderStore.placeOrder(new Order(BUY, 1000, "VOD.L", bd("100"), "User1", currentTimeMillis()));
        });
        runManyTimes(1.0, 900, () -> {
            orderStore.placeOrder(new Order(SELL, 1000, "VOD.L", bd("100"), "User1", currentTimeMillis()));
        });
        runManyTimes(1.0, 1000, () -> {
            assertTrue(!orderStore.getOpenOrders().(orderStore.getExecutedOrders()));
            assertTrue(!orderStore.getExecutedOrders().contains(orderStore.getOpenOrders()));
        });

        executor.shutdown();
        executor.awaitTermination(20, TimeUnit.SECONDS);

//        assertThat(orderStore.getExecutedOrders().size(), is(1000));
//        assertThat(orderStore.getOpenOrders().size(), is(0));
        System.out.println("open = " + orderStore.getOpenOrders().size());
        System.out.println("executed = " + orderStore.getExecutedOrders().size());
    }

    private void runManyTimes(double speed, int count, Runnable runnable) {
        executor.execute(() -> {
            for (int i = 0; i < count; i++) {
                runnable.run();
                try {
                    long randomSleep = (long) (random(5, 10) * speed);
                    Thread.sleep(randomSleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ;
        });
    }

    private Random random = new Random(System.currentTimeMillis());

    private long random(int lower, int upper) {
        return (Math.abs(random.nextInt(upper - lower)) + lower);
    }

    private <T> List<T> interection(List<T> list1, List<T> list2) {

    }


}