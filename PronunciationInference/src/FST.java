import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.javatuples.Pair;


public class FST {

	private Node entryNode = new Node("entry");
	private Node exitNode = new Node("exit");

	//Layer 1
	private Node node1 = new Node("1-1");
	private Node node2 = new Node("1-2");

	//Layer 2
	private Node node3 = new Node("2-1");
	private Node node4 = new Node("2-2");
	private Node node5 = new Node("2-3");
	private Node node6 = new Node("2-4");

	//Layer 3
	private Node node7 = new Node("3-1");
	private Node node8 = new Node("3-2");
	private Node node9 = new Node("3-3");
	private Node node10 = new Node("3-4");
	private Node node11 = new Node("3-5");
	private Node node12 = new Node("3-6");
	private Node node13 = new Node("3-7");
	private Node node14 = new Node("3-8");

	//Layer 4
	private Node node15 = new Node("4-1");
	private Node node16 = new Node("4-2");
	private Node node17 = new Node("4-3");
	private Node node18 = new Node("4-4");
	private Node node19 = new Node("4-5");
	private Node node20 = new Node("4-6");
	private Node node21 = new Node("4-7");
	private Node node22 = new Node("4-8");
	private Node node23 = new Node("4-9");
	private Node node24 = new Node("4-10");
	private Node node25 = new Node("4-11");
	private Node node26 = new Node("4-12");
	private Node node27 = new Node("4-13");
	private Node node28 = new Node("4-14");
	private Node node29 = new Node("4-15");
	private Node node30 = new Node("4-16");

	//Layer 5
	private Node node31 = new Node("5-1");
	private Node node32 = new Node("5-2");

