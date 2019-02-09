package iv.root.modeling.center;

/**
 * Класс представляет собой не только оператора, но ещё и связь с компьютером
 */
public class Operator extends ServiceUnit {
    private final ProcessingSystem processingSystem;

    public Operator(int minTime, int maxTime, ProcessingSystem system) {
        super(minTime, maxTime);
        processingSystem = system;
    }

    /**
     * Если работал до этого и не работает сейчас, значит обработка закончилась и можно принять новую заявку
     * @param dt
     */
    @Override
    public void continueService(double dt) {
        boolean wasActive = isActive();
        super.continueService(dt);
        if (wasActive && !isActive()) {
            processingSystem.putRequest();
        }
    }

    public int getQueueLength() {
        return processingSystem.getQueueLength();
    }
}
