package iv.root.modeling.center;


/**
 * В систему приходят клиенты с интерфалом в 10+-2
 * Т.е. каждый клиент будет "геренировать" слудующего с помощью TimeRandom.
 * При этом при создании запоминаются временной интервал между заявками.
 *
 * Это генератор для клиентов! Не отдельный клиент.
 */
public class Client {
    private TimeRandom timeRandom;
    private int nextRequestInterval;

    public Client(int minRequestTime, int maxRequestTime) {
        timeRandom = new TimeRandom(minRequestTime, maxRequestTime);
        nextRequestInterval = 0;
    }

    /**
     * Что-то похожее на то, что переменная для клиента используется одна и она постоянно генерирует новых клиентов
     * @param dt - время, которое прошло
     * @return - был ли сгенерирован новый клиент
     */
    public boolean moveOn(int dt) {
        nextRequestInterval -= dt;
        if (nextRequestInterval < 0) {
            nextRequestInterval = timeRandom.nextValue();
            return true;
        }

        return false;
    }
}
