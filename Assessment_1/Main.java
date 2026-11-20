
package hospital;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        List<Patient> patients = TestData.generatePatients();
        List<String> queries = TestData.generateSearchQueries();

        List<Patient> linearData = new ArrayList<>(patients);
        Collections.shuffle(linearData, new Random(42));

        List<Patient> binaryData = patients;

        List<Appointment> sampleAppointments =
                new ArrayList<>(TestData.generateSampleAppointments());

        List<Appointment> sortingAppointments =
                TestData.generateSortingAppointments();

        List<Doctor> doctors = new ArrayList<>();

        doctors.add(new Doctor(
                "D101",
                "Dr. Kumar",
                "Cardiology",
                LocalTime.of(9, 0),
                LocalTime.of(17, 0)
        ));

        System.out.println("\n======================================================");
        System.out.println("              HOSPITAL APPOINTMENT DESK");
        System.out.println("                 MANAGEMENT SYSTEM");
        System.out.println("======================================================");
        System.out.println(" System Status       : ONLINE");
        System.out.println(" Registered Patients : " + patients.size());
        System.out.println(" Available Doctors   : " + doctors.size());
        System.out.println(" System Ready!");
        System.out.println("======================================================");

        boolean running = true;

        while (running) {

            displayMenu();

            System.out.print("Enter your choice: ");
            String input = sc.nextLine().trim();

            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input! Please enter a number.");
                continue;
            }

            switch (choice) {

                case 1:
                    registerPatient(patients, linearData);
                    break;

                case 2:
                    searchPatient(binaryData);
                    break;

                case 3:
                    viewPatients(patients);
                    break;

                case 4:
                    registerDoctor(doctors);
                    break;

                case 5:
                    viewDoctors(doctors);
                    break;

                case 6:
                    bookAppointment(
                            patients,
                            doctors,
                            sampleAppointments
                    );
                    break;

                case 7:
                    viewAppointments(sampleAppointments);
                    break;

                case 8:
                    searchAppointment(sampleAppointments);
                    break;

                case 9:
                    cancelAppointment(sampleAppointments);
                    break;

                case 10:
                    runAlgorithmAnalysis(
                            linearData,
                            binaryData,
                            queries,
                            sampleAppointments,
                            sortingAppointments
                    );
                    break;

                case 0:
                    running = false;
                    System.out.println("\nThank you for using");
                    System.out.println("HOSPITAL APPOINTMENT DESK!");
                    System.out.println("Application closed successfully.");
                    break;

                default:
                    System.out.println(
                            "\nInvalid choice! Please select a valid option."
                    );
            }

            if (running) {
                System.out.println(
                        "\nPress ENTER to return to the dashboard..."
                );
                sc.nextLine();
            }
        }

        sc.close();
    }

    // Main dashboard
    private static void displayMenu() {

        System.out.println("\n");
        System.out.println("======================================================");
        System.out.println("                  RECEPTION DASHBOARD");
        System.out.println("======================================================");
        System.out.println("  1. Register New Patient");
        System.out.println("  2. Search Patient");
        System.out.println("  3. View Registered Patients");
        System.out.println("  4. Register New Doctor");
        System.out.println("  5. View Doctors");
        System.out.println("  6. Book New Appointment");
        System.out.println("  7. View Appointments");
        System.out.println("  8. Search Appointment");
        System.out.println("  9. Cancel Appointment");
        System.out.println(" 10. Algorithm Performance Analysis");
        System.out.println("------------------------------------------------------");
        System.out.println("  0. Exit Application");
        System.out.println("======================================================");
    }

    // Register a new patient
    private static void registerPatient(
            List<Patient> patients,
            List<Patient> linearData) {

        System.out.println("\n========== PATIENT REGISTRATION ==========");

        String name;

        while (true) {
            System.out.print("Enter Patient Name: ");
            name = sc.nextLine().trim();

            if (!name.isEmpty()) {
                break;
            }

            System.out.println("Name cannot be empty. Try again.");
        }

        int age;

        while (true) {
            System.out.print("Enter Patient Age: ");

            try {
                age = Integer.parseInt(sc.nextLine().trim());

                if (age > 0 && age <= 120) {
                    break;
                }

                System.out.println("Enter a valid age between 1 and 120.");

            } catch (NumberFormatException e) {
                System.out.println("Invalid age. Please enter a number.");
            }
        }

        String phone;

        while (true) {
            System.out.print("Enter Phone Number: ");
            phone = sc.nextLine().trim();

            if (phone.matches("[0-9]{10}")) {
                break;
            }

            System.out.println("Enter a valid 10-digit phone number.");
        }

        int nextId = 1;

        for (Patient patient : patients) {
            String id = patient.getPatientId();

            if (id.matches("P[0-9]+")) {
                int currentId = Integer.parseInt(id.substring(1));
                nextId = Math.max(nextId, currentId + 1);
            }
        }

        String patientId = String.format("P%05d", nextId);

        Patient newPatient =
                new Patient(patientId, name, age, phone);

        patients.add(newPatient);
        linearData.add(newPatient);

        Collections.shuffle(linearData, new Random(42));

        System.out.println("\nPATIENT REGISTERED SUCCESSFULLY");
        System.out.println(newPatient);
        System.out.println("Total Registered Patients: " + patients.size());
    }

    // Search patient using Binary Search
    private static void searchPatient(List<Patient> binaryData) {

        System.out.println("\n========== PATIENT SEARCH ==========");
        System.out.print("Enter Patient ID: ");

        String patientId = sc.nextLine().trim().toUpperCase();

        PatientSearch.SearchResult result =
                PatientSearch.binarySearch(binaryData, patientId);

        if (result.getPatient() != null) {
            System.out.println("\nPatient Found Successfully!");
            System.out.println(result.getPatient());
            System.out.println("Search Method : Binary Search");
            System.out.println("Comparisons   : " + result.getComparisons());
        } else {
            System.out.println("\nPatient not found!");
        }
    }

    // Display registered patients
    private static void viewPatients(List<Patient> patients) {

        System.out.println("\n========== REGISTERED PATIENTS ==========");
        System.out.println("Total Registered Patients: " + patients.size());

        int limit = Math.min(10, patients.size());

        for (int i = 0; i < limit; i++) {
            System.out.println((i + 1) + ". " + patients.get(i));
        }

        if (patients.size() > 10) {
            System.out.println("Showing first 10 records only.");
        }
    }

    // Register a new doctor
    private static void registerDoctor(List<Doctor> doctors) {

        System.out.println("\n========== DOCTOR REGISTRATION ==========");

        String name;

        while (true) {
            System.out.print("Enter Doctor Name: ");
            name = sc.nextLine().trim();

            if (!name.isEmpty()) {
                break;
            }

            System.out.println("Doctor name cannot be empty.");
        }

        String specialization;

        while (true) {
            System.out.print("Enter Specialization: ");
            specialization = sc.nextLine().trim();

            if (!specialization.isEmpty()) {
                break;
            }

            System.out.println("Specialization cannot be empty.");
        }

        LocalTime shiftStart;
        LocalTime shiftEnd;

        while (true) {
            try {
                System.out.print("Enter Shift Start Time (HH:mm): ");
                shiftStart = LocalTime.parse(sc.nextLine().trim());

                System.out.print("Enter Shift End Time (HH:mm): ");
                shiftEnd = LocalTime.parse(sc.nextLine().trim());

                if (shiftEnd.isAfter(shiftStart)) {
                    break;
                }

                System.out.println("Shift end must be after shift start.");

            } catch (DateTimeParseException e) {
                System.out.println("Invalid time. Use HH:mm format.");
            }
        }

        int nextId = 1;

        for (Doctor doctor : doctors) {
            String id = doctor.getDoctorId();

            if (id.matches("D[0-9]+")) {
                int currentId = Integer.parseInt(id.substring(1));
                nextId = Math.max(nextId, currentId + 1);
            }
        }

        String doctorId = String.format("D%03d", nextId);

        try {
            Doctor newDoctor = new Doctor(
                    doctorId,
                    name,
                    specialization,
                    shiftStart,
                    shiftEnd
            );

            doctors.add(newDoctor);

            System.out.println("\nDOCTOR REGISTERED SUCCESSFULLY");
            System.out.println(newDoctor);
            System.out.println("Total Available Doctors: " + doctors.size());

        } catch (IllegalArgumentException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    // Display all doctors
    private static void viewDoctors(List<Doctor> doctors) {

        System.out.println("\n========== REGISTERED DOCTORS ==========");
        System.out.println("Total Available Doctors: " + doctors.size());

        if (doctors.isEmpty()) {
            System.out.println("No doctors registered.");
            return;
        }

        for (int i = 0; i < doctors.size(); i++) {
            System.out.println((i + 1) + ". " + doctors.get(i));
        }
    }

    // Book a new appointment
    private static void bookAppointment(
            List<Patient> patients,
            List<Doctor> doctors,
            List<Appointment> appointments) {

        System.out.println("\n========================================");
        System.out.println("          BOOK NEW APPOINTMENT");
        System.out.println("========================================");

        if (patients.isEmpty() || doctors.isEmpty()) {
            System.out.println("Patients or doctors are not available.");
            return;
        }

        // Step 1: Select patient
        System.out.print("Enter Patient ID: ");
        String patientId = sc.nextLine().trim().toUpperCase();

        Patient selectedPatient = null;

        for (Patient patient : patients) {
            if (patient.getPatientId().equalsIgnoreCase(patientId)) {
                selectedPatient = patient;
                break;
            }
        }

        if (selectedPatient == null) {
            System.out.println("Patient not found. Register the patient first.");
            return;
        }

        // Step 2: Display and select doctor
        System.out.println("\nAVAILABLE DOCTORS");

        for (Doctor doctor : doctors) {
            System.out.println(doctor);
        }

        System.out.print("\nEnter Doctor ID: ");
        String doctorId = sc.nextLine().trim().toUpperCase();

        Doctor selectedDoctor = null;

        for (Doctor doctor : doctors) {
            if (doctor.getDoctorId().equalsIgnoreCase(doctorId)) {
                selectedDoctor = doctor;
                break;
            }
        }

        if (selectedDoctor == null) {
            System.out.println("Doctor not found.");
            return;
        }

        // Step 3: Appointment date
        LocalDate appointmentDate;

        while (true) {
            try {
                System.out.print("Enter Appointment Date (yyyy-MM-dd): ");
                appointmentDate =
                        LocalDate.parse(sc.nextLine().trim());

                break;

            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Use yyyy-MM-dd format.");
            }
        }

        // Step 4: Appointment start time
        LocalTime startTime;

        while (true) {
            try {
                System.out.print("Enter Start Time (HH:mm): ");
                startTime = LocalTime.parse(sc.nextLine().trim());
                break;

            } catch (DateTimeParseException e) {
                System.out.println("Invalid time. Use HH:mm format.");
            }
        }

        // Step 5: Appointment duration
        int duration;

        while (true) {
            System.out.print("Enter Duration in Minutes (15-180): ");

            try {
                duration = Integer.parseInt(sc.nextLine().trim());

                if (duration >= 15 && duration <= 180) {
                    break;
                }

                System.out.println("Duration must be between 15 and 180.");

            } catch (NumberFormatException e) {
                System.out.println("Enter a valid numeric duration.");
            }
        }

        LocalDateTime startDateTime =
                LocalDateTime.of(appointmentDate, startTime);

        LocalDateTime endDateTime =
                startDateTime.plusMinutes(duration);

        // Step 6: Check whether appointment is in the past
        if (startDateTime.isBefore(LocalDateTime.now())) {
            System.out.println("Cannot book an appointment in the past.");
            return;
        }

        // Step 7: Validate doctor working hours
        if (startTime.isBefore(selectedDoctor.getShiftStart())
                || endDateTime.toLocalDate().isAfter(appointmentDate)
                || !endDateTime.toLocalTime()
                        .isAfter(selectedDoctor.getShiftStart())
                || endDateTime.toLocalTime()
                        .isAfter(selectedDoctor.getShiftEnd())) {

            System.out.println("Appointment is outside the doctor's shift.");
            System.out.println(
                    "Doctor's shift: "
                    + selectedDoctor.getShiftStart()
                    + " - "
                    + selectedDoctor.getShiftEnd()
            );
            return;
        }

        // Step 8: Check for overlapping appointments
        for (Appointment existing : appointments) {

            boolean overlaps =
                    startDateTime.isBefore(existing.getEndTime())
                    && endDateTime.isAfter(existing.getStartTime());

            if (overlaps) {

                if (existing.getDoctorId().equalsIgnoreCase(doctorId)) {
                    System.out.println(
                            "Booking rejected! Doctor already has an "
                            + "appointment during this time."
                    );
                    return;
                }

                if (existing.getPatientId().equalsIgnoreCase(patientId)) {
                    System.out.println(
                            "Booking rejected! Patient already has an "
                            + "appointment during this time."
                    );
                    return;
                }
            }
        }

        // Step 9: Select urgency
        System.out.println("\nSelect Appointment Urgency:");
        System.out.println("1. CRITICAL");
        System.out.println("2. HIGH");
        System.out.println("3. NORMAL");

        UrgencyLevel urgency;

        while (true) {
            System.out.print("Enter urgency choice: ");
            String urgencyInput = sc.nextLine().trim();

            switch (urgencyInput) {
                case "1":
                    urgency = UrgencyLevel.CRITICAL;
                    break;
                case "2":
                    urgency = UrgencyLevel.HIGH;
                    break;
                case "3":
                    urgency = UrgencyLevel.NORMAL;
                    break;
                default:
                    System.out.println("Invalid choice. Select 1, 2 or 3.");
                    continue;
            }

            break;
        }

        // Step 10: Generate unique appointment ID
        int nextId = 1;

        for (Appointment appointment : appointments) {
            String id = appointment.getAppointmentId();

            if (id.matches("A[0-9]+")) {
                int currentId = Integer.parseInt(id.substring(1));
                nextId = Math.max(nextId, currentId + 1);
            }
        }

        String appointmentId = String.format("A%03d", nextId);

        // Generate slot ID from doctor and appointment start time
        String slotId = doctorId + "_"
                + startDateTime.toString().replace(":", "")
                                  .replace("-", "")
                                  .replace("T", "_");

        // Step 11: Create appointment
        try {
            Appointment newAppointment = new Appointment(
                    appointmentId,
                    patientId,
                    doctorId,
                    slotId,
                    urgency,
                    LocalDateTime.now(),
                    startDateTime,
                    endDateTime
            );

            appointments.add(newAppointment);

            System.out.println("\n========================================");
            System.out.println("    APPOINTMENT BOOKED SUCCESSFULLY");
            System.out.println("========================================");
            System.out.println("Appointment ID : " + appointmentId);
            System.out.println("Patient        : " + selectedPatient.getName());
            System.out.println("Patient ID     : " + patientId);
            System.out.println("Doctor         : " + selectedDoctor.getName());
            System.out.println("Doctor ID      : " + doctorId);
            System.out.println("Date           : " + appointmentDate);
            System.out.println("Start Time     : " + startTime);
            System.out.println("End Time       : " + endDateTime.toLocalTime());
            System.out.println("Duration       : " + duration + " minutes");
            System.out.println("Urgency        : " + urgency);
            System.out.println("Slot ID        : " + slotId);
            System.out.println("========================================");

        } catch (IllegalArgumentException e) {
            System.out.println("Appointment booking failed: " + e.getMessage());
        }
    }

    // Display all appointments
    private static void viewAppointments(
            List<Appointment> appointments) {

        System.out.println("\n========== APPOINTMENT DETAILS ==========");
        System.out.println("Total Appointments: " + appointments.size());

        if (appointments.isEmpty()) {
            System.out.println("No appointments available.");
            return;
        }

        for (Appointment appointment : appointments) {
            System.out.println(appointment);
        }
    }

    // Search appointment by ID
    private static void searchAppointment(
            List<Appointment> appointments) {

        System.out.println("\n========== SEARCH APPOINTMENT ==========");
        System.out.print("Enter Appointment ID: ");

        String appointmentId = sc.nextLine().trim().toUpperCase();

        for (Appointment appointment : appointments) {

            if (appointment.getAppointmentId()
                    .equalsIgnoreCase(appointmentId)) {

                System.out.println("\nAppointment Found!");
                System.out.println(appointment);
                return;
            }
        }

        System.out.println("Appointment not found.");
    }

    // Cancel appointment by ID
    private static void cancelAppointment(
            List<Appointment> appointments) {

        System.out.println("\n========== CANCEL APPOINTMENT ==========");
        System.out.print("Enter Appointment ID to cancel: ");

        String appointmentId = sc.nextLine().trim().toUpperCase();

        for (int i = 0; i < appointments.size(); i++) {

            Appointment appointment = appointments.get(i);

            if (appointment.getAppointmentId()
                    .equalsIgnoreCase(appointmentId)) {

                appointments.remove(i);

                System.out.println(
                        "Appointment " + appointmentId
                        + " cancelled successfully."
                );
                return;
            }
        }

        System.out.println("Appointment not found.");
    }

    // Execute all completed algorithm implementations
    private static void runAlgorithmAnalysis(
            List<Patient> linearData,
            List<Patient> binaryData,
            List<String> queries,
            List<Appointment> sampleAppointments,
            List<Appointment> sortingAppointments) {

        System.out.println("\n========================================");
        System.out.println("       ALGORITHM PERFORMANCE ANALYSIS");
        System.out.println("========================================");

        System.out.println("\n1. PATIENT SEARCH PERFORMANCE");

        PatientSearch.comparePerformance(
                linearData,
                binaryData,
                queries
        );

        System.out.println("\n2. APPOINTMENT SORTING PERFORMANCE");

        AppointmentSorting.comparePerformance(
                sortingAppointments
        );

        System.out.println("\n3. DOUBLE-BOOKED SLOT DETECTION");

        DuplicateAppointmentDetector.comparePerformance(
                sampleAppointments
        );

        System.out.println("\n4. WALK-IN QUEUE");

        WalkInQueue queue = new WalkInQueue(5);

        queue.enqueue(linearData.get(0));
        queue.enqueue(linearData.get(1));
        queue.enqueue(linearData.get(2));

        queue.display();

        System.out.println("\n5. RECENTLY VIEWED PATIENT STACK");

        RecentlyViewedStack stack =
                new RecentlyViewedStack(5);

        stack.push(linearData.get(0));
        stack.push(linearData.get(1));
        stack.push(linearData.get(2));

        stack.display();

        System.out.println("\n6. MAXIMUM APPOINTMENT SCHEDULING");

        AppointmentScheduling.comparePerformance(
                sortingAppointments
        );

        System.out.println("\n========================================");
        System.out.println("       ANALYSIS COMPLETED");
        System.out.println("========================================");
    }
}
