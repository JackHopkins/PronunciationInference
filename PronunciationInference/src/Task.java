import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.paukov.combinatorics.ICombinatoricsVector;


public class Task implements Runnable {
	
	private String name;
	private List<Word[]> dataPart;
	private int epoch;
	static String[] models = new String[]{"*/*/*/*/*/", "/**/*/*/*/", "*/*/*/*/*/*", "/**/*/*/*/*"};
	public Task(String name, List<Word[]> dataPart, int epoch) {
		this.name = name;
		this.dataPart = dataPart;
		this.epoch = epoch;
			}
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	@Override
	public void run() {
		for (Word[] tokens : dataPart) {
			Main.line.incrementAndGet();
			//Only train from lines with more than 3 words and less than 10
			if (tokens.length > 3 && tokens.length < 10) {

				List<ICombinatoricsVector<Integer>> dist = Main.distributions.get(tokens.length);

				//S S* S S* S S* S S* S S*
				//S* S S S* S S* S S* S S*
				//S S* S S* S S* S S* S S* S
				//S* S S S* S S* S S* S S* S


				List<Path> bestCandidates = new ArrayList<Path>();
				//float bestProb = 0d;					
				float sumProbability = 0;
				for (ICombinatoricsVector<Integer> vector : dist) {
					//We assume that no words have more than 5 syllables
					if (!(vector.contains(6) || vector.contains(7) || vector.contains(8) || vector.contains(9))) {
						for (int j = 0; j < models.length; j++) {
							//bestCandidates.add(new Path(vector, models[j], tokens));
							Path candidate = new Path(vector, models[j], tokens);
							if (candidate.isPossible()) {
								bestCandidates.add(candidate);
							}
						}
					}
				}
				if (bestCandidates.isEmpty()) {
					System.err.println("This line is not iambic : "+tokens);
					continue;
				}
				//NORMALISE ALL TRANSITION PROBABILITIES SO THEIR SUM IS 1

				float error = Float.MAX_VALUE;

				float bestProb = 0.0f;
				Path best = null;
				int count = 0;
				long sTime = System.currentTimeMillis();
				while ((error > 0.0001d || error < -0.0001d) && count < Main.MAX_ITERATIONS) {
					count++;

					sumProbability = 0;

					error = 1.0f - iterativeNormalisePaths(bestCandidates);
				}

				System.out.println(Main.line+"/"+Main.totalDataLength+"	"+(System.currentTimeMillis()-sTime)+" - 	"+epoch+" Expectation:		"+getMaximumLikelihood(bestCandidates).toVerboseString());

				//float sum = 0.0;
				sTime = System.currentTimeMillis();
				bestProb = 0.0f;
				for (Path candidate : bestCandidates) {
					//sum += candidate.getProbability();	
					candidate.normaliseFST();
					float newProb = candidate.getProbability();

					if (newProb > bestProb) {
						bestProb = newProb;
						best = candidate;
					}
					//System.out.println(candidate.pattern+" - "+candidate.getProbability());
				}
				System.out.println("-		"+(System.currentTimeMillis()-sTime)+" - 	"+count+" Maximisation:	"+getMaximumLikelihood(bestCandidates).toVerboseString());

				/*Path best = null;
				Double prob = 0.0d;
				for (Path candidate : bestCandidates) {
					float cProb = candidate.getProbability();

					if (cProb > prob) {
						prob = cProb;
						best = candidate;
					}
				}
				int k = 0;*/
			}
			//Word word = dictionary.get("the");
			//System.out.println(word.word +" - "+word.count+" "+word.fst.toVerboseString());

		}
	}
	private static float simpleNormalisePaths(List<Path> bestCandidates) {
		float localSumProbability = sumPath(bestCandidates);
		float sumProbability = 0.0f;
		for (Path candidate : bestCandidates) {
			sumProbability += candidate.simpleNormaliseBy(localSumProbability);
		}
		return sumPath(bestCandidates);
	}
	/**
	 * Normalises the paths
	 * @param bestCandidates A set of paths who's probalities must sum to 1
	 * @return The sum probability of all normalised paths
	 */
	private static float iterativeNormalisePaths(List<Path> bestCandidates) {
		float sumProbability = 0.0f;
		Float localSumProbability = 0f;
		Float[] desiredProb = new Float[bestCandidates.size()];
		for (int i = 0; i < desiredProb.length; i++) {

			//For every path, the find the factor to bring the path's value up to expected levels.
			//TRY WITHOUT THIS LINE?
			
			Path candidate = bestCandidates.get(i);
			float candidateProb = candidate.getProbability();
			//float desiredCandidateProb = candidateProb/localSumProbability;
			desiredProb[i] = candidateProb;///localSumProbability;
			//float factor = desiredCandidateProb/candidateProb;
			//candidate.normaliseBy(desiredCandidateProb);///sumProbability);

			//float newProb = candidate.getProbability();
			/*if (newProb > bestProb) {
				bestProb = newProb;
				best = candidate;
			}*/
			//sumProbability += newProb;//newProb;
			//System.out.println(newProb);
		}
		for (int i = 0; i < desiredProb.length; i++) {
			Path candidate = bestCandidates.get(i);
			localSumProbability = sumPath(bestCandidates);
			candidate.normaliseBy(candidate.getProbability()/localSumProbability);
			float newProb = candidate.getProbability();
			sumProbability += newProb;
		}
		return sumProbability;
	}
	private static float sumPath(List<Path> paths) {
		float sumProbability = 0;
		for (Path candidate2 : paths) {
			sumProbability += candidate2.getProbability();
		}	
		return sumProbability;
	}
	private static Path getMaximumLikelihood(List<Path> bestCandidates) {
		Path best = bestCandidates.get(0);
		float prob = 0.0f;
		for (Path candidate : bestCandidates) {
			float cProb = candidate.getProbability();

			if (cProb >= prob) {
				prob = cProb;
				best = candidate;
			}
		}
		if (prob == 0.0f) {
			throw new RuntimeException("Probabilities not set");
		}
		return best;
	}
}
