package iv.root.modeling.hospital;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.annotations.Nullable;

public class RegisterOffice implements Unit<List<Client>> {
    private ArrayDeque<Client> queue;       // Очередь клиентов
    private ArrayDeque<Client> vipQueue;    // Очередь для "внеочереди"
    private int nextReg;                    // Время окончания регистрации
    private Operator[] operators;

    public RegisterOffice(int regMin, int regMax) {
        queue = new ArrayDeque<>();
        vipQueue = new ArrayDeque<>();
        operators = new Operator[] {
          new Operator(regMin, regMax),
          new Operator(regMin, regMax),
          new Operator(regMin, regMax)
        };
    }

    public void putClient(Client client) {
        queue.addLast(client);
    }

    /**
     * На каждом шагу моделирования регистратура будет возвращать список клиентов, готовых к походу к врачу
     * @param dt
     * @return
     */
    @Override
    public List<Client> step(int dt) {
        List<Client> clients = new LinkedList<>();
        for (Operator op : operators) {
            Client client = op.step(dt);
            if (client != null) {
                clients.add(client);
            }
        }

        // Если очереди не пусты и есть свободные операторы
        for (Operator o = freeOperator(); o != null && (!queue.isEmpty() || !vipQueue.isEmpty()); o = freeOperator()) {
            if (!vipQueue.isEmpty()){
                o.startReceive(vipQueue.removeFirst());
            } else if (!queue.isEmpty()) {
                o.startReceive(queue.removeFirst());
            }
        }

        return clients;
    }

    /**
     *
     * @return - null, если все оператору заняты
     */
    @Nullable
    private Operator freeOperator() {
        for (Operator op : operators) {
            if (!op.isActive()) {
                return op;
            }
        }
        return null;
    }
}
