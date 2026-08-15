import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

// --- Data Models ---
class RoadNode {  
    // It contains the target city and the distance
    String target;
    double distance;
    RoadNode(String target, double distance) { 
        this.target = target; 
        this.distance = distance; 
    }
}

class PlaceData {  
    HashMap<String, String> pd = new HashMap<>();
    void loadAttraction(String path1) {
        try {
            BufferedReader br1 = new BufferedReader(new FileReader(path1));
            br1.readLine();  
            String s1;
            // Divide it into points of interest and cities
            while((s1 = br1.readLine()) != null) {
                int p1 = s1.indexOf(',');
                if(p1 < 0) {
                    continue;
                }
                String key = s1.substring(0, p1).trim();
                String value = s1.substring(p1 + 1).trim();
                pd.put(key, value);
            }
        } catch(IOException e) {
            throw new RuntimeException("Failed to load attractions: " + e.getMessage());
        }
    }
    List<String> toCities(List<String> attractions) {
        ArrayList<String> out = new ArrayList<>();
        // Convert interests to responding cities and throw exception when input error
        for(String attraction : attractions) {
            String city = pd.get(attraction);
            if(city == null) {
                throw new RuntimeException("Invalid attraction: " + attraction);
            }
            out.add(city);
        }
        return out;
    }
}

class CityMap { 
    HashMap<String, List<RoadNode>> cp = new HashMap<>();
    void loadRoad(String path2) {
        try {
            BufferedReader br2 = new BufferedReader(new FileReader(path2));
            br2.readLine();  
            String s2;
            while((s2 = br2.readLine()) != null) {
                String[] p2 = s2.split(",");
                if(p2.length < 3) {
                    continue;
                }
                // Add bidirectional edges in an undirected graph
                String start = p2[0].trim();
                String end = p2[1].trim();
                double miles = Double.parseDouble(p2[2].trim());
                if(!cp.containsKey(start)) {
                    cp.put(start, new ArrayList<>());
                }
                cp.get(start).add(new RoadNode(end, miles));
                if(!cp.containsKey(end)) {
                    cp.put(end, new ArrayList<>());
                }
                cp.get(end).add(new RoadNode(start, miles));
            }
        } catch(IOException e) {
            throw new RuntimeException("Failed to load roads: " + e.getMessage());
        }
    }
    void checkCity(String city) {
        if(!cp.containsKey(city))
            throw new RuntimeException("Invalid city: " + city);
    }
}

