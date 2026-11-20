
package hospital;

public class WalkInQueue {

    private Patient[] queue;
    private int front;
    private int rear;
    private int size;
    private int capacity;

    public WalkInQueue(int capacity) {
        this.capacity = capacity;
        queue = new Patient[capacity];

        front = 0;
        rear = -1;
        size = 0;
    }

    // Add patient to the queue
    public void enqueue(Patient patient) {

        if (size == capacity) {
            System.out.println("Walk-in queue is full!");
            return;
        }

        rear = (rear + 1) % capacity;
        queue[rear] = patient;
        size++;

        System.out.println(
            patient.getPatientId() + " added to walk-in queue."
        );
    }

    // Remove the first patient
    public Patient dequeue() {

        if (isEmpty()) {
            System.out.println("Walk-in queue is empty!");
            return null;
        }

        Patient patient = queue[front];

        queue[front] = null;
        front = (front + 1) % capacity;
        size--;

        return patient;
    }

    // View the first patient
    public Patient peek() {

        if (isEmpty()) {
            return null;
        }

        return queue[front];
    }

    // Check whether queue is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Check whether queue is full
    public boolean isFull() {
        return size == capacity;
    }

    // Return number of patients
    public int size() {
        return size;
    }

    // Display waiting patients
    public void display() {

        if (isEmpty()) {
            System.out.println("No patients in waiting queue.");
            return;
        }

        System.out.println("\nWALK-IN WAITING QUEUE");

        for (int i = 0; i < size; i++) {

            int index = (front + i) % capacity;

            System.out.println(queue[index]);
        }
    }
}
