import java.util.*;

class Order {
    long timestamp;
    String orderId;

    Order(long timestamp, String orderId) {
        this.timestamp = timestamp;
        this.orderId = orderId;
    }
}

public class MergeSortOrders {

    // Merge Sort Function
    public static void mergeSort(Order[] orders, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;

            mergeSort(orders, left, mid);        // Sort left half
            mergeSort(orders, mid + 1, right);   // Sort right half
            merge(orders, left, mid, right);     // Merge halves
        }
    }

    // Merge two sorted subarrays
    private static void merge(Order[] orders, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        Order[] leftArray = new Order[n1];
        Order[] rightArray = new Order[n2];

        for (int i = 0; i < n1; i++)
            leftArray[i] = orders[left + i];
        for (int j = 0; j < n2; j++)
            rightArray[j] = orders[mid + 1 + j];

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            if (leftArray[i].timestamp <= rightArray[j].timestamp) {
                orders[k++] = leftArray[i++];
            } else {
                orders[k++] = rightArray[j++];
            }
        }

        while (i < n1) {
            orders[k++] = leftArray[i++];
        }

        while (j < n2) {
            orders[k++] = rightArray[j++];
        }
    }

    // Driver Code
    public static void main(String[] args) {
        int n = 10; // Can go up to 1,000,000 for testing
        Order[] orders = new Order[n];

        // Generate random timestamps for testing
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            orders[i] = new Order(rand.nextInt(1000000), "Order_" + i);
        }

        System.out.println("Before Sorting:");
        for (Order o : orders) {
            System.out.println(o.orderId + " - " + o.timestamp);
        }

        mergeSort(orders, 0, orders.length - 1);

        System.out.println("\nAfter Sorting by Timestamp:");
        for (Order o : orders) {
            System.out.println(o.orderId + " - " + o.timestamp);
        }
    }
}
