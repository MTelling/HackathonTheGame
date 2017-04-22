package Testing;

public class Program {
	public Object[] run(String[] args) {
		int k = 0;
		for(int i = 0; i < Math.pow(2, 31); i ++) {
			k++;
		}
		return new Object[] {Integer.parseInt(args[0]) + Integer.parseInt(args[1])};
	}
}