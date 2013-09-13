import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Clustering4 {
    private static String graphFile = "data/clustering1.txt";
    public static void main(String[] args) {
	Path graphData = Paths.get(graphFile);
	Charset charset = Charset.forName("US-ASCII");
	try (BufferedReader reader = Files.newBufferedReader(graphData, charset)) {
		String line = reader.readLine();
		int vertices = Integer.parseInt(line);
		StringTokenizer st;
		List<Edge> allEdges = new ArrayList<>();
		while ((line = reader.readLine()) != null) {
		    st = new StringTokenizer(line, " ");
		    int from = Integer.parseInt(st.nextToken());
		    int to = Integer.parseInt(st.nextToken());
		    int cost = Integer.parseInt(st.nextToken());
		    allEdges.add(new Edge(from,to,cost));
		}
		WeightedQuickUnion uf = new WeightedQuickUnion(vertices+1);
		int size = allEdges.size();
		// System.out.println(vertices);
		// System.out.println(allEdges.get(size-1));
		Collections.sort(allEdges, new EdgeComparator());
		// System.out.println(allEdges.get(0));
		// System.out.println(allEdges.get(size-1));
		// System.out.println(true);
		System.out.println("pre-processing done");

		int idx = 0;
		Edge e = allEdges.get(idx);
		while (uf.count() > 4) {
		    e = allEdges.get(idx++);
		    if (!uf.connected(e.from, e.to)) {
			uf.union(e.from, e.to);
		    }
		}
		System.out.println(e);
	    }
	catch (IOException e) {
	    System.err.format("IOException: %s%n",e);
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

class EdgeComparator implements Comparator<Edge> {
    public int compare(Edge e1, Edge e2) {
	return e1.cost - e2.cost;
    }
}