	String[] nodes = new String[]{"*", "/", "*/", "**", "/*", "//", "***", "/**", "*/*", "//*", "**/", "/*/", "*//", "///", "****", "/***", "*/**", "//**", "**/*", "/*/*", "*//*", "///*", "***/", "/**/", "*/*/", "//*/", "**//", "/*//", "*///", "////", "*/*/*", "/*/*/"};;
	Word word;
	public FST(Word word) {
		this.word = word;
		//S S* S S* S S* S S* S S*
		//S* S S S* S S* S S* S S*
		//S S* S S* S S* S S* S S* S
		//S* S S S* S S* S S* S S* S


		//Connect Layer 1
		connect(node1, exitNode);
		connect(node2, exitNode);

		//Connect Layer 2
		connect(node3, node1);
		connect(node4, node1);
		connect(node5, node2);
		connect(node6, node2);

		//Connect Layer 3
		connect(node7, node3);
		connect(node8, node3);
		connect(node9, node4);
		connect(node10, node4);
		connect(node11, node5);
		connect(node12, node5);
		connect(node13, node6);
		connect(node14, node6);

		//Connect Layer 4
		connect(node15, node7);
		connect(node16, node7);
		connect(node17, node8);
		connect(node18, node8);
		connect(node19, node9);
		connect(node20, node9);
		connect(node21, node10);
		connect(node22, node10);
		connect(node23, node11);
		connect(node24, node11);
		connect(node25, node12);
		connect(node26, node12);
		connect(node27, node13);
		connect(node28, node13);
		connect(node29, node14);
		connect(node30, node14);

		//Connect Layer 5
		connect(node31, node20);
		connect(node32, node25);

		//Connect the entry node to all other nodes
		connect(entryNode, node1);
		connect(entryNode, node2);
		connect(entryNode, node3);
		connect(entryNode, node4);
		connect(entryNode, node5);
		connect(entryNode, node6);
		connect(entryNode, node7);
		connect(entryNode, node8);
		connect(entryNode, node9);
		connect(entryNode, node10);
		connect(entryNode, node11);
		connect(entryNode, node12);
		connect(entryNode, node13);
		connect(entryNode, node14);
		connect(entryNode, node15);
		connect(entryNode, node16);
		connect(entryNode, node17);
		connect(entryNode, node18);
		connect(entryNode, node19);
		connect(entryNode, node20);
		connect(entryNode, node21);
		connect(entryNode, node22);
		connect(entryNode, node23);
		connect(entryNode, node24);
		connect(entryNode, node25);
		connect(entryNode, node26);
		connect(entryNode, node27);
		connect(entryNode, node28);
		connect(entryNode, node29);
		connect(entryNode, node30);
		connect(entryNode, node31);
		connect(entryNode, node32);

		initFST();
	}
	private void connect(Node one, Node two) {
		one.addOutNode(two, 0.05f);
		two.addInNode(one);
	}
	private Node findStartingNode(String pattern) {
		switch (pattern) {
		case "*": return node1;
		case "/": return node2;

		//Feeds into node1
		case "**": return node3; 
		case "/*": return node4;
		//Feeds into node2
		case "*/": return node5;
		case "//": return node6;


		//Feeds into node3
		case "***": return node7;
		case "/**": return node8;
		//Feeds into node4
		case "*/*": return node9;
		case "//*": return node10;
		//Feeds into node5
		case "**/": return node11;
		case "/*/": return node12;
		//Feeds into node6
		case "*//": return node13;
		case "///": return node14;



		//Feeds into node7
		case "****": return node15;
		case "/***": return node16;
		//Feeds into node8
		case "*/**": return node17;
		case "//**": return node18;
		//Feeds into node9
		case "**/*": return node19;
		case "/*/*": return node20;
		//Feeds into node10
		case "*//*": return node21;
		case "///*": return node22;
		//Feeds into node11
		case "***/": return node23;
		case "/**/": return node24;
		//Feeds into node12
		case "*/*/": return node25;
		case "//*/": return node26;
		//Feeds into node13
		case "**//": return node27;
		case "/*//": return node28;
		//Feeds into node14
		case "*///": return node29;
		case "////": return node30;


		//Feeds into node20
		case "*/*/*": return node31;
		//Feeds into node25
		case "/*/*/": return node32;
		}
		//System.err.println("Couldn't match pattern: "+pattern);
		return null;
	}
	//Initialise all weights equally
	private void initFST() {

	}
	public void normaliseFST() {

		double sum = 0.0f;
		for (Node exits : entryNode.out.keySet()) {
			sum += entryNode.out.get(exits);
			//normalise(exits, sum);
		}
		for (Node exits : entryNode.out.keySet()) {
			entryNode.out.put(exits, (float)(entryNode.out.get(exits)/sum));
		}

		/*sum = 0.0d;
		for (Node enters : exitNode.in) {
			sum += enters.out.get(exitNode);
		}
		for (Node enters : exitNode.in) {
			enters.out.put(exitNode, enters.out.get(exitNode)/sum);
			//exitNode.out.put(enters, entryNode.out.get(enters)/sum);
		}*/
		for (Node exits : entryNode.out.keySet()) {
			//double sum += sumProbability(exits);
			normalise(exits);
		}


	}
	public void normaliseByNode() {
		exitNode.normaliseInProbabilities();
	}
	private void normalise(Node node) {
		if (node.out.isEmpty()) return;
		double sum = 0.0f;
		for (Node next : node.out.keySet()) {
			sum += node.out.get(next);
		}
		for (Node next : node.out.keySet()) {
			node.out.put(next, (float)(node.out.get(next)/sum));
			normalise(next);
		}
		/*	n.out.put(node, n.out.get(node)/sum);
			if (n.out.get(node) < 0.0000000000001d) {
				n.out.put(node, 0.0000000000001d);
			}

			normalise(n, sum);
		}*/
	}
	private double sumProbability(Node node) {
		if (node.out.size() == 0) return 0.0f;

		Node firstNodeOut = node.out.keySet().iterator().next();
		double sum = node.out.get(firstNodeOut)+sumProbability(firstNodeOut);

		return sum;
	}

