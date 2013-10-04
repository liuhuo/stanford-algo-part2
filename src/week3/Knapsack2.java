import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Map;
import java.util.HashMap;

public class Knapsack2 {
    private static String dataFile = "data/knapsack_big.txt";

    private static int explore(Item[] items, int i, int x, Map<CacheIdx,Integer> cache) {
	Item item = items[i];
	CacheIdx curr = new CacheIdx(i,x);
	if (i == 0) {
	    return 0;
	}
	else if (cache.containsKey(curr)) {
	    return cache.get(curr);
	}
	else if (item.weight > x) {
	    int result = explore(items, i - 1, x, cache);
	    cache.put(curr,result);
	    return result;
	}
	else {
	    int candidate1 = explore(items, i - 1, x, cache);
	    int candidate2 = explore(items, i - 1, x - item.weight, cache);
	    int result = Math.max(candidate1, candidate2 + item.value);
	    cache.put(curr, result);
	    return result;
	}
    }

    private static void computeEntry(Item[] items, int i, int x, Map<CacheIdx,Integer> cache) {
	Item item = items[i];
	CacheIdx curr = new CacheIdx(i,x);
	if (i == 0) {
	    cache.put(curr,0);
	}
	else if (item.weight > x) {
	    CacheIdx idx = new CacheIdx(i - 1, x);
	    if (!cache.containsKey(idx)) {
		computeEntry(items,i - 1, x, cache);
	    }
	    cache.put(curr, cache.get(idx));
	}
	else {
	    CacheIdx idx1 = new CacheIdx(i - 1, x);
	    CacheIdx idx2 = new CacheIdx(i - 1, x - item.weight);
	    if (!cache.containsKey(idx1)) {
		computeEntry(items, i - 1, x, cache);
	    }
	    if (!cache.containsKey(idx2)) {
		computeEntry(items, i - 1, x - item.weight, cache);
	    }

	    int candidate1 = cache.get(idx1);
	    int candidate2 = cache.get(idx2);
	    cache.put(curr, Math.max(candidate1,candidate2+item.value));
	}
    }

    public static void main(String[] args) {
	Path knapsackData = Paths.get(dataFile);
	Charset charset = Charset.forName("US-ASCII");

	try (BufferedReader reader = Files.newBufferedReader(knapsackData,charset)) {
	    String line = reader.readLine();
	    StringTokenizer st = new StringTokenizer(line," ");
	    int capacity = Integer.parseInt(st.nextToken());
	    int num = Integer.parseInt(st.nextToken());

	    Item[] items = new Item[num+1];
	    int idx = 1;
	    Map<CacheIdx,Integer> cache = new HashMap<>();
	    while ((line = reader.readLine()) != null) {
		st = new StringTokenizer(line," ");
		int value = Integer.parseInt(st.nextToken());
		int weight = Integer.parseInt(st.nextToken());
		items[idx++] = new Item(value,weight);
	    }


	    // computeEntry(items,num,capacity,cache);
	    // System.out.println(cache.get(new CacheIdx(num,capacity)));
	    System.out.println(explore(items,num,capacity,cache));
	}
	catch (IOException e) {
	    System.err.format("IOException: %s%n",e);
	}
    }
}

class Item {
    int value;
    int weight;

    public Item(int value, int weight) {
	this.value = value;
	this.weight = weight;
    }

    @Override
    public String toString() {
	return value + " " + weight;
    }

    @Override
    public int hashCode() {
	int result = 17;
	result = 37 * result + value;
	result = 37 * result + weight;
	return result;
    }
}

class CacheIdx {
    int i;
    int x;

    public CacheIdx(int i, int x) {
	this.i = i;
	this.x = x;
    }

    @Override
    public String toString() {
	return i + " " + x;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) return true;
	if (o == null) return false;
	if (this.getClass() != o.getClass()) return false;
	CacheIdx that = (CacheIdx) o;
	return this.i == that.i && this.x == that.x;
    }
    @Override
    public int hashCode() {
	int result = 17;
	result = 37 * result + i;
	result = 37 * result + x;
	return result;
    }
}
