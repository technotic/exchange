package technotic.exchange.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class OrderPriceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldInitialiseFromString() {

        // Expect
        assertThat(new OrderPrice("100.01").getValue(), equalTo(new BigDecimal("100.01")));
    }

    @Test
    public void shouldFailToInitialiseFromNull() {

        // Given
        thrown.expect(NullPointerException.class);

        // When
        new OrderPrice((String)null);
    }

    @Test
    public void shouldFailToInitialiseFromEmpty() {

        // Given
        thrown.expect(NumberFormatException.class);

        // When
        new OrderPrice("");
    }

    @Test
    public void shouldFailToInitialiseFromInvalidNumber() {

        // Given
        thrown.expect(NumberFormatException.class);

        // When
        new OrderPrice("ABC");
    }

}