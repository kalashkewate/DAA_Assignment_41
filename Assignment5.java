import java.util.*;
public class SwiftCargo {
    static class Edge {
        int from, to;
        double cost;
        Edge(int from, int to, double cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }
    }
    static class MultistageGraph {
        int stages;                   
        List<List<Integer>> stageNodes; 
        Map<Integer, List<Edge>> adj;  
        MultistageGraph(int stages) {
            this.stages = stages;
            stageNodes = new ArrayList<>();
            adj = new HashMap<>();
            for (int i = 0; i < stages; i++) stageNodes.add(new ArrayList<>());
        }
        void addNode(int stage, int node) {
            stageNodes.get(stage).add(node);
            adj.putIfAbsent(node, new ArrayList<>());
        }
        void addEdge(int from, int to, double cost) {
            adj.putIfAbsent(from, new ArrayList<>());
            adj.get(from).add(new Edge(from, to, cost));
        }
        boolean updateEdge(int from, int to, double newCost) {
            List<Edge> edges = adj.get(from);
            if (edges == null) return false;
            boolean updated = false;
            for (Edge e : edges) {
                if (e.to == to) {
                    e.cost = newCost;
                    updated = true;
                }
            }
            return updated;
        }
    }
    static class Result {
        double cost;
        List<Integer> path;
        Result(double cost, List<Integer> path) {
            this.cost = cost;
            this.path = path;
        }
    }
    static Result findOptimalPath(MultistageGraph g, int source, int destination) {
        Map<Integer, Double> dp = new HashMap<>();
        Map<Integer, Integer> parent = new HashMap<>();
        dp.put(source, 0.0);
        for (int stage = 0; stage < g.stages; stage++) {
            Map<Integer, Double> nextDp = new HashMap<>();
            for (int u : g.stageNodes.get(stage)) {
                if (!dp.containsKey(u)) continue;
                double costU = dp.get(u);
                List<Edge> edges = g.adj.get(u);
                if (edges == null) continue;
                for (Edge e : edges) {
                   
                    int nextStage = -1;
                    for (int s = stage + 1; s < g.stages; s++) {
                        if (g.stageNodes.get(s).contains(e.to)) {
                            nextStage = s;
                            break;
                        }
                    }
                    if (nextStage == -1) continue;
                    double newCost = costU + e.cost;
                    if (!nextDp.containsKey(e.to) || newCost < nextDp.get(e.to)) {
                        nextDp.put(e.to, newCost);
                        parent.put(e.to, u);
                    }
                }
            }
            dp.putAll(nextDp); 
        }
        List<Integer> path = new ArrayList<>();
        if (!dp.containsKey(destination)) return new Result(Double.POSITIVE_INFINITY, path);
        int cur = destination;
        while (cur != source) {
            path.add(cur);
            cur = parent.get(cur);
        }
        path.add(source);
        Collections.reverse(path);
        return new Result(dp.get(destination), path);
    }
    static List<Result> batchOptimalPaths(MultistageGraph g, List<int[]> requests) {
        List<Result> results = new ArrayList<>();
        for (int[] req : requests) {
            Result res = findOptimalPath(g, req[0], req[1]);
            results.add(res);
        }
        return results;
    }
    public static void main(String[] args) {
        MultistageGraph g = new MultistageGraph(3);
        g.addNode(0, 0);
        g.addNode(0, 1);

        g.addNode(1, 2);
        g.addNode(1, 3);

        g.addNode(2, 4);
        g.addNode(2, 5);

        g.addEdge(0, 2, 4.0);
        g.addEdge(0, 3, 2.0);
        g.addEdge(1, 2, 3.0); 
        g.addEdge(1, 3, 2.5);
        g.addEdge(2, 4, 5.0);
        g.addEdge(2, 5, 6.0);
        g.addEdge(3, 4, 2.0);
        g.addEdge(3, 5, 3.5);

        Result r1 = findOptimalPath(g, 0, 4);
        System.out.println("optimal cost: " + r1.cost);
        System.out.println("optimal path: " + r1.path);

        g.updateEdge(3, 4, 10.0);

        Result r2 = findOptimalPath(g, 0, 4);
        System.out.println("after traffic update the Optimal cost: " + r2.cost);
        System.out.println("after traffic update the Optimal path: " + r2.path);

        List<int[]> requests = new ArrayList<>();
        requests.add(new int[]{0, 4});
        requests.add(new int[]{1, 5});
        List<Result> batch = batchOptimalPaths(g, requests);
        for (int i = 0; i < batch.size(); i++) {
            System.out.println("Request " + (i + 1) + " - cost: " + batch.get(i).cost + ", path: " + batch.get(i).path);
        }
    }
}

#Output
    optimal cost: 4.0
optimal path: [0, 3, 4]
after traffic update the Optimal cost: 9.0
after traffic update the Optimal path: [0, 2, 4]
Request 1 - cost: 9.0, path: [0, 2, 4]
Request 2 - cost: 6.0, path: [1, 3, 5]



