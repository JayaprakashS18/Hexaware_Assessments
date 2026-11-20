
package logistics;

import java.util.List;
import java.util.Scanner;

public class Main {

    // Single Scanner for the entire application
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("==========================================");
        System.out.println("       LOGISTICS NETWORK SYSTEM");
        System.out.println("==========================================");

        boolean running = true;

        while (running) {

            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. Hub Management (BST)");
            System.out.println("2. Parcel Tracking (HashMap)");
            System.out.println("3. Logistics Network (Graph)");
            System.out.println("4. Delivery Window Analysis");
            System.out.println("5. Route Palindrome Checker");
            System.out.println("6. Exit");
            System.out.println("================================");

            int choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    hubManagement();
                    break;

                case 2:
                    parcelTracking();
                    break;

                case 3:
                    logisticsNetwork();
                    break;

                case 4:
                    deliveryWindowAnalysis();
                    break;

                case 5:
                    routePalindrome();
                    break;

                case 6:
                    running = false;
                    System.out.println(
                            "\nThank you for using Logistics Network System!");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        sc.close();
    }

    // ==========================================
    // COMMON INPUT METHODS
    // ==========================================

    // Read integer safely
    private static int readInt(String message) {

        while (true) {

            System.out.print(message);

            String input = sc.nextLine().trim();

            try {

                return Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.println("Please enter a valid integer.");
            }
        }
    }

    // Read non-negative integer
    private static int readNonNegativeInt(String message) {

        while (true) {

            int value = readInt(message);

            if (value >= 0) {
                return value;
            }

            System.out.println("Value cannot be negative.");
        }
    }

    // Read positive integer
    private static int readPositiveInt(String message) {

        while (true) {

            int value = readInt(message);

            if (value > 0) {
                return value;
            }

            System.out.println("Enter a number greater than zero.");
        }
    }

    // Read non-empty text
    private static String readText(String message) {

        while (true) {

            System.out.print(message);

            String value = sc.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("Input cannot be empty.");
        }
    }

    // ==========================================
    // TASK 1: HUB MANAGEMENT - BST
    // ==========================================

