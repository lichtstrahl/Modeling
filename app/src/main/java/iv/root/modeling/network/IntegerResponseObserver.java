package iv.root.modeling.network;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import iv.root.modeling.app.App;

public class IntegerResponseObserver<Response> implements SingleObserver<Response> {
    // Подписка
    private Disposable disposable;
    // Callback при успешном завершении
    Action<Response> success;
    // Callback при неудачном завершении
    Action<Throwable> error;

    public IntegerResponseObserver(Action<Response> s, Action<Throwable> e) {
        success = s;
        error = e;
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
        App.logI("Подписка на получение данных");
    }

    @Override
    public void onSuccess(Response responseRandomDTO){
        App.logI("Ответ получен");
        success.run(responseRandomDTO);
    }

    @Override
    public void onError(Throwable e) {
        App.logE("Ошибка при работе с сетью. \n" + e.getMessage());
        error.run(e);
    }

    public void dispose() {
        disposable.dispose();
        success = null;
        error = null;
        App.logI("Отписка от получения данных");
    }
}
