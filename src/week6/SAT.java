import java.math.BigInteger;
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
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class SAT {
    private static String dataFile = "data/2sat6.txt";

    public static Map<Var,Boolean> generate(Map<Var,Set<Clause>> map) {
	Map<Var,Boolean> assignment = new HashMap<>();
	Random random = new Random();
	for (Var v : map.keySet()) {
	    assignment.put(v,random.nextBoolean());
	}
	// for (Var v : assignment.keySet()) {
	//     System.out.print(v+" ");
	//     System.out.println(assignment.get(v));
	// }
	return assignment;
    }

    public static AssignmentResult satisfy(Map<Var,Boolean> assignment,
					   Set<Clause> clauses) {
	AssignmentResult result = new AssignmentResult(true);
	for (Clause c : clauses) {
	    Var var1 = new Var(c.id1);
	    Var var2 = new Var(c.id2);
	    boolean cond1 = (c.id1 > 0) ? assignment.get(var1) : !assignment.get(var1);
	    boolean cond2 = (c.id2 > 0) ? assignment.get(var2) : !assignment.get(var2);
	    if (!(cond1 || cond2)) {
		result.setSatisfible(false);
		result.add(c);
		break;
	    }
	}
	return result;
    }

    public static void flip(Clause curr,Map<Var,Boolean> assignment) {
	Random random = new Random();
	boolean coin = random.nextBoolean();
	if (coin) {
	    Var target = new Var(curr.id1);
	    boolean currValue = assignment.get(target);

	    assignment.put(target,!currValue);
	}
	else {
	    Var target = new Var(curr.id2);
	    boolean currValue = assignment.get(target);

	    assignment.put(target,!currValue);

	}
    }

    public static boolean isSingle(Var var, Map<Var,Set<Clause>> map) {
	if (null == map.get(var)) return false;
	return map.get(var).size() == 1;
    }

    public static boolean isAgree(Var var, Map<Var, Set<Clause>> map) {
	if (null == map.get(var)) return false;
	Set<Clause> clauses = map.get(var);
	List<Integer> signs = new ArrayList<>();

	for (Clause c : clauses) {
	    if (Math.abs(c.id1) == var.id) {
		signs.add(c.id1);
	    }
	    else if (Math.abs(c.id2) == var.id) {
		signs.add(c.id2);
	    }
	}

	return isSameSign(signs);
    }

    private static boolean isSameSign(List<Integer> lst) {
	if (lst == null || lst.size() == 0 || lst.size() == 1) return true;
	boolean result = true;
	String prevSign = lst.get(0) > 0 ? "positive" : "negative";

	lst.remove(0);
	for (Integer i : lst) {
	    String currSign = i > 0 ? "positive" : "negative";
	    if (!currSign.equals(prevSign)) {
		result = false;
		break;
	    }
	    prevSign = currSign;
	}

	return result;
    }


    public static void main(String[] args) {
	Path satData = Paths.get(dataFile);
	Charset charset = Charset.forName("US-ASCII");

	Map<Var,Set<Clause>> VtoC = new HashMap<>();
	Set<Clause> CtoV = new HashSet<>();
	List<Var> toRemoveVars = new ArrayList<>();
	List<Clause> toRemoveClauses = new ArrayList<>();
	int numVar = 0, numClause = 0;
	try (BufferedReader reader = Files.newBufferedReader(satData,charset)) {
		String line = reader.readLine();
		numClause = numVar = Integer.parseInt(line);

		while ((line = reader.readLine()) != null) {
		    StringTokenizer st = new StringTokenizer(line, " ");
		    int v1 = Integer.parseInt(st.nextToken());
		    int v2 = Integer.parseInt(st.nextToken());
		    Clause c = new Clause(v1,v2);
		    Var var1 = new Var(v1);
		    Var var2 = new Var(v2);
		    if (VtoC.get(var1) == null) {
			VtoC.put(var1, new HashSet<Clause>());
		    }
		    VtoC.get(var1).add(c);

		    if (VtoC.get(var2) == null) {
			VtoC.put(var2, new HashSet<Clause>());
		    }
		    VtoC.get(var2).add(c);
		    CtoV.add(c);
		}

		for (Clause c : VtoC.get(new Var(50879))) {
		    System.out.println(c);
		}
		System.out.println(VtoC.keySet().size());
		System.out.println(CtoV.size());
		// System.out.println(isSingle(new Var(16808),VtoC));
		// System.out.println(isAgree(new Var(75250),VtoC));
		// System.out.println(isAgree(new Var(16808),VtoC));
		// System.out.println(isAgree(new Var(43659),VtoC));
		// System.out.println(isAgree(new Var(-50879),VtoC));

		int varSize = VtoC.size();
		int clauseSize = CtoV.size();
		while (true) {
		    for (Map.Entry<Var,Set<Clause>> entry : VtoC.entrySet()) {
			Var key = entry.getKey();
			Set<Clause> value = entry.getValue();
			if (value == null || value.size() == 0) {
			    toRemoveVars.add(key);
			}
			else if (isSingle(key,VtoC) || isAgree(key,VtoC)) {
			    toRemoveVars.add(key);
			    for (Clause c : value) {
			    	toRemoveClauses.add(c);
			    }
			}
		    }
		    for (Var v : toRemoveVars) {
			VtoC.remove(v);
		    }
		    for (Clause c : toRemoveClauses) {
			CtoV.remove(c);
			Var tmpVar1 = new Var(c.id1);
			Var tmpVar2 = new Var(c.id2);
			if (VtoC.get(tmpVar1) != null) VtoC.get(tmpVar1).remove(c);
			if (VtoC.get(tmpVar2) != null) VtoC.get(tmpVar2).remove(c);
		    }
		    toRemoveVars = new ArrayList<>();
		    toRemoveClauses = new ArrayList<>();
		    if (VtoC.size() == varSize && CtoV.size() == clauseSize) break;
		    else {
			varSize = VtoC.size();
			clauseSize = CtoV.size();
		    }
		}
		System.out.println(VtoC.size());
		System.out.println(CtoV.size());

		int n = VtoC.size();
		// System.out.println((int)Math.ceil((Math.log(n)/Math.log(2)+1e-10)));
		for (int i = 0; i <=(int)(Math.log(n)/Math.log(2)+1e-10);i++) {
		    System.out.println("---------"+i+ " inner loop " +(2 * (int)Math.pow(n,2)));
		    Map<Var,Boolean> assignment = generate(VtoC);
		    for (int j = 0; j < 2 * (int)Math.pow(n,2); j++) {

			AssignmentResult result = satisfy(assignment,CtoV);
			if (result.satisfiable) {
			    System.out.println("satisfiable");
			    return;
			}
			else {
			    flip(result.clauses.get(0),assignment);
			}
		    }
		}
		System.out.println("unsatisfiable");
	    }
	catch (IOException e) {
	    System.err.format("IOException: %s%n",e);
	}
    }
}

