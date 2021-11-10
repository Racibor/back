package numbers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LackingNumbersCounterTest {

    @Test
    public void normalTests() {
        Integer[] array = {5, 2, 9, 7};
        assertEquals(4, LackingNumbersCounter.getLackingNumbersCount(array));

        Integer[] custom = {10, 200, 300, 100, 50};
        assertEquals(286, LackingNumbersCounter.getLackingNumbersCount(custom));

        Integer[] incrementation = {1, 4, 2, 3, 6, 5};
        assertEquals(0, LackingNumbersCounter.getLackingNumbersCount(incrementation));

        Integer[] two = { 4, 9 };
        assertEquals(4, LackingNumbersCounter.getLackingNumbersCount(two));

        Integer[] one = { 1 };
        assertEquals(0, LackingNumbersCounter.getLackingNumbersCount(one));

        Integer[] zero = { };
        assertEquals(0, LackingNumbersCounter.getLackingNumbersCount(zero));
    }
}
