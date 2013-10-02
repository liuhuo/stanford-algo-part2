import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Map;
import java.util.HashMap;


public class APSP {
    private static String dataFile = "data/g2.txt";

    public static void main(String[] args) {
	Path graphData = Paths.get(dataFile);
	Charset charset = Charset.forName("US-ASCII");

	try (BufferedReader reader = Files.newBufferedReader(graphData,charset)) {
		String line = reader.readLine();
		StringTokenizer st = new StringTokenizer(line, " ");
		int n = Integer.parseInt(st.nextToken());
		int m = Integer.parseInt(st.nextToken());

		Map<EdgeIdx,Integer> edges = new HashMap<>();

		while ((line = reader.readLine()) != null) {
		    st = new StringTokenizer(line, " ");
		    int source = Integer.parseInt(st.nextToken());
		    int dest = Integer.parseInt(st.nextToken());
		    int cost = Integer.parseInt(st.nextToken());
		    edges.put(new EdgeIdx(source,dest), cost);
		}

		int[][] dist = new int[n+1][n+1];
		for (int i = 1; i <= n; i++) {
		    for (int j = 1; j <= n; j++) {
			EdgeIdx idx = new EdgeIdx(i,j);
			if (i == j) dist[i][j] = 0;
			else if (edges.containsKey(idx)) {
			    dist[i][j] = edges.get(idx);
			}
			else {
			    dist[i][j] = Integer.MAX_VALUE / 2 - 1;
			}
		    }
		}

		for (int k = 1; k <= n; k++) {
		    for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++) {
			    if (dist[i][k] + dist[k][j] < dist[i][j])
				dist[i][j] = dist[i][k] + dist[k][j];
			}
		    }
		}


		for (int i = 1; i <= n; i++) {
		    if (dist[i][i] < 0) {
			System.out.println("negative cycle");
			break;
		    }
		}

		int best = Integer.MAX_VALUE;
		for (int i = 1; i <= n; i++) {
		    for (int j = 1; j <= n; j++) {
			if (i != j && dist[i][j] < best) {
			    best = dist[i][j];
			}
		    }
		}
		System.out.println(best);

	    }
	catch (IOException e) {
	    System.err.format("IOException: %s%n", e);
	}
    }

}

class EdgeIdx {
    int u, v;

    public EdgeIdx(int u, int v) {
	this.u = u;
	this.v = v;
    }

    @Override
    public boolean equals(Object o) {
	if (o == this) return true;
	if (o == null) return false;
	if (o.getClass() != this.getClass()) return false;

	EdgeIdx that = (EdgeIdx) o;
	return this.u == that.u && this.v == that.v;
    }

    @Override
    public int hashCode() {
	int result = 17;
	result = 31 * result + u;
	result = 31 * result + v;
	return result;
    }
}

// class Edge {
//     int u;
//     int v;
//     int cost;

//     public Edge(int u, int v, int cost) {
// 	this.u = u;
// 	this.v = v;
// 	this.cost = cost;
//     }

//     @Override
//     public boolean equals(Object o) {
// 	if (o == this) return true;
// 	if (o == null) return false;
// 	if (this.getClass() != o.getClass()) return false;

// 	Edge that = (Edge) o;
// 	return this.u == that.u && this.v == that.v && this.cost == that.cost;
//     }

//     @Override
//     public int hashCode() {
// 	int result = 17;
// 	result = 31 * result + u;
// 	result = 31 * result + v;
// 	result = 31 * result + cost;
// 	return result;
//     }

//     @Override
//     public String toString() {
// 	return u + " -> " + v + " at cost " + cost;
//     }
// }
