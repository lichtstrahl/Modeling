package iv.root.modeling.modeling;

import io.reactivex.SingleObserver;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import iv.root.modeling.network.Action;
import iv.root.modeling.util.Subscribed;

public class ModelingObserver implements SingleObserver<Integer>, Subscribed {
    private Disposable disposable;
    @Nullable
    private Action<Integer> action;
    @Nullable
    private Action<Throwable> error;

    public ModelingObserver(Action<Integer> a, Action<Throwable> e) {
        action = a;
        error = e;
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onSuccess(Integer integer) {
        if (action != null) action.run(integer);
    }

    @Override
    public void onError(Throwable e) {
        if (error != null) error.run(e);
    }

    @Override
    public void unsubscribe() {
        if (disposable != null) disposable.dispose();
        action = null;
        error = null;
    }
}
