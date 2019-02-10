package iv.root.modeling.hospital;

import io.reactivex.annotations.Nullable;
import iv.root.modeling.center.TimeRandom;

// TODO Изменить максимальный размер очереди
public class ClientGenerator implements Unit<Client> {
    private int nextClientTime;
    private TimeRandom random;
    private int countClients;

    public ClientGenerator(int timeMin, int timeMax) {
        random = new TimeRandom(timeMin, timeMax);
        nextClientTime = 0;
        countClients = 0;
    }


    @Nullable
    @Override
    public Client step(int dt) {
        nextClientTime -= dt;
        if (nextClientTime <= 0) {
            nextClientTime = random.nextValue();
            return new Client(++countClients, 10, RandomEnum.randomEnum(DoctorType.class));
        }

        return null;
    }

    public int getCountClients() {
        return countClients;
    }
}
