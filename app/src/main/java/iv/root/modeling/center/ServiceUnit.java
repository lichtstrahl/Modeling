package iv.root.modeling.center;

/**
 * Какая-то более общая структура. Вычислительный блок
 */
public class ServiceUnit {
    private boolean active;         // Активен блок или нет
    private int serviceTime;     // Необходимое время на работу
    private TimeRandom timeRandom;

    public ServiceUnit(int minTime, int maxTime) {
        timeRandom = new TimeRandom(minTime, maxTime);
    }

    public void startService() {
        active = true;
        serviceTime = timeRandom.nextValue();
    }

    public void continueService(int dt) {
        if (active) {
            serviceTime -= dt;
            active = serviceTime > 0;
        }
    }

    public void stopService() {
        active = false;
        serviceTime = 0;
    }

    public boolean isActive() {
        return active;
    }

    public int getMinimalServiceTime() {
        return timeRandom.getMinTimeInterval();
    }
}
