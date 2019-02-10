package iv.root.modeling.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import iv.root.modeling.R;
import iv.root.modeling.app.App;
import iv.root.modeling.hospital.Model;

public class HospitalActivity extends AppCompatActivity {
    @BindView(R.id.inputClientMin)
    EditText inputClientMin;
    @BindView(R.id.inputClientMax)
    EditText inputClientMax;
    @BindView(R.id.inputRegMin)
    EditText inputRegMin;
    @BindView(R.id.inputRegMax)
    EditText inputRegMax;
    @BindView(R.id.inputProcessMin)
    EditText inputProcessMin;
    @BindView(R.id.inputProcessMax)
    EditText inputProcessMax;
    @BindView(R.id.inputPrintP)
    EditText inputPrintP;
    @BindView(R.id.inputPrint)
    EditText inputPrintTime;
    @BindView(R.id.viewResult)
    TextView viewResult;
    private Disposable disposableResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        ButterKnife.bind(this);
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, HospitalActivity.class);
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
                viewResult.setVisibility(View.GONE);
                disposableResult = Single.fromCallable(()-> {
                    Model hospital = new Model(
                            Integer.valueOf(inputClientMin.getText().toString()),
                            Integer.valueOf(inputClientMax.getText().toString()),
                            Integer.valueOf(inputRegMin.getText().toString()),
                            Integer.valueOf(inputRegMax.getText().toString()),
                            Integer.valueOf(inputProcessMin.getText().toString()),
                            Integer.valueOf(inputProcessMax.getText().toString()),
                            Integer.valueOf(inputPrintTime.getText().toString()),
                            Integer.valueOf(inputPrintP.getText().toString())
                    );
                    hospital.modeling();
                    return hospital;
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(hospital -> {
                            viewResult.setVisibility(View.VISIBLE);
                            String text = "All: " + hospital.getAllClient() + "\n" +
                                    "Miss: " + hospital.getMissClient() + "\n" +
                                    String.format(Locale.ENGLISH, "Вероятность отказа: %d%%", Math.round(100.0*hospital.getMissClient()/hospital.getAllClient()));
                            viewResult.setText(text);
                        });

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
}
