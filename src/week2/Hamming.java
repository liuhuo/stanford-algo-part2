import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;


public class Hamming {
    private static String graphFile = "data/clustering2.txt";
    public static void main(String[] args) {
	Path graphData = Paths.get(graphFile);
	Charset charset = Charset.forName("US-ASCII");
	try (BufferedReader reader = Files.newBufferedReader(graphData, charset)) {
		String line = reader.readLine();
		StringTokenizer st = new StringTokenizer(line," ");
		int vertices = Integer.parseInt(st.nextToken());
		int bits = Integer.parseInt(st.nextToken());
		Map<BitNode,Integer> s = new HashMap<>();
		int idx = 1;
		while ((line = reader.readLine()) != null) {
		    BitNode tmp = new BitNode(line,bits);
		    if (s.containsKey(tmp)) continue;
		    s.put(tmp,idx++);
		}
		System.out.println(s.size());
		WeightedQuickUnion uf = new WeightedQuickUnion(s.size() + 1);
		for (Map.Entry<BitNode,Integer> e : s.entrySet()) {
		    BitNode curr = e.getKey();
		    Integer idx1 = e.getValue();
		    for (BitNode b1 : curr.neighbor1()) {
			if (s.containsKey(b1)) {
			    uf.union(idx1, s.get(b1));
			}
		    }
		    for (BitNode b2 : curr.neighbor2()) {
			if (s.containsKey(b2)) {
			    uf.union(idx1, s.get(b2));
			}
		    }
		}
		System.out.println(uf.count());
	    }
	catch (IOException e) {
	    System.err.format("IOException: %s%n",e);
	}
    }
}
