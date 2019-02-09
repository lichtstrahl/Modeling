package iv.root.modeling.center;

public class ProcessingSystem {
    private boolean active;
    private int queueLength;
    private final ServiceUnit computer;

    public ProcessingSystem(int minTime, int maxTime) {
        computer = new ServiceUnit(minTime, maxTime);
        active = false;
        queueLength = 0;
    }

    public void putRequest() {
        active = true;

        if (computer.isActive()) {
            queueLength++;
        } else {
            computer.startService();
        }
    }

    /**
     * Прололжение вычислений
     * @param dt - временной шаг
     * @return - закончил ли вычислитель работу на этом шаге
     */
    public boolean continueService(double dt) {
        if (computer.isActive()) {
            computer.continueService(dt);
            if (!computer.isActive()) {
                return true;
            }
        } else if (queueLength > 0) {
            computer.startService();
            queueLength--;  // Не уверен, но это было бы логично
        } else {
            active = false;
        }
        return false;
    }

    public void stopServie() {
        active = false;
        computer.stopService();
    }

    public boolean isActive() {
        return active;
    }

    public int getQueueLength() {
        return queueLength;
    }
}

