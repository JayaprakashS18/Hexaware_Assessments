package sequejava;
	import java.util.Scanner;
	import java.util.Arrays;
	public class binarysearch {
	    public static void main(String[] args) {

	        Scanner sc = new Scanner(System.in);

	        System.out.print("Enter array size: ");
	        int n = sc.nextInt();

	        int[] arr = new int[n];

	        System.out.println("Enter array elements:");
	        for (int i = 0; i < n; i++) {
	            arr[i] = sc.nextInt();
	        }

	        Arrays.sort(arr);

	        System.out.println("Sorted array: " + Arrays.toString(arr));

	        System.out.print("Enter element to search: ");
	        int key = sc.nextInt();

	        int low = 0;
	        int high = n - 1;

	        while (low <= high) {

	            int mid = low + (high - low) / 2;

	            if (arr[mid] == key) {
	                System.out.println("Element found at index: " + mid);
	                sc.close();
	                return;
	            }
	            else if (arr[mid] < key) {
	                low = mid + 1;
	            }
	            else {
	                high = mid - 1;
	            }
	        }

	        System.out.println("Element not found");

	        sc.close();
	    }
	}
}
