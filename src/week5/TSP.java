oimport java.math.BigInteger;
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
import java.util.ArrayList;

public class TSP {
    private static String dataFile = "data/a5t1.txt";

    private static int nchoosek(int n, int k) {
	BigInteger ret = BigInteger.ONE;
	for (int idx = 0; idx < k; idx++) {
	    ret = ret.multiply(BigInteger.valueOf(n - idx)).divide(BigInteger.valueOf(idx+1));
	}
	return ret.intValue();
    }

    private static int check(int v, int pos) {
	return v & (1 << pos);
    }

    private static int next(int v) {
	int t = v | (v - 1);
	int w = (t + 1) | (((~t & -~t) - 1) >> (Integer.numberOfTrailingZeros(v) + 1));
	return w;
    }

    private static int on(int n, int pos) {
	return n | ( 1 << pos);
    }

    private static int off(int n, int pos) {
	return n & ~(1 << pos);
    }

    private static int initial(int m) {
	int n = 0;
	for (int i = 0; i < m; i++) {
	    n = on(n,i);
	}
	return n;
    }

    private static void bitmanipulate() {
	// int number = 3;
	// for (int j = 0; j < 10; j++) {
	//     number = next(number);
	//     System.out.println(Integer.toString(number,2));
	// }
	// System.out.println(Integer.toString(number,2));

	// System.out.println(Integer.toString(on(number,2),2));
	// System.out.println(Integer.toString(off(number,0),2));

	// System.out.println(Integer.toString(initial(3),2));
	// System.out.println(Integer.toString(next(initial(3)),2));
	// System.out.println(Integer.toString(next(next(initial(3))),2));
	// System.out.println(Integer.toString(next(next(next(initial(3)))),2));
	// System.out.println(Integer.toString(next(next(next(next(initial(3))))),2));
	// System.out.println(nchoosek(25,13));
	// System.out.println(nchoosek(25,12));
	// System.out.println(nchoosek(3,1));
	int perm = 0;
	int n = 5, k = 2;
	System.out.println(nchoosek(n,k));
	for (int i = 0; i < nchoosek(n,k); i++) {
	    perm = (i == 0) ? initial(k) : next(perm);
	    System.out.println(Integer.toString(perm,2));
	}
    }

    private static float compute(float[][] arr, int s, int k) {
	if (k == 1) {
	    if (s == 1) return 0.0f;
	    else return Float.POSITIVE_INFINITY;
	}
	else {

	    return arr[s][k];
	}
    }

