import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;



public class Prim {
    private static String graphFile = "data/edges.txt";

    public static void main(String[] args) {
	Path graphData = Paths.get(graphFile);
	Charset charset = Charset.forName("US-ASCII");
	try (BufferedReader reader = Files.newBufferedReader(graphData,charset)) {
		String line = reader.readLine();
		StringTokenizer st = new StringTokenizer(line," ");
		int vertices = Integer.parseInt(st.nextToken());
		int edges = Integer.parseInt(st.nextToken());

		Map<Integer,List<Edge>> graph = new HashMap<>();
		List<Edge> allEdges = new ArrayList<>();
		int from = 0;
		for (int i = 0; i < edges; i++) {
		    line = reader.readLine();
		    st = new StringTokenizer(line," ");
		    from = Integer.parseInt(st.nextToken());
		    int to = Integer.parseInt(st.nextToken());
		    int cost = Integer.parseInt(st.nextToken());
		    allEdges.add(new Edge(from, to, cost));

 		}

		Set<Integer> frontier = new HashSet<>();
		frontier.add(from);
		List<Edge> mst = new ArrayList<>();

		while (frontier.size() < vertices) {
		    int minCost = 9999999;
		    Edge theEdge = null;
		    for (Edge e : allEdges) {
			if (((frontier.contains(e.from) && !frontier.contains(e.to)) ||
			     (frontier.contains(e.to) && !frontier.contains(e.from)))
			    && e.cost < minCost) {
				theEdge = e;
				minCost = e.cost;
			}
		    }
		    if (!frontier.contains(theEdge.from)) frontier.add(theEdge.from);
		    if (!frontier.contains(theEdge.to)) frontier.add(theEdge.to);
		    mst.add(theEdge);
		}

		int mstCost = 0;
		for (Edge e : mst) {
		    mstCost += e.cost;
		}
		System.out.println(mstCost);

	    } catch (IOException x) {
	    System.err.format("IOException: %s%n", x);
	}
    }
}

class Edge {
    int from;
    int to;
    int cost;

    public Edge(int from, int to, int cost) {
	this.from = from;
	this.to = to;
	this.cost = cost;
    }

    public String toString() {
	return from + " -> " + to + " at cost " + cost;
    }
}
