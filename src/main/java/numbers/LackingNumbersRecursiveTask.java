package numbers;

import java.util.concurrent.RecursiveTask;

public class LackingNumbersRecursiveTask extends RecursiveTask<Integer> {
    private Integer[] numbers;
    int begin;
    int end;

    public LackingNumbersRecursiveTask(Integer[] numbers, int begin, int end) {
        this.numbers = numbers;
        this.begin = begin;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if(numbers.length <= 1) {
            return 0;
        }
        if(begin < end) {
            int mid = begin + (end - begin) / 2;
            LackingNumbersRecursiveTask left = new LackingNumbersRecursiveTask(numbers, begin, mid);
            LackingNumbersRecursiveTask right = new LackingNumbersRecursiveTask(numbers, mid+1, end);

            right.fork();
            left.compute();
            right.join();
            return mergeWithLackingCount(numbers, begin, mid, end);
        } else {
            return 0;
        }
    }

    private Integer mergeWithLackingCount(Integer[] numbers, int begin, int mid, int end) {
        int lackingCount = 0;
        int n1 = mid - begin + 1;
        int n2 = end - mid;

        Integer[] left = new Integer[n1];
        Integer[] right = new Integer[n2];

        for(int i=0; i<n1; i++) {
            left[i] = numbers[begin + i];
        }
        for(int i=0; i<n2; i++) {
            right[i] = numbers[mid + 1 + i];
        }

        int indexL = 0;
        int indexR = 0;

        int index = begin;

        if(left[indexL] <= right[indexR]) {
            numbers[index] = left[indexL];
            indexL++;
        } else {
            numbers[index] = right[indexR];
            indexR++;
        }
        index++;

        while(indexL < n1 && indexR < n2) {
            if(left[indexL] <= right[indexR]) {
                numbers[index] = left[indexL];
                indexL++;
            } else {
                numbers[index] = right[indexR];
                indexR++;
            }
            lackingCount += numbers[index] - numbers[index-1] - 1;
            index++;
        }

        while(indexL < n1) {
            numbers[index] = left[indexL];
            indexL++;
            lackingCount += numbers[index] - numbers[index-1] - 1;
            index++;
        }

        while(indexR < n2) {
            numbers[index] = right[indexR];
            indexR++;
            lackingCount += numbers[index] - numbers[index-1] - 1;
            index++;
        }
        return lackingCount;
    }
}

