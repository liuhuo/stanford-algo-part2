import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;


public class Knapsack1 {
    private static String dataFile = "data/a3t1.txt";

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
		while ((line = reader.readLine()) != null) {
		    st = new StringTokenizer(line," ");
		    int value = Integer.parseInt(st.nextToken());
		    int weight = Integer.parseInt(st.nextToken());
		    items[idx++] = new Item(value,weight);
		}

		System.out.println(items[idx-1]);

		int[][] dyn = new int[num+1][capacity+1];
		for (int i = 1; i <= num; i++) {
		    for (int x = 0; x <= capacity; x++) {
			if (items[i].weight > x) {
			    dyn[i][x] = dyn[i-1][x];
			}
			else {
			    Item tmp = items[i];
			    dyn[i][x] = Math.max(dyn[i-1][x],dyn[i-1][x-tmp.weight]+tmp.value);
			}
		    }
		}
		System.out.println(dyn[num][capacity]);

	    }
	catch (IOException e) {
	    System.err.format("IOException: %s%n",e);
	}
    }
}

// class Item {
//     int value;
//     int weight;

//     public Item(int value, int weight) {
// 	this.value = value;
// 	this.weight = weight;
//     }

//     @Override
//     public String toString() {
// 	return value + " " + weight;
//     }
// }
