
package hospital;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DuplicateAppointmentDetector {

    // Stores the result of duplicate detection
    public static class DetectionResult {

        private List<String> duplicateAppointmentIds;
        private long comparisons;
        private long executionTime;

        public DetectionResult(
                List<String> duplicateAppointmentIds,
                long comparisons,
                long executionTime) {

            this.duplicateAppointmentIds = duplicateAppointmentIds;
            this.comparisons = comparisons;
            this.executionTime = executionTime;
        }

        public List<String> getDuplicateAppointmentIds() {
            return duplicateAppointmentIds;
        }

        public long getComparisons() {
            return comparisons;
        }

        public long getExecutionTime() {
            return executionTime;
        }
    }

    // Creates a unique key using patient ID and slot ID
    private static String createKey(Appointment appointment) {

        return appointment.getPatientId()
                + "|"
                + appointment.getSlotId();
    }

    // Approach 1: Duplicate detection using List
    public static DetectionResult detectUsingList(
            List<Appointment> appointments) {

        List<String> existingKeys = new ArrayList<>();
        List<String> duplicates = new ArrayList<>();

        long comparisons = 0;

        long startTime = System.nanoTime();

        for (Appointment appointment : appointments) {

            String key = createKey(appointment);

            boolean found = false;

            for (String existingKey : existingKeys) {

                comparisons++;

                if (existingKey.equals(key)) {
                    found = true;
                    break;
                }
            }

            if (found) {
                duplicates.add(appointment.getAppointmentId());
            } else {
                existingKeys.add(key);
            }
        }

        long endTime = System.nanoTime();

        return new DetectionResult(
                duplicates,
                comparisons,
                endTime - startTime
        );
    }

    // Approach 2: Duplicate detection using HashSet
    public static DetectionResult detectUsingHashSet(
            List<Appointment> appointments) {

        Set<String> existingKeys = new HashSet<>();

        List<String> duplicates = new ArrayList<>();

        long startTime = System.nanoTime();

        for (Appointment appointment : appointments) {

            String key = createKey(appointment);

            // add() returns false if the key already exists
            if (!existingKeys.add(key)) {

                duplicates.add(appointment.getAppointmentId());
            }
        }

        long endTime = System.nanoTime();

        return new DetectionResult(
                duplicates,
                0,
                endTime - startTime
        );
    }

    // Display duplicate appointments
    public static void displayDuplicates(
            List<String> duplicateIds) {

        if (duplicateIds.isEmpty()) {

            System.out.println("No duplicate appointments found.");

        } else {

            System.out.println("Duplicate appointments found:");

            for (String id : duplicateIds) {
                System.out.println("Appointment ID: " + id);
            }
        }
    }

    // Compare both approaches
    public static void comparePerformance(
            List<Appointment> appointments) {

        DetectionResult listResult =
                detectUsingList(appointments);

        DetectionResult hashSetResult =
                detectUsingHashSet(appointments);

        System.out.println("\n========================================");
        System.out.println("    DUPLICATE APPOINTMENT DETECTION");
        System.out.println("========================================");

        System.out.println("\nTotal appointments: " + appointments.size());

        System.out.println("\n========== LIST SCAN ==========");

        displayDuplicates(
                listResult.getDuplicateAppointmentIds()
        );

        System.out.println("Comparisons: " + listResult.getComparisons());

        System.out.println("Execution Time (ns): "
                + listResult.getExecutionTime());

        System.out.println("\n========== HASHSET ==========");

        displayDuplicates(
                hashSetResult.getDuplicateAppointmentIds()
        );

        System.out.println("Execution Time (ns): "
                + hashSetResult.getExecutionTime());

        System.out.println("\n========== PERFORMANCE COMPARISON ==========");

        System.out.printf(
                "%-25s %-15s %-15s%n",
                "Metric", "List Scan", "HashSet"
        );

        System.out.println("-----------------------------------------------");

        System.out.printf(
                "%-25s %-15d %-15d%n",
                "Appointments",
                appointments.size(),
                appointments.size()
        );

        System.out.printf(
                "%-25s %-15d %-15s%n",
                "Comparisons",
                listResult.getComparisons(),
                "N/A"
        );

        System.out.printf(
                "%-25s %-15d %-15d%n",
                "Duplicates Found",
                listResult.getDuplicateAppointmentIds().size(),
                hashSetResult.getDuplicateAppointmentIds().size()
        );

        System.out.printf(
                "%-25s %-15d %-15d%n",
                "Execution Time (ns)",
                listResult.getExecutionTime(),
                hashSetResult.getExecutionTime()
        );

        System.out.println("-----------------------------------------------");

        System.out.println("List Scan Complexity: O(n^2)");

        System.out.println("HashSet Complexity: O(n) average");

        System.out.println("===============================================");
    }
}
