package pl.mbassara.battleships.enums;

import java.util.Random;

public enum WhoStarts {
	HOST_STARTS,
	CLIENT_STARTS;
	
	private static final Random RANDOM = new Random();
	public static WhoStarts getRandom()  {
		if(RANDOM.nextBoolean())
			return HOST_STARTS;
		else
			return CLIENT_STARTS;
	}
}
