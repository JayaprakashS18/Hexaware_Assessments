
package hospital;

import java.util.ArrayList;
import java.util.List;

public class AppointmentSorting {

    // Stores sorting results
    public static class SortResult {

        private List<Appointment> appointments;
        private long swaps;
        private long executionTime;

        public SortResult(List<Appointment> appointments,
                          long swaps,
                          long executionTime) {

            this.appointments = appointments;
            this.swaps = swaps;
            this.executionTime = executionTime;
        }

        public List<Appointment> getAppointments() {
            return appointments;
        }

        public long getSwaps() {
            return swaps;
        }

        public long getExecutionTime() {
            return executionTime;
        }
    }

    // COMMON COMPARATOR

    // Negative: first appointment should come before second
    // Zero: both have the same urgency and arrival time
    // Positive: first appointment should come after second

    public static int compareAppointments(
            Appointment a, Appointment b) {

        // Condition 1: Compare urgency
        int urgencyComparison = Integer.compare(
            a.getUrgency().getPriority(),
            b.getUrgency().getPriority()
        );

        if (urgencyComparison != 0) {
            return urgencyComparison;
        }

        // Condition 2: Compare arrival time
        return a.getArrivalTime().compareTo(b.getArrivalTime());
    }

    // SELECTION SORT

    public static SortResult selectionSort(
            List<Appointment> appointments) {

        List<Appointment> data =
                new ArrayList<>(appointments);

        long swaps = 0;

        long startTime = System.nanoTime();

        int n = data.size();

        for (int i = 0; i < n - 1; i++) {

            int minIndex = i;

            // Find the minimum appointment
            for (int j = i + 1; j < n; j++) {

                if (compareAppointments(
                        data.get(j),
                        data.get(minIndex)) < 0) {

                    minIndex = j;
                }
            }

            // Swap only when positions are different
            if (minIndex != i) {

                Appointment temp = data.get(i);

                data.set(i, data.get(minIndex));

                data.set(minIndex, temp);

                swaps++;
            }
        }

        long endTime = System.nanoTime();

        return new SortResult(
            data,
            swaps,
            endTime - startTime
        );
    }

    // QUICK SORT

    public static SortResult quickSort(
            List<Appointment> appointments) {

        List<Appointment> data =
                new ArrayList<>(appointments);

        long startTime = System.nanoTime();

        long swaps = quickSortRecursive(
            data, 0, data.size() - 1
        );

        long endTime = System.nanoTime();

        return new SortResult(
            data,
            swaps,
            endTime - startTime
        );
    }

    // Recursive Quick Sort
    private static long quickSortRecursive(
            List<Appointment> data,
            int low,
            int high) {

        long swaps = 0;

        if (low < high) {

            // Partition the array
            int[] partitionResult = partition(data, low, high);

            int pivotIndex = partitionResult[0];

            swaps += partitionResult[1];

            // Sort left partition
            swaps += quickSortRecursive(
                data, low, pivotIndex - 1
            );

            // Sort right partition
            swaps += quickSortRecursive(
                data, pivotIndex + 1, high
            );
        }

        return swaps;
    }

    // Lomuto Partition
    private static int[] partition(
            List<Appointment> data,
            int low,
            int high) {

        Appointment pivot = data.get(high);

        int i = low - 1;

        long swaps = 0;

        for (int j = low; j < high; j++) {

            if (compareAppointments(
                    data.get(j), pivot) <= 0) {

                i++;

                // Avoid unnecessary self-swaps
                if (i != j) {

                    Appointment temp = data.get(i);

                    data.set(i, data.get(j));

                    data.set(j, temp);

                    swaps++;
                }
            }
        }

        // Place pivot in correct position
        int pivotIndex = i + 1;

        if (pivotIndex != high) {

            Appointment temp = data.get(pivotIndex);

            data.set(pivotIndex, data.get(high));

            data.set(high, temp);

            swaps++;
        }

        return new int[] {pivotIndex, (int) swaps};
    }

    // DISPLAY SORTED APPOINTMENTS

    public static void displayAppointments(
            List<Appointment> appointments, int limit) {

        int count = Math.min(limit, appointments.size());

        for (int i = 0; i < count; i++) {

            System.out.println(
                (i + 1) + ". " + appointments.get(i)
            );
        }

        if (appointments.size() > count) {

            System.out.println(
                "... and " + (appointments.size() - count)
                + " more appointments."
            );
        }
    }

    // VERIFY SORTING ORDER

    public static boolean isSorted(
            List<Appointment> appointments) {

        for (int i = 1; i < appointments.size(); i++) {

            if (compareAppointments(
                    appointments.get(i - 1),
                    appointments.get(i)) > 0) {

                return false;
            }
        }

        return true;
    }

    // PERFORMANCE COMPARISON

    public static void comparePerformance(
            List<Appointment> appointments) {

        // Warm-up both algorithms
        int warmupSize = Math.min(100, appointments.size());

        List<Appointment> warmupData =
                new ArrayList<>(appointments.subList(0, warmupSize));

        selectionSort(warmupData);
        quickSort(warmupData);

        // Execute both algorithms on identical input
        SortResult selectionResult =
                selectionSort(appointments);

        SortResult quickResult =
                quickSort(appointments);

        System.out.println("\n========================================");
        System.out.println("    APPOINTMENT SORTING COMPARISON");
        System.out.println("========================================");

        System.out.printf("%-25s %-15s %-15s%n",
                "Metric", "Selection", "Quick Sort");

        System.out.println("----------------------------------------");

        System.out.printf("%-25s %-15d %-15d%n",
                "Appointments",
                appointments.size(),
                appointments.size());

        System.out.printf("%-25s %-15d %-15d%n",
                "Total Swaps",
                selectionResult.getSwaps(),
                quickResult.getSwaps());

        System.out.printf("%-25s %-15.3f %-15.3f%n",
                "Execution Time (ms)",
                selectionResult.getExecutionTime() / 1_000_000.0,
                quickResult.getExecutionTime() / 1_000_000.0);

        System.out.printf("%-25s %-15s %-15s%n",
                "Sorting Correct",
                isSorted(selectionResult.getAppointments()),
                isSorted(quickResult.getAppointments()));

        System.out.println("----------------------------------------");

        System.out.println("Selection Sort: O(n^2)");
        System.out.println("Quick Sort: Average O(n log n)");

        System.out.println("========================================");
    }
}
