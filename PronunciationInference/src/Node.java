import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.javatuples.Pair;


public class Node {

	List<Node> in = new ArrayList<Node>();
	HashMap<Node, Float> out = new HashMap<Node, Float>();
	//float outProbability = 1;
	boolean stressed;
	String name;
	public Node(String name) {
		this.name = name;
	}
	/*public Node(float prob, String name) {
		outProbability = prob;
		this.name = name;
	}*/
	//USED IN MAXIMISATION STEP
	public void normaliseInProbabilities() {
		float prob = 0;
		for (Node node : in) {
			prob += node.out.get(this);//outProbability;
		}
		if (prob == 0.0 && !in.isEmpty()) {
			System.out.println();
		}
		for (Node node : in) {
			node.out.put(this,node.out.get(this)/prob);
			if (node.out.get(this) < 0.000001f) node.out.put(this, 0.000001f);
			node.normaliseInProbabilities();
		}
	}


	public void addInNode(Node node){
		in.add(node);
	}
	public void addOutNode(Node node, float val){
		out.put(node, val);
	}
	@Override
	public String toString(){
		return name;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
