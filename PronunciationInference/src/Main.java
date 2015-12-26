import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.javatuples.Pair;
import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.primitives.Ints;


public class Main {

	private static final String trainingPath = 	"data/sonnets.txt";
	private static final String resultsPath = 	"results/";
	private static final String cmuDictionary = "data/cmudict.txt";
	private static final String testFile = 		"data/test.txt";
	
	private static String[] testLines = new String[]{
		"From fairest creatures we desire increase",
		"That thereby beautys rose might never die",
		"That time of year thou mayst in me behold",
		"When yellow leaves or none or few do hang",
		"Upon those boughs which shake against the cold"};
	private static String[] testStresses = new String[]{
		"* /* /* / */ */",
		"* /* /* / * /* /",
		"* / * / * / * / */",
		"* /* / * / * / * /",
		"*/ * / * / */ * /",

		};
	private static Double[] testLastBest = new Double[]{0.0, 0.0, 0.0, 0.0, 0.0};
	static final int MAX_ITERATIONS = 20;
	static Map<String, Word> dictionary = new ConcurrentHashMap<String, Word>();
	static Map<Integer, List<ICombinatoricsVector<Integer>>> distributions = new ConcurrentHashMap<Integer, List<ICombinatoricsVector<Integer>>>();
	//static float lambda = 0.08f;

