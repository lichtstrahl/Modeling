package iv.root.modeling.hospital;

import android.support.annotation.Nullable;

import java.util.ArrayDeque;

import iv.root.modeling.center.TimeRandom;

public class Doctor implements Unit<Client> {
    private ArrayDeque<Client> queue;
    private TimeRandom timeRandom;
    private boolean active;
    private int endReceive;             // Окончание очередного приёма
    private Client curClient;
    private DoctorType type;

    public Doctor(DoctorType type, int docMin, int docMax) {
        timeRandom = new TimeRandom(docMin, docMax);
        queue = new ArrayDeque<>();
        active = false;
        endReceive = 0;
        this.type = type;
    }

    public void putClient(Client client) {
        queue.addLast(client);
    }

    private void startReceive(Client client) {
        if (isActive()) throw new IllegalStateException("Врач уже занят");
        active = true;
        endReceive = timeRandom.nextValue();
        curClient = client;
    }

    private Client finishReceive() {
        active = false;
        return curClient;
    }

    @Nullable
    @Override
    public Client step(int dt) {
        if (isActive()) {
            endReceive -= dt;
            if (endReceive <= 0) {
                return finishReceive();
            }
        } else if (!queue.isEmpty()) {
            startReceive(queue.removeFirst());
        }

        return null;
    }

    public boolean isActive() {
        return active;
    }

    public DoctorType getType() {
        return type;
    }

    public int getClients() {
        return queue.size();
    }
}