class Var {
    int id;

    public Var(int id) {
	this.id = Math.abs(id);
    }


    @Override
    public String toString() {
	return id + " ";
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) return true;
	if (o == null) return false;
	if (this.getClass() != o.getClass()) return false;
	Var that = (Var) o;

	return this.id == that.id;
    }

    @Override
    public int hashCode() {
	int result = 17;
	result = 31 * result + id;
	return result;
    }
}

class Clause {
    int id1, id2;
    // boolean cond1,cond2;

    public Clause(int id1, int id2) {
	this.id1 = id1;
	this.id2 = id2;
	// this.cond1 = cond1;
	// this.cond2 = cond2;
    }

    @Override
    public String toString() {
	return id1 + " " + id2;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) return true;
	if (o == null) return false;
	if (this.getClass() != o.getClass()) return false;
	Clause that = (Clause) o;

	return this.id1 == that.id2
	    && this.id2 == that.id2;
	    // && this.cond1 == that.cond1
	    // && this.cond2 == that.cond2;
    }

    @Override
    public int hashCode() {
	int result = 17;
	result = 31 * result + this.id1;
	result = 31 * result + this.id2;
	// result = 31 * result + (this.cond1 ? 1 : 0);
	// result = 31 * result + (this.cond2 ? 1 : 0);
	return result;
    }
}

class AssignmentResult {
    boolean satisfiable;
    List<Clause> clauses;

    public AssignmentResult(boolean satisfiable) {
	this.satisfiable = satisfiable;
	clauses = new ArrayList<>();
    }

    public void add(Clause c) {
	clauses.add(c);
    }

    public void setSatisfible(boolean satisfiable) {
	this.satisfiable = satisfiable;
    }
}
