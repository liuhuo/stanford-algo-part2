import java.util.StringTokenizer;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class BitNode {
    private byte[] internal;
    private int bits;

    public BitNode(String strRepr, int bits) {
	internal = new byte[bits];
	this.bits = bits;
	StringTokenizer st = new StringTokenizer(strRepr, " ");
	int idx = 0;
	while (st.hasMoreTokens()) {
	    internal[idx++] = Byte.parseByte(st.nextToken());
	}
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) return true;
        if (! (o instanceof BitNode)) return false;

	BitNode that = (BitNode) o;
	boolean result = (this.bits == that.bits);
	for (int i = 0; i < bits; i++) {
	    result = result && (this.internal[i] == that.internal[i]);
	}
	return result;
    }

    @Override
    public int hashCode() {
	return Arrays.hashCode(internal) * 37 + bits;
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	for (byte b : internal) {
	    sb.append(b);
	    sb.append(" ");
	}
	sb.deleteCharAt(sb.length() - 1);
	return sb.toString();
    }

    public List<BitNode> neighbor1() {
	List<BitNode> result = new ArrayList<BitNode>();
	for (int i = 0; i < bits; i++) {
	    result.add(this.flip(i));
	}
	return result;
    }

    public List<BitNode> neighbor2() {
	List<BitNode> result = new ArrayList<BitNode>();
	for (int i = 0; i < bits; i++) {
	    for (int j = i + 1; j < bits; j++) {
		result.add(this.flip(i).flip(j));
	    }
	}
	return result;
    }

    private BitNode flip(int pos) {
	BitNode result = new BitNode(this.toString(), this.bits);
	result.internal[pos] = (byte)(1 - result.internal[pos]);
	return result;
    }

    public static void main(String[] args) {
	System.out.println(new BitNode("0 0 1 0",4));
	System.out.println(new BitNode("0 0 1 1 1",5));
	System.out.println(new BitNode("0 0 1 0 0 0 1",7));
	BitNode b1 = new BitNode("0 0 1 0",4);
	BitNode b2 = new BitNode("0 0 1 0",4);
	BitNode b3 = new BitNode("0 0 0 1",4);
	System.out.println(b1.flip(2));
	System.out.println(b1.equals(b2));
	System.out.println(b1.equals(b3));
	System.out.println(b1.hashCode());
	System.out.println(b2.hashCode());
	System.out.println(b3.hashCode());
	Set<BitNode> s = new HashSet<>();
	s.add(b1);
	System.out.println(s.contains(b2));
	System.out.println(s.contains(b1));
	System.out.println(s.contains(b3));
	for (BitNode tmp : b1.neighbor1()) {
	    System.out.println(tmp);
	}
	BitNode b4= new BitNode("1 0 0 0 1 1 0 0 0 0 0 0 0 1 1 0 0 1 1 0 1 0 1 1",24);
	System.out.println(b1.neighbor2().size());
	System.out.println(b4.neighbor2().size());
	System.out.println(b4.neighbor1().size());
    }
}
