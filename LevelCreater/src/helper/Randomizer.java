package helper;

import java.util.Random;

public class Randomizer {
	public static Random random = new Random();

	public static int randomIntFromInterval(int min, int max)
	{
	    return (int) Math.floor(random.nextDouble()*(max-min+1)+min);
	}
}
