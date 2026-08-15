import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortingBenchmark {

    // Dataset path constants
    private static final String DATASET_1000_SORTED = "1000places_sorted.csv";
    private static final String DATASET_1000_RANDOM = "1000places_random.csv";
    private static final String DATASET_10000_SORTED = "10000places_sorted.csv";
    private static final String DATASET_10000_RANDOM = "10000places_random.csv";

    public static void main(String[] args) {
        try {
            // Load datasets
            List<String> data1000Sorted = loadDataset(DATASET_1000_SORTED);
            List<String> data1000Random = loadDataset(DATASET_1000_RANDOM);
            List<String> data10000Sorted = loadDataset(DATASET_10000_SORTED);
            List<String> data10000Random = loadDataset(DATASET_10000_RANDOM);

            // Benchmark insertion sort
            long insertion1000Sorted = benchmarkSort(SortingBenchmark::insertionSort, data1000Sorted);
            long insertion1000Random = benchmarkSort(SortingBenchmark::insertionSort, data1000Random);
            long insertion10000Sorted = benchmarkSort(SortingBenchmark::insertionSort, data10000Sorted);
            long insertion10000Random = benchmarkSort(SortingBenchmark::insertionSort, data10000Random);

            // Benchmark quick sort
            long quick1000Sorted = benchmarkSort(SortingBenchmark::quickSort, data1000Sorted);
            long quick1000Random = benchmarkSort(SortingBenchmark::quickSort, data1000Random);
            long quick10000Sorted = benchmarkSort(SortingBenchmark::quickSort, data10000Sorted);
            long quick10000Random = benchmarkSort(SortingBenchmark::quickSort, data10000Random);

            // Benchmark merge sort
            long merge1000Sorted = benchmarkSort(SortingBenchmark::mergeSort, data1000Sorted);
            long merge1000Random = benchmarkSort(SortingBenchmark::mergeSort, data1000Random);
            long merge10000Sorted = benchmarkSort(SortingBenchmark::mergeSort, data10000Sorted);
            long merge10000Random = benchmarkSort(SortingBenchmark::mergeSort, data10000Random);

            // Print results
            printResults("Insertion Sort", insertion1000Sorted, insertion1000Random, insertion10000Sorted, insertion10000Random);
            printResults("Quick Sort", quick1000Sorted, quick1000Random, quick10000Sorted, quick10000Random);
            printResults("Merge Sort", merge1000Sorted, merge1000Random, merge10000Sorted, merge10000Random);

        } catch (IOException e) {
            System.err.println("Error loading dataset: " + e.getMessage());
        }
    }

    //---------------------------------------- Data Loading ----------------------------------------
    /**
     * Load dataset from CSV file into ArrayList
     */
    private static List<String> loadDataset(String filename) throws IOException {
        List<String> dataset = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                dataset.add(line.trim()); // Assume each line contains one place name
            }
        }
        return dataset;
    }

    //---------------------------------------- Sorting Algorithms ----------------------------------------
    /** Insertion Sort */
    private static void insertionSort(List<String> list) {
        for (int i = 1; i < list.size(); i++) {
            String key = list.get(i);
            int j = i - 1;
            while (j >= 0 && list.get(j).compareTo(key) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }

    /** Quick Sort (recursive implementation) */
    private static void quickSort(List<String> list) {
        quickSort(list, 0, list.size() - 1);
    }

    private static void quickSort(List<String> list, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(list, low, high);
            quickSort(list, low, pivotIndex - 1);
            quickSort(list, pivotIndex + 1, high);
        }
    }

    private static int partition(List<String> list, int low, int high) {
        String pivot = list.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (list.get(j).compareTo(pivot) <= 0) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, high);
        return i + 1;
    }

    /** Merge Sort */
    private static void mergeSort(List<String> list) {
        if (list.size() <= 1) return;
        int mid = list.size() / 2;
        List<String> left = new ArrayList<>(list.subList(0, mid));
        List<String> right = new ArrayList<>(list.subList(mid, list.size()));
        mergeSort(left);
        mergeSort(right);
        merge(list, left, right);
    }

    private static void merge(List<String> result, List<String> left, List<String> right) {
        int i = 0, j = 0, k = 0;
        while (i < left.size() && j < right.size()) {
            if (left.get(i).compareTo(right.get(j)) <= 0) {
                result.set(k++, left.get(i++));
            } else {
                result.set(k++, right.get(j++));
            }
        }
        while (i < left.size()) result.set(k++, left.get(i++));
        while (j < right.size()) result.set(k++, right.get(j++));
    }

    //---------------------------------------- Timing Logic ----------------------------------------
    /**
     * Benchmark method: Perform warm-up runs and return median time
     * @param sortFunction Sorting function reference
     * @param dataset Dataset copy
     * @return Median time in nanoseconds
     */
    private static long benchmarkSort(SortFunction sortFunction, List<String> dataset) {
        List<Long> executionTimes = new ArrayList<>();
        List<String> dataCopy = new ArrayList<>(dataset);

        // Warm-up phase (5 runs)
        for (int i = 0; i < 5; i++) {
            sortFunction.sort(new ArrayList<>(dataCopy));
        }

        // Formal testing (10 runs)
        for (int i = 0; i < 10; i++) {
            List<String> copy = new ArrayList<>(dataCopy);
            long startTime = System.nanoTime();
            sortFunction.sort(copy);
            long endTime = System.nanoTime();
            executionTimes.add(endTime - startTime);
        }

        // Calculate median
        Collections.sort(executionTimes);
        return executionTimes.get(executionTimes.size() / 2);
    }

    /** Functional interface for sorting methods */
    @FunctionalInterface
    interface SortFunction {
        void sort(List<String> list);
    }

    //---------------------------------------- Results Output ----------------------------------------
    private static void printResults(String algorithmName, long time1000Sorted, long time1000Random,
                                     long time10000Sorted, long time10000Random) {
        System.out.println("\n--- " + algorithmName + " Performance ---");
        System.out.printf("| %-20s | %-15s | %-15s | %-15s | %-15s |\n",
                "Dataset", "1000 Sorted", "1000 Random", "10000 Sorted", "10000 Random");
        System.out.printf("| %-20s | %,15d | %,15d | %,15d | %,15d |\n",
                algorithmName, time1000Sorted, time1000Random, time10000Sorted, time10000Random);
    }
}
