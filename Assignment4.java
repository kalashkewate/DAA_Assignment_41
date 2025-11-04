public class EmergencyTrafficSystem {

    static final int MAX_NODES = 100;

    int[][] graph;
    int totalNodes;
    String[] nodeNames;

    public EmergencyTrafficSystem(int nodes) {
        this.totalNodes = nodes;
        this.graph = new int[nodes][nodes];
        this.nodeNames = new String[nodes];

        for (int i = 0; i < nodes; i++) {
            for (int j = 0; j < nodes; j++) {
                if (i == j) {
                    graph[i][j] = 0;
                } else {
                    graph[i][j] = Integer.MAX_VALUE;
                }
            }
        }
    }

    public void setNodeName(int nodeId, String name) {
        nodeNames[nodeId] = name;
    }

    public void addRoad(int from, int to, int time) {
        graph[from][to] = time;
        graph[to][from] = time; 
    }

    public void updateTraffic(int from, int to, int newTime) {
        graph[from][to] = newTime;
        graph[to][from] = newTime;
        System.out.println("\n>>> Traffic Updated: Road between " + nodeNames[from] +
                " and " + nodeNames[to] + " now takes " + newTime + " minutes");
    }

    private int findMinDistanceNode(int[] distance, boolean[] visited) {
        int minDist = Integer.MAX_VALUE;
        int minIndex = -1;

        for (int i = 0; i < totalNodes; i++) {
            if (!visited[i] && distance[i] < minDist) {
                minDist = distance[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    public void findShortestPath(int source, int[] hospitals, int hospitalCount) {

        int[] distance = new int[totalNodes];
        boolean[] visited = new boolean[totalNodes];
        int[] parent = new int[totalNodes];

        for (int i = 0; i < totalNodes; i++) {
            distance[i] = Integer.MAX_VALUE;
            visited[i] = false;
            parent[i] = -1;
        }
        distance[source] = 0;

        for (int count = 0; count < totalNodes - 1; count++) {

            int u = findMinDistanceNode(distance, visited);

            if (u == -1) break;

            visited[u] = true;

            for (int v = 0; v < totalNodes; v++) {
                if (!visited[v] && graph[u][v] != Integer.MAX_VALUE &&
                        distance[u] != Integer.MAX_VALUE &&
                        distance[u] + graph[u][v] < distance[v]) {

                    distance[v] = distance[u] + graph[u][v];
                    parent[v] = u;
                }
            }
        }

        displayResults(source, hospitals, hospitalCount, distance, parent);
    }

    private void displayResults(int source, int[] hospitals, int hospitalCount,
                                int[] distance, int[] parent) {

        System.out.println("\n========================================");
        System.out.println("  EMERGENCY ROUTE CALCULATION");
        System.out.println("========================================");
        System.out.println("Ambulance Location: " + nodeNames[source]);
        System.out.println();

        int nearestHospital = -1;
        int minTime = Integer.MAX_VALUE;

        System.out.println("Distances to All Hospitals:");
        System.out.println("----------------------------------------");

        for (int i = 0; i < hospitalCount; i++) {
            int hospital = hospitals[i];
            if (distance[hospital] < minTime) {
                minTime = distance[hospital];
                nearestHospital = hospital;
            }

            System.out.printf("  %s: %d minutes", nodeNames[hospital], distance[hospital]);
            if (hospital == nearestHospital && i == hospitalCount - 1) {
                System.out.print(" <- NEAREST");
            }
            System.out.println();
        }

        if (nearestHospital != -1) {
            System.out.println("\n========================================");
            System.out.println("  OPTIMAL ROUTE (SHORTEST TIME)");
            System.out.println("========================================");
            System.out.println("Destination: " + nodeNames[nearestHospital]);
            System.out.println("Total Time: " + distance[nearestHospital] + " minutes");
            System.out.println();

            showPath(source, nearestHospital, parent);
        }
    }

    private void showPath(int source, int destination, int[] parent) {

        int[] path = new int[totalNodes];
        int pathLength = 0;

        int current = destination;
        while (current != -1) {
            path[pathLength++] = current;
            current = parent[current];
        }

        System.out.println("Route Navigation:");
        System.out.println("----------------------------------------");

        for (int i = pathLength - 1; i >= 0; i--) {
            if (i == pathLength - 1) {
                System.out.println("START: " + nodeNames[path[i]]);
            } else if (i == 0) {
                System.out.println("   |");
                System.out.println("   V");
                System.out.println("END:   " + nodeNames[path[i]] + " (HOSPITAL)");
            } else {
                System.out.println("   |");
                System.out.println("   V");
                System.out.println("       " + nodeNames[path[i]]);
            }
        }
        System.out.println("----------------------------------------");
    }

    public static void main(String[] args) {

        System.out.println("\n***** SMART TRAFFIC MANAGEMENT SYSTEM *****");
        System.out.println("    FOR EMERGENCY VEHICLES");
        System.out.println("*******************************************\n");

        EmergencyTrafficSystem system = new EmergencyTrafficSystem(10);

        system.setNodeName(0, "Central Square");
        system.setNodeName(1, "Main Street");
        system.setNodeName(2, "Park Avenue");
        system.setNodeName(3, "City Hospital");
        system.setNodeName(4, "Market Junction");
        system.setNodeName(5, "Tech Park");
        system.setNodeName(6, "General Hospital");
        system.setNodeName(7, "University Circle");
        system.setNodeName(8, "Sports Complex");
        system.setNodeName(9, "Metro Hospital");

        system.addRoad(0, 1, 5);
        system.addRoad(0, 2, 3);
        system.addRoad(1, 3, 7);
        system.addRoad(1, 4, 4);
        system.addRoad(2, 4, 2);
        system.addRoad(2, 5, 6);
        system.addRoad(3, 6, 8);
        system.addRoad(4, 6, 5);
        system.addRoad(4, 7, 3);
        system.addRoad(5, 8, 4);
        system.addRoad(6, 9, 2);
        system.addRoad(7, 9, 6);
        system.addRoad(8, 9, 7);

        int ambulanceAt = 0; 

        int[] hospitalLocations = {3, 6, 9}; 
        General Hospital, Metro Hospital
        int hospitalCount = 3;

        System.out.println("Initial Traffic Conditions:");
        system.findShortestPath(ambulanceAt, hospitalLocations, hospitalCount);

        System.out.println("\n\n==========================================");
        System.out.println("  REAL-TIME TRAFFIC UPDATE #1");
        System.out.println("==========================================");
        system.updateTraffic(2, 4, 8); 

        System.out.println("\nRecalculating route with updated conditions...");
        system.findShortestPath(ambulanceAt, hospitalLocations, hospitalCount);

        System.out.println("\n\n==========================================");
        System.out.println("  REAL-TIME TRAFFIC UPDATE #2");
        System.out.println("==========================================");
        system.updateTraffic(1, 3, 3); 

        System.out.println("\nRecalculating route with updated conditions...");
        system.findShortestPath(ambulanceAt, hospitalLocations, hospitalCount);

        System.out.println("\n\n*******************************************");
        System.out.println("  System ready for continuous monitoring");
        System.out.println("*******************************************\n");
    }

}

#Output
    
***** SMART TRAFFIC MANAGEMENT SYSTEM *****
    FOR EMERGENCY VEHICLES
*******************************************

Initial Traffic Conditions:

========================================
  EMERGENCY ROUTE CALCULATION
========================================
Ambulance Location: Central Square

Distances to All Hospitals:
----------------------------------------
  City Hospital: 12 minutes
  General Hospital: 10 minutes
  Metro Hospital: 12 minutes

========================================
  OPTIMAL ROUTE (SHORTEST TIME)
========================================
Destination: General Hospital
Total Time: 10 minutes

Route Navigation:
----------------------------------------
START: Central Square
   |
   V
       Park Avenue
   |
   V
       Market Junction
   |
   V
END:   General Hospital (HOSPITAL)
----------------------------------------


==========================================
  REAL-TIME TRAFFIC UPDATE #1
==========================================

>>> Traffic Updated: Road between Park Avenue and Market Junction now takes 8 minutes

Recalculating route with updated conditions...

========================================
  EMERGENCY ROUTE CALCULATION
========================================
Ambulance Location: Central Square

Distances to All Hospitals:
----------------------------------------
  City Hospital: 12 minutes
  General Hospital: 14 minutes
  Metro Hospital: 16 minutes

========================================
  OPTIMAL ROUTE (SHORTEST TIME)
========================================
Destination: City Hospital
Total Time: 12 minutes

Route Navigation:
----------------------------------------
START: Central Square
   |
   V
       Main Street
   |
   V
END:   City Hospital (HOSPITAL)
----------------------------------------


==========================================
  REAL-TIME TRAFFIC UPDATE #2
==========================================

>>> Traffic Updated: Road between Main Street and City Hospital now takes 3 minutes

Recalculating route with updated conditions...

========================================
  EMERGENCY ROUTE CALCULATION
========================================
Ambulance Location: Central Square

Distances to All Hospitals:
----------------------------------------
  City Hospital: 8 minutes
  General Hospital: 14 minutes
  Metro Hospital: 16 minutes

========================================
  OPTIMAL ROUTE (SHORTEST TIME)
========================================
Destination: City Hospital
Total Time: 8 minutes

Route Navigation:
----------------------------------------
START: Central Square
   |
   V
       Main Street
   |
   V
END:   City Hospital (HOSPITAL)
----------------------------------------


*******************************************
  System ready for continuous monitoring
*******************************************






