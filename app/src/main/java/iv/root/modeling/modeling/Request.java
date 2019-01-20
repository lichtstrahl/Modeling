package iv.root.modeling.modeling;

import java.util.LinkedList;
import java.util.List;

public class Request {
    private int id;

    public Request(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static List<Request> getPullUniqueRequests(int count) {
        List<Request> requests = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            requests.add(new Request(i));
        }
        return requests;
    }
}
