package numbers;

import java.util.LinkedList;
import java.util.Queue;

public class LackingNumbersCounter {
    public static LackingNumbersRecursiveTask getLackingNumbersCountMultiThreaded(Integer numbers[]) {
        return new LackingNumbersRecursiveTask(numbers, 0, numbers.length-1);
    }

    public static int getLackingNumbersCount(Integer numbers[]) {
        if(numbers.length <= 1) {
            return 0;
        }
        Queue<Integer[]> queue = new LinkedList<Integer[]>();
        for (int i = 0; i < numbers.length; i++) {
            queue.add(new Integer[]{numbers[i]});
        }
        while (queue.size()>2) {
            Integer[] r = queue.poll();
            Integer[] l = queue.poll();
            Integer[] merged = merge(l, r);
            queue.add(merged);
        }
        return mergeWithLackingCount(queue.poll(), queue.poll());
    }

    private static int mergeWithLackingCount(Integer a[], Integer b[]) {
        int lackingCount = 0;
        Integer c[] = new Integer[a.length + b.length];
        int index = 0;
        int indexA = 0;
        int indexB = 0;

        if(a[indexA] < b[indexB]) {
            c[index] = a[indexA];
            indexA++;
        } else {
            c[index] = b[indexB];
            indexB++;
        }
        index++;

        while(indexA < a.length && indexB < b.length) {
            if(a[indexA] < b[indexB]) {
                c[index] = a[indexA];
                indexA++;
            } else {
                c[index] = b[indexB];
                indexB++;
            }
            lackingCount += c[index] - c[index-1] - 1;
            index++;
        }
        while(indexA < a.length) {
            c[index] = a[indexA];
            indexA++;
            lackingCount += c[index] - c[index-1] - 1;
            index++;
        }
        while(indexB < b.length) {
            c[index] = b[indexB];
            indexB++;
            lackingCount += c[index] - c[index-1] - 1;
            index++;
        }
        return lackingCount;
    }

    private static Integer[] merge(Integer a[], Integer b[]) {
        Integer c[] = new Integer[a.length + b.length];
        int index = 0;
        int indexA = 0;
        int indexB = 0;

        while(indexA < a.length && indexB < b.length) {
            if(a[indexA] <= b[indexB]) {
                c[index] = a[indexA];
                indexA++;
            } else {
                c[index] = b[indexB];
                indexB++;
            }
            index++;
        }
        while(indexA < a.length) {
            c[index] = a[indexA];
            indexA++;
            index++;
        }
        while(indexB < b.length) {
            c[index] = b[indexB];
            indexB++;
            index++;
        }
        return c;
    }
}
