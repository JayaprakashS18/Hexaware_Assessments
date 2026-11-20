
package logistics;

public class Hub {

    private String hubCode;
    private String hubName;

    // Constructor
    public Hub(String hubCode, String hubName) {
        this.hubCode = hubCode;
        this.hubName = hubName;
    }

    // Getters
    public String getHubCode() {
        return hubCode;
    }

    public String getHubName() {
        return hubName;
    }

    // Display hub details
    @Override
    public String toString() {
        return "Hub Code: " + hubCode
                + ", Hub Name: " + hubName;
    }
}
