
package hospital;

import java.time.LocalTime;

public class Doctor {

    private String doctorId;
    private String name;
    private String specialization;
    private LocalTime shiftStart;
    private LocalTime shiftEnd;

    public Doctor(String doctorId, String name,
                  String specialization,
                  LocalTime shiftStart,
                  LocalTime shiftEnd) {

        if (doctorId == null || doctorId.isBlank()) {
            throw new IllegalArgumentException("Doctor ID cannot be empty");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Doctor name cannot be empty");
        }

        if (specialization == null || specialization.isBlank()) {
            throw new IllegalArgumentException("Specialization cannot be empty");
        }

        if (shiftStart == null || shiftEnd == null ||
            !shiftEnd.isAfter(shiftStart)) {
            throw new IllegalArgumentException("Invalid doctor shift");
        }

        this.doctorId = doctorId;
        this.name = name;
        this.specialization = specialization;
        this.shiftStart = shiftStart;
        this.shiftEnd = shiftEnd;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public LocalTime getShiftStart() {
        return shiftStart;
    }

    public LocalTime getShiftEnd() {
        return shiftEnd;
    }

    @Override
    public String toString() {
        return "Doctor ID: " + doctorId +
               " | Name: " + name +
               " | Specialization: " + specialization +
               " | Shift: " + shiftStart + " - " + shiftEnd;
    }
}
