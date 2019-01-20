package iv.root.modeling.modeling;

public class Processor {
    private UniformRandom uniformRandom;    // Генератор времени для обработки

    private Processor(UniformRandom uniformRandom) {
        this.uniformRandom = uniformRandom;
    }

    public int timeForProcessingRequest() {
        return uniformRandom.generate();
    }

    public static Processor getInstance(int a, int b) {
        return new Processor(new UniformRandom(a, b));
    }
}
