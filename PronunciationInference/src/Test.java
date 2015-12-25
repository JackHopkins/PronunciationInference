
public class Test {

	public static void main(String[] args) {
		double[] vals1 = new double[]{0.5, 0.5, 0.5}; 
		double[] vals2 = new double[]{0.9, 0.2, 0.7}; 
		double[] vals3 = new double[]{0.4, 0.5, 0.625};
		
		double p1 = 1.0d;
		double p2 = 1.0d;
		double p3 = 1.0d;
		for (int i = 0; i < vals1.length; i++) p1*=vals1[i];
		for (int i = 0; i < vals2.length; i++) p2*=vals2[i];
		for (int i = 0; i < vals3.length; i++) p3*=vals3[i];

		double sum = p1+p2+p3;
		
		vals1 = iterativeSolver(vals1, p1/sum);
		vals2 = iterativeSolver(vals2, p2/sum);
		vals3 = iterativeSolver(vals3, p3/sum);
		
		
		double factor1 = Math.pow(sum, 1.0/vals1.length);///(sum);
		double factor2 = Math.pow(sum, 1.0/vals2.length);///(sum);
		double factor3 = Math.pow(sum, 1.0/vals3.length);///(sum);

		double pp1 = new Double(p1);
		double pp2 = new Double(p2);
		double pp3 = new Double(p3);
		p1 = 1.0d;
		p2 = 1.0d;
		p3 = 1.0d;
		System.out.println(sum);
		for (int i = 0; i < vals1.length; i++){
			//vals1[i] *= factor1;
			//vals1[i]+= ((1-vals1[i]) * factor1);
			//vals1[i] /= factor1;
			p1 *= vals1[i];
		}
		for (int i = 0; i < vals2.length; i++){
			//if (vals2[i] < factor2) {
			//vals2[i] /= factor2;
			//}else{
			//vals2[i] += (1-vals2[i])*(1-factor2); //factor2;
			//}
			p2 *= vals2[i];
		}
		for (int i = 0; i < vals3.length; i++){
			//vals1[i] *= factor1;
			//vals1[i]+= ((1-vals1[i]) * factor1);
			//vals3[i] /= factor3;
			p3 *= vals3[i];
		}
		System.out.println(p1+p2+p3);
	}
	public static double[] iterativeSolver(double[] input, double goal) {
		double error = Double.MAX_VALUE;
		
		while (error > 0.0001d || error < -0.0001d) {
				double current = 1.0d;
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
	public static double sig(double x)
	{//((Input - InputLow) / (InputHigh - InputLow)) * (OutputHigh - OutputLow) + OutputLow;
		//x = ((x-min)/(max-min));
		return 1 / (1 + Math.exp(-x));
	}
}