	/**
	 * Multiply all probabilities by a variable factor
	 * @param fstFactor
	 * @param pattern
	 * @return
	 */
	public double iterativeNormaliseTransitionsBy(Double error, String pattern) {
		Node startingNode = findStartingNode(pattern);
		if (startingNode == null) return -1.0;

		if (error.isNaN()) {
			throw new RuntimeException("ERGH!");
		}
		if (entryNode.out.get(startingNode).isNaN()) {
			throw new RuntimeException("ERGH!");
		}
		//IS THE BELOW LINE WORKING?
		entryNode.out.put(startingNode, (float)(entryNode.out.get(startingNode) + (1-entryNode.out.get(startingNode))*-error));//+(1-entryNode.out.get(startingNode)*error)));//(1-entryNode.out.get(startingNode)*-error)));
		/*float sum = 0;
		for (Node key : entryNode.out.keySet() ) {
			sum += entryNode.out.get(key);
		}
		for (Node key : entryNode.out.keySet() ) {
			entryNode.out.put(key, entryNode.out.get(key)/sum);
		}*/
		return entryNode.out.get(startingNode);//*divideAllNodesInPathBy(factor, startingNode);
	}
	private double divideAllNodesInPathBy(double val, Node node) {
		//double entryProb = entryNode.out.get(node);
		if (node.out.isEmpty()) return 1.0f;
		Node next = node.out.keySet().iterator().next();

		if (node.out.get(next).isNaN()) {
			throw new RuntimeException("ERGH!");
		}
		node.out.put(next, (float)(node.out.get(next)+ (1-node.out.get(next))*-val));//= sigmoid(outProbability/val);
		return divideAllNodesInPathBy(val, next);

	}

	/**
	 * Multiply all probabilities by a STANDARD factor 
	 */
	public double normaliseTransitionsBy(double factor, String pattern) {
		Node startingNode = findStartingNode(pattern);
		entryNode.out.put(startingNode, (float)(entryNode.out.get(startingNode)*factor));
		return entryNode.out.get(startingNode)*divideAllNodesInPathByFlat(factor, startingNode);
	}
	private double divideAllNodesInPathByFlat(double val, Node node) {
		//double entryProb = entryNode.out.get(node);
		if (node.out.isEmpty()) return 1.0f;
		Node next = node.out.keySet().iterator().next();
		node.out.put(next, (float)(node.out.get(next)*val));//= sigmoid(outProbability/val);
		return divideAllNodesInPathBy(val, next);

	}

	public double getForwardPathProbability(String pattern) {
		Node startingNode = findStartingNode(pattern);
		if (startingNode == null) return -1.0;
		//for (Node firstOutNodes : startingNode.)
		Double prob = findPathProbability(startingNode)*entryNode.out.get(startingNode);
		if (prob.isNaN()) {
			return -1.0;
			//throw new RuntimeException("Probability is NaN!");
		}
		return prob;
	}
	@Override
	public String toString() {
		double maxProb = 0.0f;
		String node = "";
		for (String n : nodes) {
			double prob = getForwardPathProbability(n);
			if (prob > maxProb) {
				maxProb = prob;
				node = n;
			}
		}
		return node+" { "+maxProb+" }";
	}
	public String toVerboseString() {
		StringBuilder builder = new StringBuilder();
		DecimalFormat df = new DecimalFormat("#.####");

		df.setRoundingMode(RoundingMode.FLOOR);
		for (String n : nodes) {
			double prob = getForwardPathProbability(n);
			if (prob > 0.001)
				builder.append("("+n+","+new Double(df.format(prob))+"),");
		}
		return builder.toString();
	}
	public double findPathProbability(Node node) {

		/*if (node.outProbability > 1) {
			System.out.println("PROBLEM");
		}*/
		if (node == null) return 1;
		if (node.out.isEmpty()) return 1;

		Node next = node.out.keySet().iterator().next();



		return node.out.get(next) * findPathProbability(next);
	}
	public void addInitialPronunciations() {
			Map<String, Double> probabilities = new HashMap<String, Double>();
			for (String pronunciation : word.initialPronunciations) {
				double probability = getForwardPathProbability(pronunciation);
				if (probability < 0) continue;
				probabilities.put(pronunciation, probability);
			}
			for (String pronunciation : word.initialPronunciations) {
				if (probabilities.containsKey(pronunciation)) {
					float factor = 1f/probabilities.size();///(float) (probabilities.size()/probabilities.get(pronunciation));
					iterativeNormaliseTransitionsBy((double) factor, pronunciation);	
				}
			}
		
		//System.out.println(this.toVerboseString());
		normaliseFST();

	}
}
