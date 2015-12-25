import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;


public class Word implements Comparable{

	public String word;
	public Double[] stress;
	public int count = 1;
	public Double[] syllables = new Double[10];
	public FST fst;
	
	public Set<String> initialPronunciations = new HashSet<String>();
	
	public Word(String word) {
		fst = new FST(this);
		this.word = word;
		stress = new Double[46];
		for (int i = 0; i < 46; i++) {
			stress[i]=1/46d;
			
		}
		for (int i = 0; i < 10; i++) {
			syllables[i] = 0.1d;
		}
	}
	/*public void updateDistributions(List<ICombinatoricsVector<Integer>> fs) {

		for (int i = 0; i < syllables.length; i++) {
			if (i >= fs.length) {
				syllables[i] = 0d;
			}else{
				syllables[i] *= fs[i];
			}
		}
		count++;
	}*/
	public String getMaxEnt() {
		Double prob = 0d;
		String guess = null;
		for (int i = 0; i < fst.nodes.length; i++) {
			Double value = fst.getForwardPathProbability(fst.nodes[i]);
			if (value > prob) {
				prob = value;
				guess = fst.nodes[i];
			}
		}
		/*for (int i = 0; i < syllables.length; i++) {
			if (syllables[i] > prob) {
				prob = syllables[i];
				index = i;
			}
		}*/
		return guess+" - "+prob;
	}
	/*public int getMaxEntSyllables() {
		Double prob = 0d;
		int index = 0;
		for (int i = 0; i < syllables.length; i++) {
			if (syllables[i] > prob) {
				prob = syllables[i];
				index = i;
			}
		}
		return index+1;
	}*/
	@Override
	public String toString() {

		return word+"{"+getMaxEnt()+"}";
	}
	public String toStringVerbose() {
		/*StringBuilder builder = new StringBuilder();
		for (int i = 0; i < stress.length; i++) {
			String stress = fst.
		}*/
		return word+"{"+fst.toVerboseString()+"}";
	}

	@Override
	public int compareTo(Object o) {
		if (count>((Word)o).count) return 1;
		if (count==((Word)o).count) return 0;
		return -1;
	}

	public static int stressLookup(String stress) {
		switch (stress) {
		case "*": return 0;
		case "/": return 1;

		case "**": return 2;
		case "*/": return 3;
		case "/*": return 4;
		case "//": return 5;

		case "***": return 6;
		case "**/": return 7;
		case "*/*": return 8;
		case "*//": return 9;
		case "/**": return 10;
		case "/*/": return 11;
		case "//*": return 12;
		case "///": return 13;

		case "****": return 14;
		case "***/": return 15;
		case "**/*": return 16;
		case "**//": return 17;
		case "*/**": return 18;
		case "*/*/": return 19;
		case "*//*": return 20;
		case "*///": return 21;
		case "/***": return 22;
		case "/**/": return 23;
		case "/*/*": return 24;
		case "/*//": return 25;
		case "//**": return 26;
		case "//*/": return 27;
		case "///*": return 28;
		case "////": return 29;
		case "**/*/": return 36;
		case "*/*/*": return 30;
		case "/**/*": return 31;
		case "**/*/*": return 37;
		case "/*/*/": return 32;
		case "/*/*/*/": return 41;
		case "*/*/*/*": return 42;
		case "/*/*/*": return 33;
		case "/**/*/": return 34;
		case "*/*/*/*/": return 40;
		case "/*/*/*/*/": return 38;
		case "**/*/*/*/": return 39;
		case "*/*/*/": return 35;
		case "/**/*/*": return 41;
		case "/**/*/*/": return 42;
		case "*/*/*/*/*": return 43;
		case "**/*/*/" : return 44;
		}
		System.out.println(stress);
		return -1;
	}

	public static String indexLookup(int index) {
		switch (index) {
		 case 0: return "*";
		 case 1: return "/";

		 case 2: return "**";
		 case 3: return "*/";
		 case 4: return "/*";
		 case 5: return "//";

		 case 6: return "***";
		 case 7: return "**/";
		 case 8: return "*/*";
		 case 9: return "*//";
		 case 10: return "/**";
		 case 11: return "/*/";
		 case 12: return "//*";
		 case 13: return "///";

		 case 14: return "****";
		 case 15: return "***/";
		 case 16: return "**/*";
		 case 17: return "**//";
		 case 18: return "*/**";
		 case 19: return "*/*/";
		 case 20: return "*//*";
		 case 21: return "*///";
		 case 22: return "/***";
		 case 23: return "/**/";
		 case 24: return "/*/*";
		 case 25: return "/*//";
		 case 26: return "//**";
		 case 27: return "//*/";
		 case 28: return "///*";
		 case 29: return "////";

		 case 30: return "*/*/*";
		 case 31: return "/**/*";

		 case 32: return "/*/*/";

		 
		 case 33: return "/*/*/*";
		 case 34: return "/**/*/";

		 case 35: return "*/*/*/";
		}
		return "";
	}


}
