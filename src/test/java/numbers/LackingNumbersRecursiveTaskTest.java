package numbers;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LackingNumbersRecursiveTaskTest {
    ForkJoinPool pool;

    LackingNumbersRecursiveTaskTest() {
        pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
    }

    @Test
    public void normalTests() {
        Integer[] array = {5, 2, 9, 7};
        assertEquals(4, pool.invoke(LackingNumbersCounter.getLackingNumbersCountMultiThreaded(array)));

        Integer[] custom = {10, 200, 300, 100, 50};
        assertEquals(286, pool.invoke(LackingNumbersCounter.getLackingNumbersCountMultiThreaded(custom)));

        Integer[] incrementation = {1, 4, 2, 3, 6, 5};
        assertEquals(0, pool.invoke(LackingNumbersCounter.getLackingNumbersCountMultiThreaded(incrementation)));

        Integer[] two = { 4, 9 };
        assertEquals(4, pool.invoke(LackingNumbersCounter.getLackingNumbersCountMultiThreaded(two)));

        Integer[] one = { 1 };
        assertEquals(0, pool.invoke(LackingNumbersCounter.getLackingNumbersCountMultiThreaded(one)));

        Integer[] zero = { };
        assertEquals(0, pool.invoke(LackingNumbersCounter.getLackingNumbersCountMultiThreaded(zero)));
    }
}
