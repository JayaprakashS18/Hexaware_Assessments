
package hospital;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AppointmentScheduling {

    // Stores the result of an algorithm
    public static class SchedulingResult {

        private List<Appointment> selectedAppointments;
        private long executionTime;

        public SchedulingResult(
                List<Appointment> selectedAppointments,
                long executionTime) {

            this.selectedAppointments = selectedAppointments;
            this.executionTime = executionTime;
        }

        public List<Appointment> getSelectedAppointments() {
            return selectedAppointments;
        }

        public long getExecutionTime() {
            return executionTime;
        }

        public int getCount() {
            return selectedAppointments.size();
        }
    }

    // Sort appointments by ending time
    private static List<Appointment> sortByEndTime(
            List<Appointment> appointments) {

        List<Appointment> sorted = new ArrayList<>(appointments);

        sorted.sort(
            Comparator.comparing(Appointment::getEndTime)
                      .thenComparing(Appointment::getStartTime)
        );

        return sorted;
    }

    // Find the number of earlier appointments
    // that finish before the current appointment starts
    private static int findCompatibleCount(
            List<Appointment> appointments,
            int currentIndex) {

        int low = 0;
        int high = currentIndex - 1;
        int answer = -1;

        while (low <= high) {

            int mid = (low + high) / 2;

            if (!appointments.get(mid).getEndTime()
                    .isAfter(appointments.get(currentIndex).getStartTime())) {

                answer = mid;
                low = mid + 1;

            } else {

                high = mid - 1;
            }
        }

        return answer + 1;
    }

    // Dynamic Programming approach
    public static SchedulingResult dynamicProgramming(
            List<Appointment> appointments) {

        long startTime = System.nanoTime();

        List<Appointment> sorted = sortByEndTime(appointments);

        int n = sorted.size();

        int[] dp = new int[n + 1];
        int[] compatibleCount = new int[n];

        // Calculate compatible appointments
        for (int i = 0; i < n; i++) {

            compatibleCount[i] =
                findCompatibleCount(sorted, i);
        }

        // Build DP table
        for (int i = 1; i <= n; i++) {

            int include = 1 + dp[compatibleCount[i - 1]];

            int exclude = dp[i - 1];

            dp[i] = Math.max(include, exclude);
        }

        // Reconstruct selected appointments
        List<Appointment> selected = new ArrayList<>();

        int i = n;

        while (i > 0) {

            int include =
                1 + dp[compatibleCount[i - 1]];

            int exclude = dp[i - 1];

            if (include > exclude) {

                selected.add(sorted.get(i - 1));

                i = compatibleCount[i - 1];

            } else {

                i--;
            }
        }

        // Reverse to display chronological order
        java.util.Collections.reverse(selected);

        long endTime = System.nanoTime();

        return new SchedulingResult(
            selected,
            endTime - startTime
        );
    }

    // Greedy approach
    public static SchedulingResult greedy(
            List<Appointment> appointments) {

        long startTime = System.nanoTime();

        List<Appointment> sorted = sortByEndTime(appointments);

        List<Appointment> selected = new ArrayList<>();

        java.time.LocalDateTime lastEndTime = null;

        for (Appointment appointment : sorted) {

            if (lastEndTime == null ||
                !appointment.getStartTime().isBefore(lastEndTime)) {

                selected.add(appointment);

                lastEndTime = appointment.getEndTime();
            }
        }

        long endTime = System.nanoTime();

        return new SchedulingResult(
            selected,
            endTime - startTime
        );
    }

    // Display selected appointments
    public static void displaySelectedAppointments(
            String algorithm,
            SchedulingResult result) {

        System.out.println("\n" + algorithm);

        System.out.println(
            "Maximum appointments selected: " + result.getCount()
        );

        for (Appointment appointment :
                result.getSelectedAppointments()) {

            System.out.println(appointment);
        }
    }

    // Compare Dynamic Programming and Greedy
    public static void comparePerformance(
            List<Appointment> appointments) {

        SchedulingResult dpResult =
            dynamicProgramming(appointments);

        SchedulingResult greedyResult =
            greedy(appointments);

        System.out.println("\n========================================");
        System.out.println("MAXIMUM APPOINTMENT SCHEDULING");
        System.out.println("========================================");

        System.out.println(
            "Total appointments: " + appointments.size()
        );

        displaySelectedAppointments(
            "DYNAMIC PROGRAMMING",
            dpResult
        );

        displaySelectedAppointments(
            "GREEDY ALGORITHM",
            greedyResult
        );

        System.out.println("\n========================================");
        System.out.println("PERFORMANCE COMPARISON");
        System.out.println("========================================");

        System.out.printf(
            "%-25s %-15s %-15s%n",
            "Metric", "DP", "Greedy"
        );

        System.out.println("----------------------------------------");

        System.out.printf(
            "%-25s %-15d %-15d%n",
            "Total Appointments",
            appointments.size(),
            appointments.size()
        );

        System.out.printf(
            "%-25s %-15d %-15d%n",
            "Selected Appointments",
            dpResult.getCount(),
            greedyResult.getCount()
        );

        System.out.printf(
            "%-25s %-15d %-15d%n",
            "Execution Time (ns)",
            dpResult.getExecutionTime(),
            greedyResult.getExecutionTime()
        );

        System.out.println("----------------------------------------");

        System.out.println(
            "DP Complexity: O(n log n)"
        );

        System.out.println(
            "Greedy Complexity: O(n log n)"
        );

        System.out.println(
            "Both approaches select the same maximum count: "
            + (dpResult.getCount() == greedyResult.getCount())
        );

        System.out.println("========================================");
    }
}
