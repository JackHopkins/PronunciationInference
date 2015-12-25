import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.javatuples.Pair;
import org.paukov.combinatorics.ICombinatoricsVector;


public class Path {
	private static final float minimum = 0.0000001f;
	ICombinatoricsVector<Integer> distribution;
	String pattern;
	Word[] tokens;
	public Path(ICombinatoricsVector<Integer> distribution, String pattern, Word[] tokens) {
		this.distribution = distribution;
		this.pattern = new String(pattern);
		this.tokens = tokens;
	}

	public float getProbability() {
		float probability = 1;
		String current;
		String patternCopy = new String(pattern);
		for (int j = 0; j < distribution.getSize(); j++) {
			current = patternCopy.substring(0, distribution.getValue(j));								
			Word word = tokens[j];
			probability *= word.fst.getForwardPathProbability(current);//word.stress[Word.stressLookup(current)];
			if (probability < minimum) probability = minimum;

			if (((Float)probability).isNaN()) {
				throw new RuntimeException("Probability is NaN!");
			}
		}
		return probability;
	}
	public void normaliseFST() {
			
			for (int j = 0; j < distribution.getSize(); j++) {
				Word word = tokens[j];
				word.fst.normaliseFST();
			}
		
	}
	public float simpleNormaliseBy(float factor) {
		float currentProb = getProbability();
		String patternCopy = new String(pattern);
		String current;
		
		//float error = currentProb-factor;
		
		currentProb = 1.0f;
		
		for (int j = 0; j < distribution.getSize(); j++) {
			current = patternCopy.substring(0, distribution.getValue(j));
			patternCopy = patternCopy.substring(distribution.getValue(j));
			Word word = tokens[j];
			double nFactor = Math.pow(factor, 1/distribution.getSize());
			currentProb *= word.fst.normaliseTransitionsBy(nFactor, current);

		}

	return currentProb;
	}
	public float normaliseBy(float desired) {
		//FACTOR == GOAL
		float currentProb = getProbability();
		//float error = float.MAX_VALUE;
		//while (error > 0.0001d || error < -0.0001d) {
			String patternCopy = new String(pattern);
			String current;
			
			Double error = (double) (currentProb-desired);
			Double fstFactor = 0d;
			if(error < 0) {
			fstFactor = Math.pow(-error, 1.0d/tokens.length);
			fstFactor*=-1;
			}else{
			fstFactor = Math.pow(error, 1.0d/tokens.length);
			}
			currentProb = 1.0f;
			if (error.isNaN()) {
				throw new RuntimeException("This should not happen");
			}

			for (int j = 0; j < distribution.getSize(); j++) {
				current = patternCopy.substring(0, distribution.getValue(j));
				patternCopy = patternCopy.substring(distribution.getValue(j));
				Word word = tokens[j];
				double newFSTProb = word.fst.iterativeNormaliseTransitionsBy(error, current);
				currentProb *= newFSTProb;
			}

		return currentProb;
		


	}
	private static float[] iterativeSolver(float[] input, float goal) {
		float error = Float.MAX_VALUE;

		while (error > 0.0001d || error < -0.0001d) {
			float current = 1.0f;
			for (int i = 0; i < input.length; i++) {
				current*=input[i];
			}
			error = current-goal;
			for (int i = 0; i < input.length; i++) {
				input[i] += (1-input[i])*-error;
			}

			System.out.println(error);
		}
		return input;
	}
	public void normaliseByWord() {
		for (Word word : tokens) {
			word.count ++;
			word.fst.normaliseByNode();
		}
	}
	@Override
	public String toString() {
		return "Path - {"+pattern+" - "+distribution.toString()+"} - "+getProbability();
	}
	public String toVerboseString() {
		StringBuilder builder = new StringBuilder();
		String current;
		String patternCopy = new String(pattern);

		for (int j = 0; j < distribution.getSize(); j++) {
			current = patternCopy.substring(0, distribution.getValue(j));	
			patternCopy = patternCopy.substring(distribution.getValue(j));
			
			Word word = tokens[j];
			//word.stress[Word.stressLookup(current)];
			DecimalFormat df = new DecimalFormat("#.####");

			df.setRoundingMode(RoundingMode.FLOOR);
			builder.append(word.word+"{"+current+"-"+ df.format(word.fst.getForwardPathProbability(current))+"} ");
		}
		return builder.toString();
	}
	//Checks to see if this path is 
	public boolean isPossible() {
		String current;
		String patternCopy = new String(pattern);

		for (int j = 0; j < distribution.getSize(); j++) {
			current = patternCopy.substring(0, distribution.getValue(j));	
			patternCopy = patternCopy.substring(distribution.getValue(j));
			double prob = tokens[j].fst.getForwardPathProbability(current);
			if (prob < 0) {
				return false;
			}
		}
		return true;
	}
}
