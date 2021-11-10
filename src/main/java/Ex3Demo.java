import numbers.LackingNumbersCounter;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

public class Ex3Demo {
    static Integer[] array = {5, 2, 9, 7};
    static Integer[] custom = {10, 200, 300, 100, 50};

    public static void ex3() {
        System.out.println("\n-------Zad nr 3----------\n");

        System.out.print("tablica: " );
        Arrays.stream(array).forEach(e -> System.out.print(" " + e));
        System.out.println();
        System.out.println("rozwiązanie: " + LackingNumbersCounter.getLackingNumbersCount(array));

        System.out.println("\n-----------------\n");

        System.out.print("tablica: ");
        Arrays.stream(custom).forEach(e -> System.out.print(" " + e));
        System.out.println();
        ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        System.out.println("rozwiązanie: " + pool.invoke(LackingNumbersCounter.getLackingNumbersCountMultiThreaded(custom)));
    }
}
