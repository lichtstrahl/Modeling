package iv.root.modeling.center;

import java.util.Random;

public class TimeRandom {
    private Random random;
    private int minTimeInterval;
    private int maxTimeInterval;

    public TimeRandom(int min, int max) {
        this.random = new Random(System.currentTimeMillis());
        minTimeInterval = min;
        maxTimeInterval = max;
    }

    public int nextValue() {
        int delta = maxTimeInterval - minTimeInterval;
        return minTimeInterval + ((delta != 0) ? random.nextInt(delta) : 0);
    }

    public int getMinTimeInterval() {
        return minTimeInterval;
    }
}
