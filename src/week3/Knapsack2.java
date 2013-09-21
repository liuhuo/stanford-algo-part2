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
    private static String dataFile = "data/knapsack1.txt";

    private static long computeEntry(Item[] items, int i, int x, Map<CacheIdx,Long> cache) {

	long result;
	if (i == 0) {
	    result = 0;
	}
	else {
	    Item tmp = items[i];
	    if (tmp.weight > x) {
		CacheIdx idx = new CacheIdx(i - 1, x);
		if (cache.containsKey(idx)) {
		    result = cache.get(idx);
		}
		else {
		    result = computeEntry(items, i-1,x,cache);
		    cache.put(new CacheIdx(i,x), result);
		    //		    cache.put(idx, result);
		}
	    }
	    else {
		CacheIdx idx1 = new CacheIdx(i-1, x);
		CacheIdx idx2 = new CacheIdx(i-1, x - tmp.weight);
		long val1,val2;
		if (cache.containsKey(idx1)) {
		    val1 = cache.get(idx1);
		}
		else {
		    val1 = computeEntry(items, i -1, x, cache);
		    //cache.put(idx1,val1);
		}

		if (cache.containsKey(idx2)) {
		    val2 = cache.get(idx2);
		}
		else {
		    val2 = computeEntry(items, i -1, x - tmp.weight, cache);
		    //cache.put(idx2,val2);
		}
		result = Math.max(val1,val2 + tmp.value);
		cache.put(new CacheIdx(i,x), result);
	    }
	}
	//	System.out.println(i + " " + x + " "+result);
	return result;
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
	    Map<CacheIdx,Long> cache = new HashMap<>();
	    while ((line = reader.readLine()) != null) {
		st = new StringTokenizer(line," ");
		int value = Integer.parseInt(st.nextToken());
		int weight = Integer.parseInt(st.nextToken());
		items[idx++] = new Item(value,weight);
	    }
	    // System.out.println(items[idx-1]);
	    // System.out.println(items[idx-1].hashCode());

	    long result = computeEntry(items,num,capacity,cache);
	    System.out.println(result + " " + cache.size());
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
    public int hashCode() {
	int result = 17;
	result = 37 * result + i;
	result = 37 * result + x;
	return result;
    }
}
