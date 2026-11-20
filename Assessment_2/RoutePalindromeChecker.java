
package logistics;

public class RoutePalindromeChecker {

    // Check palindrome
    public boolean isPalindrome(String route) {

        if (route == null) {
            return false;
        }

        int left = 0;

        int right = route.length() - 1;

        while (left < right) {

            // Skip non-alphanumeric characters on left
            while (left < right
                    && !Character.isLetterOrDigit(
                            route.charAt(left))) {

                left++;
            }

            // Skip non-alphanumeric characters on right
            while (left < right
                    && !Character.isLetterOrDigit(
                            route.charAt(right))) {

                right--;
            }

            // Compare without case sensitivity
            char leftChar = Character.toLowerCase(
                    route.charAt(left));

            char rightChar = Character.toLowerCase(
                    route.charAt(right));

            if (leftChar != rightChar) {
                return false;
            }

            left++;

            right--;
        }

        return true;
    }

    // Display result
    public void checkRoute(String route) {

        System.out.println("\nRoute Description: " + route);

        if (isPalindrome(route)) {

            System.out.println(
                    "Result: The route is a palindrome.");

        } else {

            System.out.println(
                    "Result: The route is not a palindrome.");
        }
    }
}
