package iv.root.modeling.modeling;

import java.util.ArrayDeque;

public class Pull {
    private ArrayDeque<Request> queue;      // Очередь

    public Pull() {
        queue = new ArrayDeque<>();
    }

    public int getCurSize() {
        return queue.size();
    }

    public void put(Request r) {
        queue.addLast(r);
    }

    public Request get() {
        return queue.removeFirst();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
