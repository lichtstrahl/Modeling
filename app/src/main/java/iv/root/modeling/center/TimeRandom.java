package iv.root.modeling.center;

import java.util.Random;

public class TimeRandom {
    private Random random;
    private int minTimeInterval, maxTimeInterval;

    public TimeRandom(int min, int max) {
        this.random = new Random(System.currentTimeMillis());
        minTimeInterval = min;
        maxTimeInterval = max;
    }

    public double nextValue() {
        int delta = maxTimeInterval - minTimeInterval;
        return minTimeInterval + random.nextInt(delta) + random.nextDouble();
    }

    public int getMinTimeInterval() {
        return minTimeInterval;
    }
}