	static int epochs = 1000;
	public static AtomicInteger line = new AtomicInteger(0);
	public static int totalDataLength = 0;
	public static void main(String[] args) throws IOException, InterruptedException {


		//List<Word[]> data = loadTrainingData();


		for (int i = 1; i < 10; i++)
			distributions.put(i, buildDistribution(i, 10));

		List<Hyperparameters> candidateParams = Arrays.asList(new Hyperparameters(epochs, 0.999f, 1.2f));

		learn(candidateParams);
		/*candidateParams = createHyperparameters(10);
		for (int i = 0; i < 10; i++) {
			System.out.println("    Generation: "+i);
			learn(candidateParams);
			Collections.sort(candidateParams);
			List<Hyperparameters> newCandidates = new ArrayList<Hyperparameters>();
			newCandidates.addAll(createHyperparameters(candidateParams.get(0), 4));
			newCandidates.addAll(createHyperparameters(candidateParams.get(1), 3));
			newCandidates.addAll(createHyperparameters(candidateParams.get(2), 2));
			newCandidates.add(candidateParams.get(0));
			candidateParams.clear();
			candidateParams.addAll(newCandidates);
		}*/
	}
	public static void learn(List<Hyperparameters> candidateParams) throws IOException, InterruptedException {
		List<Word[]> data = loadTrainingData();
		totalDataLength = data.size();
		//Collections.shuffle(data);
		System.out.println(evaluate());
		for (Hyperparameters candidateParam : candidateParams) {
			float attenuation = 1f;
			float attenuationFactor = new Float(candidateParam.attenuationFactor);
			for (int x = 0; x < candidateParam.epochs; x++) {
				for (Word[] tokens : data) {
					for (Word word: tokens) {
						word.count++;
					}
				}
				long time = System.currentTimeMillis();
				float averageCandidates = 0;
				System.out.println("NEW EPOCH");
				
				ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
				List<List<Word[]>> partitionedData = Lists.partition(data, 100);
				int taskNum = 0;
				for (List<Word[]> dataPart : partitionedData) {
					Task newTask = new Task(taskNum+++"",dataPart, x);
					executor.execute(newTask);
				}
				
				executor.shutdown();
				executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
				Main.line.set(0);
				printEpochOutput(x);
				System.out.println("Average candidates: "+averageCandidates/data.size());
				System.out.println("Epoch time: "+(System.currentTimeMillis()-time));
				attenuation *= attenuationFactor;

				System.out.println(Arrays.toString(dictionary.get("abundance").stress));
			}
			int error = getTestError();
			candidateParam.score = error;
			System.out.println(candidateParam);
			dictionary.clear();
			data = loadTrainingData();
		}
	}
	private static void printEpochOutput(int x) throws IOException {
		StringBuilder builder = new StringBuilder();
		builder.append(evaluate());
		for (Word word: dictionary.values()) {
			builder.append(word.toStringVerbose()+"\n");
		}
		
		File file = new File(resultsPath+x);
		file.getParentFile().mkdirs();
		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(builder.toString());
		bw.close();

		
	}
	static String evaluate() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < testLines.length; i++) {
		builder.append(testLine(testLines[i], testStresses[i],i)+"\n");
		}
		//builder.append(testLine(testLines[2], testStresses[2], testLastBest[2])+"\n");
		return builder.toString();
	}
	static String testLine(String line, String stress, int lastBest) {
		String[] split = line.split(" ");
		String[] stresses = stress.split(" ");
		double prob = 1.0d;
		String maxEnt = "";
		for (int i = 0; i < split.length; i++) {
			Word word = dictionary.get(split[i].toLowerCase());
			prob *= word.fst.getForwardPathProbability(stresses[i]);
			maxEnt+=word.word+" {"+word.getMaxEnt()+"} ";
		}
		
		String out = "Last P = "+testLastBest[lastBest]+"\nCurrent P = "+prob+"\n"+maxEnt;
		testLastBest[lastBest] = prob;
		return out;
	}
	private static List<Hyperparameters> createHyperparameters(int num) {
		List<Hyperparameters> params = new ArrayList<Hyperparameters>();
		for (int i = 0; i < num; i++) {
			Hyperparameters param = new Hyperparameters();
			param.attenuationFactor = (float) (1-(Math.random()/10));
			param.epochs = 100;//(int) Math.round(Math.random()*20);
			param.lambda = (float) (1+Math.random()/10);
			params.add(param);
		}
		return params;
	}
	private static List<Hyperparameters> createHyperparameters(Hyperparameters obj, int num) {
		List<Hyperparameters> params = new ArrayList<Hyperparameters>();
		for (int i = 0; i < num; i++) {
			Hyperparameters param = new Hyperparameters();

			param.attenuationFactor = obj.attenuationFactor+(1-obj.attenuationFactor)*(float)((Math.random()-0.5)*0.2);
			param.epochs = 100;//(obj.epochs +  (int)Math.round((Math.random()-0.5)*2));
			param.lambda = (obj.lambda + (float)(Math.random()-0.5)/100);
			params.add(param);
		}
		return params;
	}
	private static List<Word[]> loadTrainingData() throws IOException {


		Scanner scan = new Scanner(new File(trainingPath));
		String line="";
		List<Word[]> data = new ArrayList<Word[]>();

		while (scan.hasNextLine())
		{
			line = scan.nextLine();
			String[] split=line.trim().replaceAll("[+-.,!@#$%^&*();\\/|<>\"']", "").split("[\\s\\.,!?]+");
			Word[] words = new Word[split.length];
			for (int i = 0; i < split.length; i++) {
				String splitted = split[i].trim().toLowerCase();
				if (!dictionary.containsKey(splitted)) {
					dictionary.put(splitted, new Word(splitted));
				}
				words[i] = dictionary.get(splitted);

			}
			data.add(words);
		}
		//initialiseWordsWithCMUPronunciations();

		return data;
	}
	private static void initialiseWordsWithCMUPronunciations()
			throws IOException {
		System.out.println("Initialising words with pronunciations taken from the CMU dataset");
		for (String line : Files.readLines(new File(cmuDictionary), Charset.defaultCharset())){
			if (line.startsWith(";;;")) continue;
			String[] data = line.split("  ");
			String splitted = data[0].replaceAll("\\(\\d\\)", "").toLowerCase();
			List<String> pronunciations = parseCmuPronunciations(data[1]);
			/*if (!dictionary.containsKey(splitted)) {
				Word word = new Word(splitted);
				word.initialPronunciations.addAll(pronunciations);
				System.out.println("Processing "+splitted);
				dictionary.put(splitted, word);
			}else{*/
			if (dictionary.containsKey(splitted)) {
				dictionary.get(splitted).initialPronunciations.addAll(pronunciations);
			}
			//}

		}
		for (Word word : dictionary.values()) {
			if (!word.initialPronunciations.isEmpty()) {
				for (int i = 0; i < 100; i++) {

					word.fst.addInitialPronunciations();
				}
				System.out.println(word.toStringVerbose());
			}
		}
	}


	private static List<String> parseCmuPronunciations(String string) {
		String strippedPronunciation = string.replaceAll("[^0-9]", "");
		List<String> representationsOfTwo = new ArrayList<String>();
		strippedPronunciation = strippedPronunciation.replaceAll("0", "/");
		strippedPronunciation = strippedPronunciation.replaceAll("1", "*");
		representationsOfTwo.add(strippedPronunciation.replace('2', '/'));
		representationsOfTwo.add(strippedPronunciation.replace('2', '*'));
		return representationsOfTwo;
	}
	public static int getTestError() throws FileNotFoundException {
		Scanner scan = new Scanner(new File(testFile));
		String line="";
		int error = 0;
		while (scan.hasNextLine())
		{
			line = scan.nextLine();
			String[] split=line.trim().replaceAll("[+-.,!@#$%^&*();\\/|<>\"']", "").split("[\\s\\.,!?]+");
			Word[] words = new Word[split.length];
			StringBuilder checker = new StringBuilder();
			for (int i = 0; i < split.length; i++) {
				words[i] = dictionary.get(split[i].toLowerCase());
				checker.append(words[i].getMaxEnt());
			}
			char stress = '*';
			String output = checker.toString();
			int count = 0;
			for (int i = 0; i < output.length(); i++) {
				if (output.charAt(i) == '*' && stress == '*') {

					count ++;
				}
				if (output.charAt(i) == '/' && stress == '/') {
					stress = '*';
					count ++;
				}
				if (stress == '*') {
					stress = '/';
				}else{
					stress = '*';
				}
			}
			for (Word w : words) {
				System.out.print(w);
			}
			System.out.println();
			error += count;
			//System.out.println(line+" "+count);
		}
		return error;
	}
	public static List<ICombinatoricsVector<Integer>> buildDistribution(int words, int syllables) {
		List<ICombinatoricsVector<Integer>> combinations = new ArrayList<ICombinatoricsVector<Integer>>();
		float sum = 0;
		Set<int[]> stacks = new HashSet<int[]>();

		Generator<Integer> gen = Factory.createCompositionGenerator(syllables);

		for (ICombinatoricsVector<Integer> p : gen) {
			int size = p.getSize();
			if (p.getSize() == words) {
				combinations.add(p);
				//System.out.println(p);
			}

		}
		//for (int i = 0; i < counts.length; i++)
		//	counts[i] /= sum;

		return combinations;
	}
	private static void print(float[] stack) {
		StringBuilder sb = new StringBuilder();
		for (Float i : stack) {
			sb.append(i).append("+");
		}
		System.out.println(sb.deleteCharAt(sb.length() - 1).toString());
	}
	public static void populateSubset(final int[] data, int fromIndex, 
			final int[] stack, final int stacklen,
			final int target,
			final Set<int[]> stacks) {
		if (target == 0) {
			// exact match of our target. Success!

			stacks.add(Arrays.copyOf(stack, stacklen));
			//return;
		}

		while (fromIndex < data.length && data[fromIndex] > target) {
			// take advantage of sorted data.
			// we can skip all values that are too large.
			fromIndex++;
		}

		while (fromIndex < data.length && data[fromIndex] <= target) {
			// stop looping when we run out of data, or when we overflow our target.
			stack[stacklen] = data[fromIndex];
			populateSubset(data, fromIndex + 1, stack, stacklen + 1, target - data[fromIndex], stacks);
			fromIndex++;
		}
	}
}