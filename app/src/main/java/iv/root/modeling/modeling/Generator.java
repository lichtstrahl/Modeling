package iv.root.modeling.modeling;

public class Generator {
    private PoissonRandom random;

    private Generator(PoissonRandom random) {
        this.random = random;
    }

    public int timeForNextRequest() {
        return random.generate();
    }

    public static Generator getInstance(double lambda) {
        return new Generator(new PoissonRandom(lambda));
    }
}