    public static void main(String[] args) {
	// TSP.bitmanipulate();

	Path graphData = Paths.get(dataFile);
	Charset charset = Charset.forName("US-ASCII");
	City[] cities = null;
	int n = 0;
	Map<Edge,Float> distance = new HashMap<>();
	try (BufferedReader reader = Files.newBufferedReader(graphData,charset)) {
		String line = reader.readLine();
		n = Integer.parseInt(line);

		cities = new City[n];
		int idx = 0;
		while ( (line = reader.readLine()) != null) {
		    StringTokenizer st = new StringTokenizer(line," ");
		    float x = Float.parseFloat(st.nextToken());
		    float y = Float.parseFloat(st.nextToken());
		    cities[idx] = new City(idx,x,y);
		    idx++;
		}
	    }
	catch (IOException e) {
	    System.err.format("IOException: %s%n",e);
	}

	for (int i = 0; i < cities.length; i++) {
	    for (int j = 0; j < cities.length; j++) {
		if (i != j) {
		    Edge e = new Edge(i,j);
		    float cost = (float)Math.hypot( (cities[i].x - cities[j].x), (cities[i].y - cities[j].y));
		    distance.put(e,cost);
		}
	    }
	}

	float[][] A = new float[(int)Math.pow(2,n)][];
	A[1] = new float[n];

	for (int m = 1; m <= n - 1; m++) {
	    int perm = 0;
	    for (int subset = 0; subset < nchoosek(n - 1, m); subset++) {
		perm = (subset == 0) ? initial(m) : next(perm);
		int idx = (perm << 1) + 1;
		if (null == A[idx]) {
		    A[idx] = new float[n];
		    for (int i = 0; i < n; i++) {
			A[idx][i] = Float.POSITIVE_INFINITY;
		    }
		}

		for (int j = 1; j < n; j++) {
		    if (check(idx,j) != 0) {
			float best = Float.POSITIVE_INFINITY;
			for (int k = 0; k < n; k++) {
			    if (check(idx,k) !=0 && k != j) {
				best = Math.min(best,A[off(idx,j)][k] + distance.get(new Edge(k,j)));
			    }
			}
			A[idx][j] = best;
		    }
		}
	    }
	}

	float minTour = Float.POSITIVE_INFINITY;
	for (int j = 1; j < n; j++) {
	    minTour = Math.min(minTour, A[initial(n)][j]+distance.get(new Edge(j,0)));
	}
	System.out.println(minTour);
	// for (int m = 2; m <= n; m++) {
	//     int perm = 0;
	//     for (int subset = 0; subset < nchoosek(n,m); subset++) {
	// 	perm = (subset == 0) ? initial(m) : next(perm);
	// 	if (perm % 2 == 0) continue;

	// 	if (null == A[perm]) {
	// 	    A[perm] = new float[n];
	// 	    for (int i = 0; i < n; i++) {
	// 		A[perm][i] = Float.POSITIVE_INFINITY;
	// 	    }
	// 	}

	// 	for (int j = 1; j < n; j++) {
	// 	    if ( (perm & (1 << j)) != 0) {
	// 		float best = Float.POSITIVE_INFINITY;
	// 		for (int k = 0; k < n; k++) {
	// 		    if (((perm & (1 << j)) != 0) && (k != j)) {
	// 			best = Math.min(best,A[off(perm,j)][k]+distance.get(new Edge(j+1,k+1)));
	// 		    }
	// 		}
	// 		A[perm][j] = best;
	// 	    }
	// 	}
	// 	// String bits = Integer.toString(perm,2);
	// 	// //System.out.println( bits);
	// 	// List<Integer> c = new ArrayList<>();
	// 	// for (int i = 0; i < bits.length(); i++) {
	// 	//     if (bits.charAt(i) == '1') {
	// 	// 	c.add( i+1);
	// 	//     }
	// 	// }
	// 	// System.out.println("--------");
	// 	// for (int j : c) {
	// 	//     System.out.print(j +  " ");
	// 	// }
	// 	// System.out.println();
	// 	// System.out.println("--------");
	// 	// for (int j : c) {
	// 	//     if (j == 1) continue;
	// 	//     float best = Float.POSITIVE_INFINITY;
	// 	//     for (int k : c) {
	// 	//     	if (j == k) continue;
	// 	//     	int prevPerm = off(perm,j-1);
	// 	//     	Edge idx = new Edge(j,k);

	// 	//     	best = Math.min(best,A[prevPerm][k] + distance.get(idx));
	// 	//     }
	// 	//     A[perm][j] = best;
	// 	// }
	//     }
	//     System.out.println("m is ======================================> " + m );
	// }
	// int fullset = initial(n);
	// float minTour = Float.POSITIVE_INFINITY;
	// for (int j = 1; j < n; j++) {
	//     System.out.println(A[fullset][j] +" " + distance.get(new Edge(j+1,1)));
	//     minTour = Math.min(minTour,A[fullset][j] + distance.get(new Edge(j+1,1)));
	// }
	// System.out.println(minTour);
    }
}

class Edge {
    int u;
    int v;

    public Edge(int u, int v) {
	this.u = u;
	this.v = v;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) return true;
	if (null == o) return false;
	if (this.getClass() != o.getClass()) return false;
	Edge that = (Edge) o;
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

class City {
    int id;
    float x;
    float y;

    public City(int id, float x, float y) {
	this.id = id;
	this.x = x;
	this.y = y;
    }

    @Override
    public String toString() {
	return "city " + id + " at (" + x + ", " + y + ")";
    }
}
