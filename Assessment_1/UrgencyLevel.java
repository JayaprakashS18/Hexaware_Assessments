
package hospital;

public enum UrgencyLevel {

    CRITICAL(1),
    HIGH(2),
    NORMAL(3);

    private final int priority;

    UrgencyLevel(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