// --- Core Algorithm ---
class RoutePlanner {  
    PlaceData place;
    CityMap map;
    RoutePlanner(PlaceData place, CityMap map) {
        this.place = place;
        this.map = map;
    }
    static class RouteResult {
        // Including start, destination, list of interest points, complete path, total distance
        final String start;
        final String end;
        final List<String> attractions;
        final List<String> path;
        final double distance;
        RouteResult(String start, String end, List<String> attractions, List<String> path, double distance) {
            this.start = start;
            this.end = end;
            this.attractions = Collections.unmodifiableList(attractions);
            this.path = Collections.unmodifiableList(path);
            this.distance = distance;
        }
        public String getStart() {
            return start;
        }
        public String getEnd() {
            return end;
        }
        public List<String> getAttractions() {
            return attractions;
        }
        public List<String> getPath() {
            return path;
        }
        public double getDistance() {
            return distance;
        }
    }
    RouteResult computeRoute(String start, String end, List<String> attractions) {
        // Validate input city (start and end)
        map.checkCity(start);
        map.checkCity(end);
        List<String> cities = attractions.isEmpty() ?
                Collections.emptyList() :
                place.toCities(attractions);
        for(String city : cities) {
            map.checkCity(city);
        }
        List<String> fullPath = makePath(start, end, cities);
        return new RouteResult(start, end, attractions, fullPath, calcDistance(fullPath));
    }
    private double calcDistance(List<String> cd) {
        double sum = 0;
        for(int i = 1; i < cd.size(); i ++) {
            String a = cd.get(i - 1);
            String b = cd.get(i);
            for(RoadNode rn : map.cp.get(a)) {
                if(rn.target.equals(b)) {
                    sum += rn.distance;
                    break;
                }
            }
        }
        return sum;
    }
    private List<String> makePath(String start, String end, List<String> cities) {
        HashMap<String, List<String>> memo = new HashMap<>();
        ArrayList<String> all = new ArrayList<>();
        all.add(start);
        all.addAll(cities);
        all.add(end);
        // Pre-save all possible paths
        for(int i = 0; i < all.size(); i ++) {
            for(int j = 0; j < all.size(); j ++) {
                if(i == j) {
                    continue;
                }
                String a = all.get(i);
                String b = all.get(j);
                List<String> p = findShortestPath(a, b);
                memo.put(a + "_" + b, p);
            }
        }
        // Initialize dynamic programming
        int n = cities.size();
        int[][] dp = new int[1 << n][n];
        int[][] back = new int[1 << n][n];
        for(int[] arr : dp) {
            Arrays.fill(arr, 9999999);
        }
        for(int i = 0; i < n; i ++) {
            dp[1 << i][i] = (int)calcDistance(memo.get(start + "_" + cities.get(i)));
        }
        // Fill dynamic programming table
        for(int mask = 1; mask < (1 << n); mask ++) {
            for(int last = 0; last < n; last ++) {
                if((mask & (1 << last)) == 0) {
                    continue;
                }
                for(int next = 0; next < n; next ++) {
                    if((mask & (1 << next)) != 0) {
                        continue;
                    }
                    int newMask = mask | (1<<next);
                    int newCost = dp[mask][last] + (int)calcDistance(memo.get(cities.get(last) + "_" + cities.get(next)));
                    if(newCost < dp[newMask][next]) {
                        dp[newMask][next] = newCost;
                        back[newMask][next] = last;
                    }
                }
            }
        }
        // Reconstruct optimal path
        int finalMask = (1 << n) - 1;
        int bestDist = 9999999;
        int lastCity = -1;
        for(int i = 0; i < n; i ++) {
            int total = dp[finalMask][i] + (int)calcDistance(memo.get(cities.get(i) + "_" + end));
            if(total < bestDist) {
                bestDist = total;
                lastCity = i;
            }
        }
        // Rebuild final path
        List<String> optionalPath = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        int currentMask = finalMask;
        int currentCity = lastCity;
        while(currentMask > 0) {
            stack.push(cities.get(currentCity));
            int prevCity = back[currentMask][currentCity];
            currentMask ^= (1 << currentCity);
            currentCity = prevCity;
        }
        // Assemble full route
        String currentPath = start;
        optionalPath.add(currentPath);
        while(!stack.empty()) {
            String next = stack.pop();
            List<String> seg = memo.get(currentPath + "_" + next);
            optionalPath.addAll(seg.subList(1, seg.size()));
            currentPath = next;
        }
        List<String> lastSeg = memo.get(currentPath + "_" + end);
        optionalPath.addAll(lastSeg.subList(1, lastSeg.size()));
        return clean(optionalPath);
    }
    private List<String> findShortestPath(String start, String end) {
        // A*  pre-calculate algorithm implementation
        PriorityQueue<Item> heap = new PriorityQueue<>();
        HashMap<String, Double> dist = new HashMap<>();
        HashMap<String, String> prev = new HashMap<>();
        dist.put(start, 0.0);
        heap.add(new Item(start, 0));
        while(!heap.isEmpty()) {
            Item item = heap.poll();
            if(item.city.equals(end)) {
                break;
            }
            for(RoadNode rn : map.cp.get(item.city)) {
                double newDist = dist.get(item.city) + rn.distance;
                if(newDist < dist.getOrDefault(rn.target, 1e9)) {
                    dist.put(rn.target, newDist);
                    prev.put(rn.target, item.city);
                    heap.add(new Item(rn.target, newDist));
                }
            }
        }
        LinkedList<String> path = new LinkedList<>();
        // Reconstruct path
        String cu = end;
        while(cu != null) {
            path.addFirst(cu);
            cu = prev.get(cu);
        }
        return path;
    }
    private List<String> clean(List<String> cleans) {
        // Remove consecutive duplicates, optimize the path
        ArrayList<String> out = new ArrayList<>();
        String last = null;
        for(String clean : cleans) {
            if(!clean.equals(last)) {
                out.add(clean);
            }
            last = clean;
        }
        return out;
    }
    static class Item implements Comparable<Item> {
        // Implementation
        String city;
        double cost;
        Item(String city, double cost) { 
            this.city = city; 
            this.cost = cost; 
        }
        @Override
        public int compareTo(Item object) { 
            return Double.compare(cost, object.cost); 
        }
    }
}

// --- Main Task ---
public class Route {
    public static void main(String[] args) {
        // Initialize data and load components
        PlaceData pd = new PlaceData();
        pd.loadAttraction("attractions.csv");
        CityMap cp = new CityMap();
        cp.loadRoad("roads.csv");
        RoutePlanner planner = new RoutePlanner(pd, cp);
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Enter starting city (e.g., New York NY): ");
            String start = sc.nextLine().trim();
            System.out.print("Enter destination city (e.g., Chicago IL): ");
            String end = sc.nextLine().trim();
            System.out.print("Enter attractions (comma-separated, e.g., Hollywood Sign): ");
            String input = sc.nextLine().trim();
            List<String> attractions = new ArrayList<>();
            if (!input.isEmpty()) {
                attractions = Arrays.stream(input.split("\\s*,\\s*"))
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());
            }
            RoutePlanner.RouteResult result = planner.computeRoute(start, end, attractions);
            printResult(result);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    private static void printResult(RoutePlanner.RouteResult result) {
        System.out.println("\nResult:");
        System.out.println("Start: " + result.getStart());
        System.out.println("Destination: " + result.getEnd());
        System.out.println("Attractions: " + result.getAttractions());
        System.out.println("Optimal Route: " + String.join(" -> ", result.getPath()));
        System.out.printf("Total Distance: %.1f miles%n", result.getDistance());
    }
}