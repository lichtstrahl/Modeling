package iv.root.modeling.modeling;

public class Processor {
    private Request request;
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

    public void receive(Request r) {
        request = r;
    }

    public Request release() {
        return request;
    }
}
