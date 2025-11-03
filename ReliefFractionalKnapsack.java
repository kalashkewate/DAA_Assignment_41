import java.util.*;

public class ReliefFractionalKnapsack {

    static class Item {
        String name;
        double weight;
        double value;
        boolean divisible; // if true means can take fraction

        Item(String name, double weight, double value, boolean divisible) {
            if (weight <= 0) throw new IllegalArgumentException("Weight must be > 0");
            this.name = name;
            this.weight = weight;
            this.value = value;
            this.divisible = divisible;
        }

        double density() {
            return value / weight;
        }
    }
    public static double fillBoat(List<Item> items, double capacityKg) {
        if (capacityKg <= 0) {
            System.out.println("Boat capacity should be > 0.");
            return 0.0;
        }
        // Sort by value by weight in descending order
        items.sort((a, b) -> Double.compare(b.density(), a.density()));

        double remaining = capacityKg;
        double totalValue = 0.0;

        System.out.printf("boat capacity: %.2f kg%n", capacityKg);
        System.out.println("selected items (name : takenWeight kg -> gainedUtility)");

        for (Item it : items) {
            if (remaining <= 0) break;
            if (it.divisible) {
                // take fraction part if needed
                double takeWeight = Math.min(it.weight, remaining);
                double gainedValue = it.density() * takeWeight;
                totalValue += gainedValue;
                remaining -= takeWeight;

                double percent = (takeWeight / it.weight) * 100.0;
                System.out.printf("%s : %.2f kg -> %.2f utility (%.1f%% of item)%n",
                        it.name, takeWeight, gainedValue, percent);
            } else {
                // indivisible: only take whole if it is < capacity
                if (it.weight <= remaining) {
                    totalValue += it.value;
                    remaining -= it.weight;
                    System.out.printf("%s : %.2f kg -> %.2f utility (whole item)%n",
                            it.name, it.weight, it.value);
                } else {
                    System.out.printf("%s : SKIPPED (needs %.2f kg, only %.2f kg left)%n",
                            it.name, it.weight, remaining);
                }
            }
        }
        System.out.printf("total utility value loaded: %.2f%n", totalValue);
        System.out.printf("unused capacity remaining: %.2f kg%n", remaining);
        return totalValue;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Enter boat capacity W in kg ");
            double W = sc.nextDouble();
            System.out.print("Enter number of different item types n ");
            int n = sc.nextInt();
            sc.nextLine();

            List<Item> items = new ArrayList<>();
            for (int i = 1; i <= n; i++) {
                System.out.printf("Item %d name: ", i);
                String name = sc.nextLine().trim();
                System.out.printf("Item %d weight (kg): ", i);
                double w = sc.nextDouble();
                System.out.printf("Item %d utility value: ", i);
                double v = sc.nextDouble();
                sc.nextLine();
                System.out.printf("Is item %d divisible? (y/n): ", i);
                String d = sc.nextLine().trim().toLowerCase();
                boolean divisible = d.equals("y") || d.equals("yes");
                if (w <= 0) {
                    System.out.println("Weight should be > 0. Re-enter this item.");
                    i--;
                    continue;
                }
                items.add(new Item(name, w, v, divisible));
            }
            System.out.println();
            fillBoat(items, W);
        } catch (InputMismatchException ime) {
            System.out.println("Invalid input. Enter numbers where required.");
        } catch (IllegalArgumentException iae) {
            System.out.println("Error: " + iae.getMessage());
        } finally {
            sc.close();
        }
    }
}

#input

    Enter boat capacity W in kg 50
Enter number of different item types n 4
Item 1 name: Medical Kits
Item 1 weight (kg): 10
Item 1 utility value: 500
Is item 1 divisible? (y/n): n
Item 2 name: Food Packets
Item 2 weight (kg): 20
Item 2 utility value: 300
Is item 2 divisible? (y/n): y
Item 3 name: Water Bottles
Item 3 weight (kg): 15
Item 3 utility value: 200
Is item 3 divisible? (y/n): y
Item 4 name: Blankets
Item 4 weight (kg): 25
Item 4 utility value: 150
Is item 4 divisible? (y/n): n

    #Output
    boat capacity: 50.00 kg
selected items (name : takenWeight kg -> gainedUtility)
Medical Kits : 10.00 kg -> 500.00 utility (whole item)
Food Packets : 20.00 kg -> 300.00 utility (100.0% of item)
Water Bottles : 15.00 kg -> 200.00 utility (100.0% of item)
Blankets : SKIPPED (needs 25.00 kg, only 5.00 kg left)
total utility value loaded: 1000.00
unused capacity remaining: 5.00 kg

    
