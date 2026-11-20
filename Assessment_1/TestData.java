
package hospital;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestData {

    private static final int PATIENT_COUNT = 20000;

    // ==========================================
    // 1. GENERATE 20,000 PATIENT RECORDS
    // ==========================================

    public static List<Patient> generatePatients() {

        List<Patient> patients = new ArrayList<>();

        String[] names = {
            "Arun", "Bala", "Charan", "David",
            "Ezhil", "Kumar", "Manoj", "Rahul"
        };

        Random random = new Random(42);

        for (int i = 1; i <= PATIENT_COUNT; i++) {

            String patientId = String.format("P%05d", i);

            String name = names[random.nextInt(names.length)]
                          + " " + i;

            int age = 18 + random.nextInt(63);

            String phone = "90000" + String.format("%05d", i);

            patients.add(
                new Patient(patientId, name, age, phone)
            );
        }

        return patients;
    }

    // ==========================================
    // 2. GENERATE 1,000 SEARCH QUERIES
    // ==========================================

    public static List<String> generateSearchQueries() {

        List<String> queries = new ArrayList<>();

        Random random = new Random(100);

        for (int i = 0; i < 1000; i++) {

            int patientNumber = 1 + random.nextInt(PATIENT_COUNT);

            String patientId = String.format(
                "P%05d", patientNumber
            );

            queries.add(patientId);
        }

        return queries;
    }

    // ==========================================
    // 3. GENERATE SAMPLE APPOINTMENTS
    // ==========================================

    public static List<Appointment> generateSampleAppointments() {

        List<Appointment> appointments = new ArrayList<>();

        LocalDateTime base = LocalDateTime.of(
            2026, 11, 11, 9, 0
        );

        // Appointment 1
        appointments.add(new Appointment(
            "A101",
            "P00001",
            "D101",
            "SLOT01",
            UrgencyLevel.NORMAL,
            base.minusMinutes(20),
            base,
            base.plusHours(1)
        ));

        // Appointment 2
        appointments.add(new Appointment(
            "A102",
            "P00002",
            "D101",
            "SLOT02",
            UrgencyLevel.CRITICAL,
            base.minusMinutes(15),
            base.plusMinutes(30),
            base.plusHours(1)
        ));

        // Appointment 3
        appointments.add(new Appointment(
            "A103",
            "P00003",
            "D101",
            "SLOT03",
            UrgencyLevel.HIGH,
            base.minusMinutes(10),
            base.plusHours(1),
            base.plusHours(2)
        ));

        // Appointment 4 - Intentional duplicate
        appointments.add(new Appointment(
            "A104",
            "P00001",
            "D101",
            "SLOT01",
            UrgencyLevel.NORMAL,
            base.minusMinutes(5),
            base,
            base.plusHours(1)
        ));

        // Appointment 5
        appointments.add(new Appointment(
            "A105",
            "P00004",
            "D101",
            "SLOT05",
            UrgencyLevel.HIGH,
            base,
            base.plusHours(2),
            base.plusHours(3)
        ));

        return appointments;
    }

    // ==========================================
    // 4. GENERATE 1,000 SORTING APPOINTMENTS
    // ==========================================

    public static List<Appointment> generateSortingAppointments() {

        List<Appointment> appointments = new ArrayList<>();

        Random random = new Random(200);

        LocalDateTime base = LocalDateTime.of(
            2026, 11, 11, 9, 0
        );

        UrgencyLevel[] levels = UrgencyLevel.values();

        for (int i = 1; i <= 1000; i++) {

            // Unique appointment ID
            String appointmentId = String.format(
                "AS%04d", i
            );

            // Random patient ID
            int patientNumber = 1 + random.nextInt(PATIENT_COUNT);

            String patientId = String.format(
                "P%05d", patientNumber
            );

            // Random urgency level
            UrgencyLevel urgency =
                    levels[random.nextInt(levels.length)];

            // Appointment start time between 9 AM and 4 PM
            int startOffset = random.nextInt(421);

            LocalDateTime startTime =
                    base.plusMinutes(startOffset);

            // Random appointment duration: 15 to 60 minutes
            int duration = 15 + random.nextInt(46);

            LocalDateTime endTime =
                    startTime.plusMinutes(duration);

            // Arrival time: 0 to 60 minutes before appointment
            int arrivalOffset = random.nextInt(61);

            LocalDateTime arrivalTime =
                    startTime.minusMinutes(arrivalOffset);

            // Unique slot ID
            String slotId = String.format(
                "SORT%04d", i
            );

            Appointment appointment = new Appointment(
                appointmentId,
                patientId,
                "D101",
                slotId,
                urgency,
                arrivalTime,
                startTime,
                endTime
            );

            appointments.add(appointment);
        }

        return appointments;
    }
}
