package iv.root.modeling.modeling;

public class PoissonRandom {
    private double lambda;

    public PoissonRandom(double lambda) {
        this.lambda = lambda;
    }

    public int generate() {
        double L = Math.exp(-lambda);
        double p = 1.0;
        int k = 0;

        do {
            k++;
            p *= Math.random();
        } while (p > L);

        return k - 1;
    }
}
