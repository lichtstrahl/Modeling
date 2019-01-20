package iv.root.modeling.modeling;

import java.util.Random;

/**
 * Генератор случайных чисел.
 * Диапазон [a,b] задаётся.
 * Распределение: равномерное (по умолчанию в Java)
 */
public class UniformRandom {
    private int a, b;
    private Random random;

    public UniformRandom(int a, int b) {
        this.a = a;
        this.b = b;
        random = new Random();
    }

    public int generate() {
        return a + random.nextInt(b-a);
    }
}
