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

    public static void mergeSort(Order[] orders, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;

            mergeSort(orders, left, mid);       
            mergeSort(orders, mid + 1, right);  
            merge(orders, left, mid, right);     
        }
    }

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

    public static void main(String[] args) {
        int n = 10; 
        Order[] orders = new Order[n];

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

#Output

    Before Sorting:
Order_0 - 823456
Order_1 - 192837
Order_2 - 987654
Order_3 - 456789
Order_4 - 123456
Order_5 - 678901
Order_6 - 234567
Order_7 - 345678
Order_8 - 789012
Order_9 - 567890

After Sorting by Timestamp:
Order_4 - 123456
Order_1 - 192837
Order_6 - 234567
Order_7 - 345678
Order_3 - 456789
Order_9 - 567890
Order_5 - 678901
Order_0 - 823456
Order_8 - 789012
Order_2 - 987654

