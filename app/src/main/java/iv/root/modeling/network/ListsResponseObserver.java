package iv.root.modeling.network;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import iv.root.modeling.network.dto.ContainerForList;

public class ListsResponseObserver implements Observer<ContainerForList> {
    private Disposable disposable;
    private Action<Throwable> error;
    private Action<ContainerForList> next;

    public ListsResponseObserver(Action<ContainerForList> action, Action<Throwable> e) {
        next = action;
        error = e;
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(ContainerForList containerForList) {
        if (next != null) next.run(containerForList);
    }

    @Override
    public void onError(Throwable e) {
        if (error != null) error.run(e);
    }

    @Override
    public void onComplete() {

    }

    public void unsubscribe() {
        if (disposable != null) disposable.dispose();
    }
}
