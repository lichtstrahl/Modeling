package iv.root.modeling.modeling;

import iv.root.modeling.network.Action;

class Event {
    private int time;
    private boolean scheduled;
    private Action<Integer> action;


    public int getTime() {
        return time;
    }

    public void scheduled(int time, Action<Integer> a) {
        this.time = time;
        action = a;
        scheduled = true;
    }

    public void action(int time) {
        action.run(time);
        scheduled = false;
    }

    public boolean isScheduled() {
        return scheduled;
    }
}