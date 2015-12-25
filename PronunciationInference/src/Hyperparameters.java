
public class Hyperparameters implements Comparable{

	public int epochs;
	public float attenuationFactor;
	public float lambda;
	public int score;
	public Hyperparameters() {
		
	}
	public Hyperparameters(int epochs, float att, float lambda) {
		this.epochs = epochs;
		this.attenuationFactor = att;
				this.lambda = lambda;
		this.score = score;
	}
	@Override
	public int compareTo(Object o) {
		if (score > ((Hyperparameters)o).score) return 1;
		if (score == ((Hyperparameters)o).score) return 0;
		return -1;
	} 
	@Override
	public String toString() {
		return "Epochs: "+epochs+", AF: "+attenuationFactor+", Lambda: "+lambda+", Errors: "+score;
	}
	
}
