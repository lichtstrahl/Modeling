package iv.root.modeling.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import iv.root.modeling.R;
import iv.root.modeling.app.App;
import iv.root.modeling.network.IntegerResponseObserver;
import iv.root.modeling.network.dto.ResponseQRNS;
import iv.root.modeling.util.Evaluation;
import iv.root.modeling.util.IntegerAdapter;
import iv.root.modeling.ui.util.ListenerEditText;

public class RandomActivity extends AppCompatActivity {
    private static final int COUNT_VALUE = 100;    // Количество запрашиваемых чисел

    @BindView(R.id.progressBarLow)
    ProgressBar barLow;
    @BindView(R.id.progressBarMiddle)
    ProgressBar barMiddle;
    @BindView(R.id.progressBarHigh)
    ProgressBar barHigh;
    @BindView(R.id.input)
    EditText input;
    @BindView(R.id.labelInput)
    TextView labelInput;

    private ListenerEditText listenerInput;

    // 0 ... 9
    private RecyclerView listLow;
    private IntegerAdapter adapterLow;
    private IntegerResponseObserver<ResponseQRNS> observerLow;

    // 10 ... 99
    private RecyclerView listMiddle;
    private IntegerAdapter adapterMiddle;
    private IntegerResponseObserver<ResponseQRNS> observerMiddle;

    // 100 ... 999
    private RecyclerView listHigh;
    private IntegerAdapter adapterHigh;
    private IntegerResponseObserver<ResponseQRNS> observerHigh;

    @OnClick(R.id.buttonEvaluation)
    public void clickEvaluation() {
        double pr1 = Evaluation.evaluation(adapterLow.getList());
        double pr2 = Evaluation.evaluation(adapterMiddle.getList());
        double pr3 = Evaluation.evaluation(adapterHigh.getList());
        String msg = String.format(Locale.ENGLISH, "%8.3f %8.3f %8.3f", pr1, pr2, pr3);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);
        ButterKnife.bind(this);

        initLow();
        initMiddle();
        initHigh();

        listenerInput = new ListenerEditText(input);
    }

    private void initLow() {
        adapterLow = new IntegerAdapter(new LinkedList<>(), LayoutInflater.from(this));
        listLow = findViewById(R.id.list_low);
        listLow.setAdapter(adapterLow);
        listLow.setLayoutManager(new LinearLayoutManager(this));

        observerLow = new IntegerResponseObserver<>(response -> {
                    List<Integer> values = response.getData();
                    for (Integer v : values) {
                        adapterLow.append(v % 10);
                    }
                    barLow.setVisibility(View.GONE);
                },
                this::error
        );

        App.requestRandomInteger(COUNT_VALUE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observerLow);
    }

    private void initMiddle() {
        adapterMiddle = new IntegerAdapter(new LinkedList<>(), LayoutInflater.from(this));
        listMiddle = findViewById(R.id.list_middle);
        listMiddle.setAdapter(adapterMiddle);
        listMiddle.setLayoutManager(new LinearLayoutManager(this));

        observerMiddle = new IntegerResponseObserver<>(response -> {
                    List<Integer> values = response.getData();
                    for (Integer v : values)
                        adapterMiddle.append(10 + v % 90);
                    barMiddle.setVisibility(View.GONE);
                },
                this::error
        );

        App.requestRandomInteger(COUNT_VALUE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observerMiddle);
    }

    private void initHigh() {
        adapterHigh = new IntegerAdapter(new LinkedList<>(), LayoutInflater.from(this));

        listHigh = findViewById(R.id.list_high);
        listHigh.setAdapter(adapterHigh);
        listHigh.setLayoutManager(new LinearLayoutManager(this));

        observerHigh = new IntegerResponseObserver<>(respone -> {
                    List<Integer> values = respone.getData();
                    for (Integer v : values)
                        adapterHigh.append(100 + v % 900);
                    barHigh.setVisibility(View.GONE);
                },
                this::error
        );

        App.requestRandomInteger(COUNT_VALUE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observerHigh);
    }

    @Override
    protected void onStart() {
        super.onStart();
        listenerInput.subscribe(str -> {
            labelInput.setText("");
            String[] values = str.split(" ");
            if (values.length != 10)
                labelInput.setText("Должно быть 10 чисел");
            else {
                try {
                    List<Integer> list = new LinkedList<>();
                    for (String v : values) {
                        list.add(Integer.valueOf(v));
                    }
                    double p = Evaluation.evaluation(list);
                    Toast.makeText(this, "" + p, Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    labelInput.setText("NULL");
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        observerLow.dispose();
        observerMiddle.dispose();
        observerHigh.dispose();
        listenerInput.unsubscribe();
    }

    public void error(Throwable t) {
        App.logE(t.getMessage());
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_LONG).show();
        barLow.setVisibility(View.GONE);
        barMiddle.setVisibility(View.GONE);
        barHigh.setVisibility(View.GONE);
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, RandomActivity.class);
        activity.startActivity(intent);
    }
}
