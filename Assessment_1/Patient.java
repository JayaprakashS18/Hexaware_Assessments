
package hospital;

public class Patient {

    private String patientId;
    private String name;
    private int age;
    private String phoneNumber;

    public Patient(String patientId, String name, int age, String phoneNumber) {

        if (patientId == null || patientId.isBlank()) {
            throw new IllegalArgumentException("Patient ID cannot be empty");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Patient name cannot be empty");
        }

        if (age <= 0) {
            throw new IllegalArgumentException("Invalid patient age");
        }

        this.patientId = patientId;
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "Patient ID: " + patientId +
               " | Name: " + name +
               " | Age: " + age +
               " | Phone: " + phoneNumber;
    }
}
