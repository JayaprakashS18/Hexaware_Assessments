
package logistics;

import java.util.*;

public class HubNetwork {

    // Adjacency list
    private Map<String, List<String>> adjacencyList =
            new LinkedHashMap<>();

    // Add hub
    public boolean addHub(String hubCode) {

        if (hubCode == null || hubCode.trim().isEmpty()) {
            return false;
        }

        hubCode = hubCode.trim();

        if (adjacencyList.containsKey(hubCode)) {
            return false;
        }

        adjacencyList.put(hubCode, new ArrayList<>());

        return true;
    }

    // Add directed route
    public boolean addRoute(String source, String destination) {

        if (source == null || destination == null) {
            return false;
        }

        source = source.trim();
        destination = destination.trim();

        if (!adjacencyList.containsKey(source)
                || !adjacencyList.containsKey(destination)) {

            return false;
        }

        List<String> routes = adjacencyList.get(source);

        if (routes.contains(destination)) {
            return false;
        }

        routes.add(destination);

        return true;
    }

    // Display graph
    public void displayNetwork() {

        if (adjacencyList.isEmpty()) {

            System.out.println("No hubs available.");
            return;
        }

        System.out.println("\nLogistics Network:");

        for (String hub : adjacencyList.keySet()) {

            System.out.println(hub + " -> "
                    + adjacencyList.get(hub));
        }
    }

    // BFS shortest path
    public List<String> shortestRouteBFS(
            String start, String destination) {

        if (start == null || destination == null
                || !adjacencyList.containsKey(start)
                || !adjacencyList.containsKey(destination)) {

            return Collections.emptyList();
        }

        Queue<String> queue = new LinkedList<>();

        Set<String> visited = new HashSet<>();

        Map<String, String> parent = new HashMap<>();

        queue.add(start);

        visited.add(start);

        while (!queue.isEmpty()) {

            String current = queue.poll();

            if (current.equals(destination)) {
                break;
            }

            for (String neighbor :
                    adjacencyList.get(current)) {

                if (!visited.contains(neighbor)) {

                    visited.add(neighbor);

                    parent.put(neighbor, current);

                    queue.add(neighbor);
                }
            }
        }

        // Destination not reachable
        if (!visited.contains(destination)) {

            return Collections.emptyList();
        }

        // Reconstruct path
        List<String> path = new ArrayList<>();

        String current = destination;

        while (current != null) {

            path.add(current);

            current = parent.get(current);
        }

        Collections.reverse(path);

        return path;
    }

    // DFS
    public List<String> reachableHubsDFS(String start) {

        List<String> result = new ArrayList<>();

        if (start == null || !adjacencyList.containsKey(start)) {
            return result;
        }

        Set<String> visited = new HashSet<>();

        dfs(start, visited, result);

        return result;
    }

    // Recursive DFS
    private void dfs(String current,
                     Set<String> visited,
                     List<String> result) {

        visited.add(current);

        result.add(current);

        for (String neighbor : adjacencyList.get(current)) {

            if (!visited.contains(neighbor)) {

                dfs(neighbor, visited, result);
            }
        }
    }

    // Check hub existence
    public boolean containsHub(String hubCode) {

        return hubCode != null
                && adjacencyList.containsKey(hubCode.trim());
    }

    // Number of hubs
    public int getHubCount() {
        return adjacencyList.size();
    }
}
