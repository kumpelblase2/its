package Praktika2;

/**
 * 
 * @author Nam Lineare Kongruenz Formel: Xn=(a * Xn-1 + b) mod m
 */
public class LCG {
	/*
	 * Parameter nach IBM's RANDU
	 */
	private long a = 65539;
	private long b = 0;
	private long n = (long) Math.pow(2, 31);
	private long startValue;

	public LCG(long startValue) {
		this.startValue = startValue;
	}

	public LCG(long multiplikator, long inkrement, long modulus, long startValue) {
		a = multiplikator;
		b = inkrement;
		n = modulus;
		this.startValue = startValue;
	}

	/*
	 * This method generats a random number with the linear congruence method.
	 */
	public long nextValue() {
		startValue = (a * startValue + b) % n;
		return startValue;
	}

	public static void main(String[] args) {
		LCG lcg = new LCG(100);
		for (int i = 0; i < 20; i++) {
			System.out.println(lcg.nextValue());
		}
	}

}
