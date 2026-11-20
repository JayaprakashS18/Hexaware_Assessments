
package logistics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ParcelTracker {

    // HashMap stores tracking number and parcel details
    private Map<String, ParcelRecord> parcels = new HashMap<>();

    // Parcel details
    private static class ParcelRecord {

        String trackingNumber;
        String destination;

        // HashSet stores unique scanned hub codes
        Set<String> scannedHubs = new HashSet<>();

        ParcelRecord(String trackingNumber, String destination) {

            this.trackingNumber = trackingNumber;
            this.destination = destination;
        }
    }

    // Register parcel
    public boolean registerParcel(String trackingNumber,
                                  String destination) {

        if (trackingNumber == null
                || trackingNumber.trim().isEmpty()
                || destination == null
                || destination.trim().isEmpty()) {

            return false;
        }

        trackingNumber = trackingNumber.trim();
        destination = destination.trim();

        if (parcels.containsKey(trackingNumber)) {
            return false;
        }

        parcels.put(trackingNumber,
                new ParcelRecord(trackingNumber, destination));

        return true;
    }

    // Search parcel
    public void searchParcel(String trackingNumber) {

        if (trackingNumber == null) {
            System.out.println("Invalid tracking number.");
            return;
        }

        ParcelRecord parcel = parcels.get(trackingNumber.trim());

        if (parcel == null) {

            System.out.println("Parcel not found.");

        } else {

            System.out.println("\nParcel Found!");

            System.out.println("Tracking Number: "
                    + parcel.trackingNumber);

            System.out.println("Destination: "
                    + parcel.destination);

            System.out.println("Scanned Hubs: "
                    + parcel.scannedHubs);
        }
    }

    // Scan parcel at a hub
    public boolean scanParcel(String trackingNumber,
                              String hubCode) {

        if (trackingNumber == null || hubCode == null) {
            System.out.println("Invalid scan details.");
            return false;
        }

        ParcelRecord parcel = parcels.get(trackingNumber.trim());

        if (parcel == null) {

            System.out.println("Parcel not registered.");
            return false;
        }

        hubCode = hubCode.trim();

        if (hubCode.isEmpty()) {
            System.out.println("Invalid hub code.");
            return false;
        }

        // HashSet.add returns false for duplicates
        if (!parcel.scannedHubs.add(hubCode)) {

            System.out.println("Duplicate scan detected!");

            System.out.println("Parcel: "
                    + trackingNumber);

            System.out.println("Hub: " + hubCode);

            return false;
        }

        System.out.println("Parcel scanned successfully at "
                + hubCode);

        return true;
    }

    // Display all parcels
    public void displayAllParcels() {

        if (parcels.isEmpty()) {

            System.out.println("No parcels registered.");
            return;
        }

        System.out.println("\nRegistered Parcels:");

        for (ParcelRecord parcel : parcels.values()) {

            System.out.println("-------------------------");

            System.out.println("Tracking Number: "
                    + parcel.trackingNumber);

            System.out.println("Destination: "
                    + parcel.destination);

            System.out.println("Scanned Hubs: "
                    + parcel.scannedHubs);
        }

        System.out.println("-------------------------");
    }

    // Check whether parcel exists
    public boolean parcelExists(String trackingNumber) {

        return trackingNumber != null
                && parcels.containsKey(trackingNumber.trim());
    }

    // Return number of registered parcels
    public int getParcelCount() {
        return parcels.size();
    }
}
