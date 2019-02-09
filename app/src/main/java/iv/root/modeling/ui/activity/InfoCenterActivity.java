package iv.root.modeling.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import iv.root.modeling.R;
import iv.root.modeling.app.App;
import iv.root.modeling.center.Model;

public class InfoCenterActivity extends AppCompatActivity {
    @BindView(R.id.inputDT)
    EditText inputDT;
    @BindView(R.id.viewResult)
    ViewGroup viewResult;
    @BindView(R.id.viewCountRequest)
    TextView viewCountRequest;
    @BindView(R.id.viewMissingRequest)
    TextView viewMissingRequest;
    @BindView(R.id.viewP)
    TextView viewP;
    private Disposable disposableResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_center);
        ButterKnife.bind(this);

    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, InfoCenterActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_modeling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemRunModeling:
                startModeling();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (disposableResult != null) disposableResult.dispose();
    }

    private void startModeling() {
        int dt = Integer.valueOf(inputDT.getText().toString());
        if (dt > 10) {
            Toast.makeText(this, R.string.incorrectData, Toast.LENGTH_LONG).show();
        }
        Model model = new Model(dt);
        model.restart();

        viewResult.setVisibility(View.GONE);
        disposableResult = Single.fromCallable(() -> {
            while (model.getCountRequest() < 300) {
                model.step();
            }

            return  (double)model.getCountMissRequest() / (model.getCountRequest() + model.getCountMissRequest());
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(p -> {
            viewResult.setVisibility(View.VISIBLE);

            viewCountRequest.setText("Заявок обработано: " + model.getCountRequest());
            viewMissingRequest.setText("Заявок отклонено: " + model.getCountMissRequest());
            viewP.setText(String.format(Locale.ENGLISH,"Вероятность отказа: %d%%", Math.round(p*100)));

        });
    }
}
