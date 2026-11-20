
package logistics;

public class DeliveryWindowAnalyzer {

    // Result class
    public static class WindowResult {

        private int startHour;
        private int endHour;
        private int totalParcels;
        private double average;

        public WindowResult(int startHour,
                            int endHour,
                            int totalParcels,
                            double average) {

            this.startHour = startHour;
            this.endHour = endHour;
            this.totalParcels = totalParcels;
            this.average = average;
        }

        public int getStartHour() {
            return startHour;
        }

        public int getEndHour() {
            return endHour;
        }

        public int getTotalParcels() {
            return totalParcels;
        }

        public double getAverage() {
            return average;
        }

        @Override
        public String toString() {

            return "Time Window: Hour " + startHour
                    + " to Hour " + endHour
                    + "\nTotal Parcels: " + totalParcels
                    + "\nAverage Parcels: "
                    + String.format("%.2f", average);
        }
    }

    // Find highest average four-hour window
    public WindowResult findHighestAverageWindow(
            int[] parcelCounts) {

        final int WINDOW_SIZE = 4;

        if (parcelCounts == null
                || parcelCounts.length < WINDOW_SIZE) {

            System.out.println(
                    "At least four hourly counts are required.");

            return null;
        }

        // Validate parcel counts
        for (int count : parcelCounts) {

            if (count < 0) {

                System.out.println(
                        "Parcel counts cannot be negative.");

                return null;
            }
        }

        // Calculate initial window
        int windowSum = 0;

        for (int i = 0; i < WINDOW_SIZE; i++) {

            windowSum += parcelCounts[i];
        }

        int maxSum = windowSum;

        int maxStart = 0;

        // Sliding window
        for (int i = WINDOW_SIZE;
             i < parcelCounts.length; i++) {

            windowSum = windowSum
                    - parcelCounts[i - WINDOW_SIZE]
                    + parcelCounts[i];

            if (windowSum > maxSum) {

                maxSum = windowSum;

                maxStart = i - WINDOW_SIZE + 1;
            }
        }

        double average = (double) maxSum / WINDOW_SIZE;

        return new WindowResult(
                maxStart + 1,
                maxStart + WINDOW_SIZE,
                maxSum,
                average
        );
    }
}
