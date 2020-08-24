package entity.market;

import java.util.Random;

public class MarketUtils {

    public static int generateId() {
        return new Random().nextInt() * Integer.MAX_VALUE;
    }
}
