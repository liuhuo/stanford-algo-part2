import java.math.BigInteger;

public class TSP {
    private static String dataFile = "data/tsp.txt";

    private static int nchoosek(int n, int k) {
	BigInteger ret = BigInteger.ONE;
	for (int idx = 0; idx < k; idx++) {
	    ret = ret.multiply(BigInteger.valueOf(n - idx)).divide(BigInteger.valueOf(idx+1));
	}
	return ret.intValue();
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
	int number = 3;

	for (int j = 0; j < 10; j++) {
	    number = next(number);
	    System.out.println(Integer.toString(number,2));
	}
	System.out.println(Integer.toString(number,2));

	System.out.println(Integer.toString(on(number,2),2));
	System.out.println(Integer.toString(off(number,0),2));

	System.out.println(Integer.toString(initial(12),2));
	System.out.println(Integer.toString(next(initial(12)),2));
	System.out.println(nchoosek(25,13));
	System.out.println(nchoosek(25,12));
    }

    public static void main(String[] args) {
	TSP.bitmanipulate();
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
    Double x;
    Double y;

    public City(int id, double x, double y) {
	this.id = id;
	this.x = x;
	this.y = y;
    }
}
