package iv.root.modeling.hospital;

import io.reactivex.annotations.Nullable;
import iv.root.modeling.center.TimeRandom;

public class Operator implements Unit<Client> {
    private TimeRandom timeRandom;
    private int timeEndReceive;     // Время окончания регистрации пациента
    private boolean active;
    private Client curClient;
    private int printTime;

    public Operator(int t0, int t1, int pt) {
        timeRandom = new TimeRandom(t0, t1);
        active = false;
        timeEndReceive = 0;
        printTime = pt;
    }

    public void startReceive(Client client) {
        if (isActive()) throw new IllegalStateException("Оператор уже занят");
        active = true;
        curClient = client;
        timeEndReceive = timeRandom.nextValue();
    }

    public void startPrint(Client client) {
        if (isActive()) throw new IllegalStateException("Оператор уже занят");
        active = true;
        curClient = client;
        curClient.setNeedPrint(true);
        timeEndReceive = printTime;
    }


    /**
     * На каждом шагу у оператора (если он занят) есть возможность завершить обслуживание и отпустить пациента
     * @param dt
     * @return
     */
    @Override
    public Client step(int dt) {
        if (isActive()) {
            timeEndReceive -= dt;
            if (timeEndReceive <= 0) {
                active = false;
                return curClient;
            }
        }
        return null;
    }

    public boolean isActive() {
        return active;
    }
}
