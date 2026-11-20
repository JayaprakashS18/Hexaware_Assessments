
package hospital;

import java.util.List;

public class PatientSearch {

    // Stores the patient found and comparison count
    public static class SearchResult {

        private Patient patient;
        private int comparisons;

        public SearchResult(Patient patient, int comparisons) {
            this.patient = patient;
            this.comparisons = comparisons;
        }

        public Patient getPatient() {
            return patient;
        }

        public int getComparisons() {
            return comparisons;
        }
    }

    // Stores benchmark results
    public static class BenchmarkResult {

        private long totalTime;
        private long totalComparisons;
        private int successfulSearches;

        public BenchmarkResult(long totalTime,
                               long totalComparisons,
                               int successfulSearches) {

            this.totalTime = totalTime;
            this.totalComparisons = totalComparisons;
            this.successfulSearches = successfulSearches;
        }

        public long getTotalTime() {
            return totalTime;
        }

        public long getTotalComparisons() {
            return totalComparisons;
        }

        public int getSuccessfulSearches() {
            return successfulSearches;
        }

        public double getAverageTime() {
            return (double) totalTime / 1000;
        }

        public double getAverageComparisons() {
            return (double) totalComparisons / 1000;
        }
    }

    // TASK 1A: LINEAR SEARCH

    public static SearchResult linearSearch(
            List<Patient> patients, String targetId) {

        int comparisons = 0;

        for (Patient patient : patients) {

            comparisons++;

            if (patient.getPatientId().equals(targetId)) {

                return new SearchResult(patient, comparisons);
            }
        }

        return new SearchResult(null, comparisons);
    }

    // TASK 1B: BINARY SEARCH

    // Patient records must be sorted by patient ID.

    public static SearchResult binarySearch(
            List<Patient> patients, String targetId) {

        int low = 0;
        int high = patients.size() - 1;

        int comparisons = 0;

        while (low <= high) {

            int mid = low + (high - low) / 2;

            String middleId = patients.get(mid).getPatientId();

            comparisons++;

            int result = targetId.compareTo(middleId);

            if (result == 0) {

                return new SearchResult(
                    patients.get(mid), comparisons
                );
            }

            else if (result < 0) {

                high = mid - 1;
            }

            else {

                low = mid + 1;
            }
        }

        return new SearchResult(null, comparisons);
    }

    // TASK 1C: LINEAR SEARCH BENCHMARK

    public static BenchmarkResult benchmarkLinear(
            List<Patient> patients, List<String> queries) {

        long totalComparisons = 0;
        int successfulSearches = 0;

        long startTime = System.nanoTime();

        for (String query : queries) {

            SearchResult result = linearSearch(patients, query);

            totalComparisons += result.getComparisons();

            if (result.getPatient() != null) {
                successfulSearches++;
            }
        }

        long endTime = System.nanoTime();

        return new BenchmarkResult(
            endTime - startTime,
            totalComparisons,
            successfulSearches
        );
    }

    // TASK 1D: BINARY SEARCH BENCHMARK

    public static BenchmarkResult benchmarkBinary(
            List<Patient> patients, List<String> queries) {

        long totalComparisons = 0;
        int successfulSearches = 0;

        long startTime = System.nanoTime();

        for (String query : queries) {

            SearchResult result = binarySearch(patients, query);

            totalComparisons += result.getComparisons();

            if (result.getPatient() != null) {
                successfulSearches++;
            }
        }

        long endTime = System.nanoTime();

        return new BenchmarkResult(
            endTime - startTime,
            totalComparisons,
            successfulSearches
        );
    }

    // TASK 1E: PERFORMANCE COMPARISON

    public static void comparePerformance(
            List<Patient> linearData,
            List<Patient> binaryData,
            List<String> queries) {

        // Warm-up to reduce initial JVM compilation effects
        int warmupCount = Math.min(100, queries.size());

        for (int i = 0; i < warmupCount; i++) {

            linearSearch(linearData, queries.get(i));

            binarySearch(binaryData, queries.get(i));
        }

        BenchmarkResult linearResult =
                benchmarkLinear(linearData, queries);

        BenchmarkResult binaryResult =
                benchmarkBinary(binaryData, queries);

        System.out.println("\n========================================");
        System.out.println("       SEARCH PERFORMANCE REPORT");
        System.out.println("========================================");

        System.out.printf("%-25s %-15s %-15s%n",
                "Metric", "Linear", "Binary");

        System.out.println("----------------------------------------");

        System.out.printf("%-25s %-15d %-15d%n",
                "Patient Records",
                linearData.size(),
                binaryData.size());

        System.out.printf("%-25s %-15d %-15d%n",
                "Total Lookups",
                queries.size(),
                queries.size());

        System.out.printf("%-25s %-15d %-15d%n",
                "Successful Searches",
                linearResult.getSuccessfulSearches(),
                binaryResult.getSuccessfulSearches());

        System.out.printf("%-25s %-15d %-15d%n",
                "Total Comparisons",
                linearResult.getTotalComparisons(),
                binaryResult.getTotalComparisons());

        System.out.printf("%-25s %-15.2f %-15.2f%n",
                "Average Comparisons",
                linearResult.getAverageComparisons(),
                binaryResult.getAverageComparisons());

        System.out.printf("%-25s %-15d %-15d%n",
                "Total Time (ns)",
                linearResult.getTotalTime(),
                binaryResult.getTotalTime());

        System.out.printf("%-25s %-15.2f %-15.2f%n",
                "Average Time (ns)",
                linearResult.getAverageTime(),
                binaryResult.getAverageTime());

        System.out.println("----------------------------------------");

        System.out.println("Linear Search Complexity : O(n)");
        System.out.println("Binary Search Complexity : O(log n)");

        System.out.println("========================================");
    }
}