    private static void hubManagement() {

        HubBST bst = new HubBST();

        System.out.println("\n========== HUB MANAGEMENT ==========");

        int count = readNonNegativeInt(
                "How many hubs do you want to insert? ");

        for (int i = 1; i <= count; i++) {

            System.out.println("\nEnter details of Hub " + i);

            String code = readText("Enter Hub Code: ");

            String name = readText("Enter Hub Name: ");

            boolean inserted = bst.insert(new Hub(code, name));

            if (inserted) {

                System.out.println("Hub inserted successfully!");

            } else {

                System.out.println(
                        "Duplicate hub code. Hub not inserted.");
            }
        }

        boolean active = true;

        while (active) {

            System.out.println("\n------ BST OPERATIONS ------");
            System.out.println("1. Insert Another Hub");
            System.out.println("2. Search Hub");
            System.out.println("3. Display Hubs in Sorted Order");
            System.out.println("4. Calculate BST Height");
            System.out.println("5. Back to Main Menu");

            int choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1: {

                    String code = readText("Enter Hub Code: ");

                    String name = readText("Enter Hub Name: ");

                    if (bst.insert(new Hub(code, name))) {

                        System.out.println("Hub inserted successfully!");

                    } else {

                        System.out.println("Duplicate hub code!");
                    }

                    break;
                }

                case 2: {

                    String code = readText(
                            "Enter Hub Code to Search: ");

                    Hub result = bst.search(code);

                    if (result != null) {

                        System.out.println("Hub Found: " + result);

                    } else {

                        System.out.println("Hub not found.");
                    }

                    break;
                }

                case 3:

                    bst.displayInOrder();
                    break;

                case 4:

                    System.out.println("BST Height: "
                            + bst.height());
                    break;

                case 5:

                    active = false;
                    break;

                default:

                    System.out.println("Invalid choice.");
            }
        }
    }

    // ==========================================
    // TASK 2: PARCEL TRACKING - HASHMAP
    // ==========================================

    private static void parcelTracking() {

        ParcelTracker tracker = new ParcelTracker();

        System.out.println("\n========== PARCEL TRACKING ==========");

        int count = readNonNegativeInt(
                "How many parcels do you want to register? ");

        for (int i = 1; i <= count; i++) {

            System.out.println("\nParcel " + i);

            String tracking = readText(
                    "Enter Tracking Number: ");

            String destination = readText(
                    "Enter Destination: ");

            if (tracker.registerParcel(tracking, destination)) {

                System.out.println("Parcel registered successfully!");

            } else {

                System.out.println(
                        "Registration failed. Duplicate or invalid details.");
            }
        }

        boolean active = true;

        while (active) {

            System.out.println("\n------ PARCEL OPERATIONS ------");
            System.out.println("1. Register Another Parcel");
            System.out.println("2. Search Parcel");
            System.out.println("3. Scan Parcel at Hub");
            System.out.println("4. Display All Parcels");
            System.out.println("5. Back to Main Menu");

            int choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1: {

                    String tracking = readText(
                            "Enter Tracking Number: ");

                    String destination = readText(
                            "Enter Destination: ");

                    if (tracker.registerParcel(tracking, destination)) {

                        System.out.println(
                                "Parcel registered successfully!");

                    } else {

                        System.out.println(
                                "Duplicate or invalid parcel details.");
                    }

                    break;
                }

                case 2: {

                    String tracking = readText(
                            "Enter Tracking Number: ");

                    tracker.searchParcel(tracking);

                    break;
                }

                case 3: {

                    String tracking = readText(
                            "Enter Tracking Number: ");

                    String hubCode = readText(
                            "Enter Hub Code: ");

                    tracker.scanParcel(tracking, hubCode);

                    break;
                }

                case 4:

                    tracker.displayAllParcels();
                    break;

                case 5:

                    active = false;
                    break;

                default:

                    System.out.println("Invalid choice.");
            }
        }
    }

    // ==========================================
    // TASK 3: GRAPH - BFS AND DFS
    // ==========================================

    private static void logisticsNetwork() {

        HubNetwork network = new HubNetwork();

        System.out.println("\n========== LOGISTICS NETWORK ==========");

        // Create hubs
        int hubCount = readNonNegativeInt(
                "Enter number of hubs: ");

        for (int i = 1; i <= hubCount; i++) {

            String code = readText(
                    "Enter Hub " + i + " Code: ");

            if (network.addHub(code)) {

                System.out.println("Hub added successfully!");

            } else {

                System.out.println("Duplicate or invalid hub code!");
            }
        }

        // Create routes
        int routeCount = readNonNegativeInt(
                "\nEnter number of direct routes: ");

        for (int i = 1; i <= routeCount; i++) {

            System.out.println("\nRoute " + i);

            String source = readText("Enter Source Hub: ");

            String destination = readText(
                    "Enter Destination Hub: ");

            if (network.addRoute(source, destination)) {

                System.out.println("Route added successfully!");

            } else {

                System.out.println(
                        "Invalid route, duplicate route or unknown hub.");
            }
        }

        // Display graph
        network.displayNetwork();

        boolean active = true;

        while (active) {

            System.out.println("\n------ GRAPH OPERATIONS ------");
            System.out.println("1. Find Shortest Route (BFS)");
            System.out.println("2. Find Reachable Hubs (DFS)");
            System.out.println("3. Display Network");
            System.out.println("4. Back to Main Menu");

            int choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1: {

                    String start = readText(
                            "Enter Starting Hub: ");

                    String destination = readText(
                            "Enter Destination Hub: ");

                    List<String> path =
                            network.shortestRouteBFS(
                                    start, destination);

                    if (path.isEmpty()) {

                        System.out.println(
                                "No route available between these hubs.");

                    } else {

                        System.out.println("\nShortest Route:");

                        System.out.println(
                                String.join(" -> ", path));

                        System.out.println("Minimum Hops: "
                                + (path.size() - 1));
                    }

                    break;
                }

                case 2: {

                    String start = readText(
                            "Enter Starting Hub for DFS: ");

                    List<String> reachable =
                            network.reachableHubsDFS(start);

                    if (reachable.isEmpty()) {

                        System.out.println("Hub not found.");

                    } else {

                        System.out.println(
                                "Reachable Hubs: " + reachable);
                    }

                    break;
                }

                case 3:

                    network.displayNetwork();
                    break;

                case 4:

                    active = false;
                    break;

                default:

                    System.out.println("Invalid choice.");
            }
        }
    }

    // ==========================================
    // TASK 4: SLIDING WINDOW
    // ==========================================

    private static void deliveryWindowAnalysis() {

        DeliveryWindowAnalyzer analyzer =
                new DeliveryWindowAnalyzer();

        System.out.println(
                "\n========== DELIVERY WINDOW ANALYSIS ==========");

        int hours;

        do {

            hours = readPositiveInt(
                    "Enter number of hours (minimum 4): ");

            if (hours < 4) {

                System.out.println(
                        "At least four hours are required.");
            }

        } while (hours < 4);

        int[] parcelCounts = new int[hours];

        System.out.println("\nEnter hourly parcel counts:");

        for (int i = 0; i < hours; i++) {

            parcelCounts[i] = readNonNegativeInt(
                    "Hour " + (i + 1) + ": ");
        }

        System.out.println("\nHourly Parcel Data:");

        for (int i = 0; i < hours; i++) {

            System.out.println("Hour " + (i + 1)
                    + ": " + parcelCounts[i]);
        }

        DeliveryWindowAnalyzer.WindowResult result =
                analyzer.findHighestAverageWindow(parcelCounts);

        if (result != null) {

            System.out.println(
                    "\nHighest Average Delivery Window:");

            System.out.println(result);
        }
    }

    // ==========================================
    // TASK 5: TWO POINTERS
    // ==========================================

    private static void routePalindrome() {

        RoutePalindromeChecker checker =
                new RoutePalindromeChecker();

        System.out.println(
                "\n========== ROUTE PALINDROME CHECKER ==========");

        boolean active = true;

        while (active) {

            String route = readText(
                    "Enter Route Description: ");

            checker.checkRoute(route);

            System.out.println("\n1. Check Another Route");
            System.out.println("2. Back to Main Menu");

            int choice = readInt("Enter your choice: ");

            if (choice == 2) {

                active = false;

            } else if (choice != 1) {

                System.out.println("Invalid choice. Returning to Main Menu.");

                active = false;
            }
        }
    }
}
