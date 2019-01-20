package iv.root.modeling.modeling;

import com.jjoe64.graphview.series.DataPoint;

import io.reactivex.SingleObserver;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import iv.root.modeling.network.Action;
import iv.root.modeling.util.Subscribed;

public class ModelingObserver implements SingleObserver<DataPoint[]>, Subscribed {
    private Disposable disposable;
    @Nullable
    private Action<DataPoint[]> action;
    @Nullable
    private Action<Throwable> error;

    public ModelingObserver(Action<DataPoint[]> a, Action<Throwable> e) {
        action = a;
        error = e;
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onSuccess(DataPoint[] points) {
        if (action != null) action.run(points);
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
