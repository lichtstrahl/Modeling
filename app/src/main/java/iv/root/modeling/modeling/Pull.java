package iv.root.modeling.modeling;

import java.util.ArrayDeque;

public class Pull {
    private ArrayDeque<Request> queue;      // Очередь
    private int size;

    public Pull(int s) {
        queue = new ArrayDeque<>();
        size = s;
    }

    public int getCurSize() {
        return queue.size();
    }

    public boolean put(Request r) {
        if (queue.size() < size ) {
            queue.addLast(r);
            return true;
        }
        return false;
    }

    public Request get() {
        return queue.removeFirst();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
