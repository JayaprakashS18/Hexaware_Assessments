
package hospital;

public class RecentlyViewedStack {

    private Patient[] stack;
    private int top;
    private int capacity;

    public RecentlyViewedStack(int capacity) {
        this.capacity = capacity;
        stack = new Patient[capacity];

        top = -1;
    }

    // Add recently viewed patient
    public void push(Patient patient) {

        if (isFull()) {
            System.out.println("Recently viewed stack is full!");
            return;
        }

        top++;
        stack[top] = patient;

        System.out.println(
            patient.getPatientId() + " added to recently viewed stack."
        );
    }

    // Remove most recently viewed patient
    public Patient pop() {

        if (isEmpty()) {
            System.out.println("Recently viewed stack is empty!");
            return null;
        }

        Patient patient = stack[top];

        stack[top] = null;
        top--;

        return patient;
    }

    // View most recently viewed patient
    public Patient peek() {

        if (isEmpty()) {
            return null;
        }

        return stack[top];
    }

    // Check whether stack is empty
    public boolean isEmpty() {
        return top == -1;
    }

    // Check whether stack is full
    public boolean isFull() {
        return top == capacity - 1;
    }

    // Return number of patients
    public int size() {
        return top + 1;
    }

    // Display recently viewed patients
    public void display() {

        if (isEmpty()) {
            System.out.println("No recently viewed patients.");
            return;
        }

        System.out.println("\nRECENTLY VIEWED PATIENTS");

        for (int i = top; i >= 0; i--) {
            System.out.println(stack[i]);
        }
    }
}
