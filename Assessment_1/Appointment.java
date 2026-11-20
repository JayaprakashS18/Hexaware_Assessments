
package hospital;

import java.time.LocalDateTime;
import java.time.Duration;

public class Appointment {

    private String appointmentId;
    private String patientId;
    private String doctorId;
    private String slotId;

    private UrgencyLevel urgency;

    private LocalDateTime arrivalTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Appointment(String appointmentId,
                       String patientId,
                       String doctorId,
                       String slotId,
                       UrgencyLevel urgency,
                       LocalDateTime arrivalTime,
                       LocalDateTime startTime,
                       LocalDateTime endTime) {

        if (appointmentId == null || appointmentId.isBlank() ||
            patientId == null || patientId.isBlank() ||
            doctorId == null || doctorId.isBlank() ||
            slotId == null || slotId.isBlank()) {

            throw new IllegalArgumentException("Invalid appointment ID details");
        }

        if (urgency == null || arrivalTime == null ||
            startTime == null || endTime == null) {

            throw new IllegalArgumentException("Appointment details cannot be null");
        }

        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.slotId = slotId;
        this.urgency = urgency;
        this.arrivalTime = arrivalTime;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getSlotId() {
        return slotId;
    }

    public UrgencyLevel getUrgency() {
        return urgency;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public long getDurationMinutes() {
        return Duration.between(startTime, endTime).toMinutes();
    }

    @Override
    public String toString() {

        return "Appointment ID: " + appointmentId +
               " | Patient: " + patientId +
               " | Doctor: " + doctorId +
               " | Slot: " + slotId +
               " | Urgency: " + urgency +
               " | Arrival: " + arrivalTime +
               " | Appointment: " + startTime + " - " + endTime;
    }
}
