package iv.root.modeling.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import iv.root.modeling.app.App;
import iv.root.modeling.network.Action;

public class ListenerEditText implements TextWatcher {
    private Disposable disposable;
    private PublishSubject<String> subject;

    public ListenerEditText(EditText ed) {
        subject = PublishSubject.create();
        ed.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Не используются
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        subject.onNext(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {
        // Не используются
    }

    public void subscribe(Action<String> action) {
        disposable = subject.subscribe(
                action::run,
                error -> App.logE(error.getMessage())
        );
    }

    public void unsubscribe() {
        disposable.dispose();
    }
}
