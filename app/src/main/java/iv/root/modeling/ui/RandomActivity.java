package iv.root.modeling.ui;

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
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import iv.root.modeling.R;
import iv.root.modeling.app.App;
import iv.root.modeling.network.ListsResponseObserver;
import iv.root.modeling.network.dto.ContainerForList;
import iv.root.modeling.network.dto.ResponseQRNS;
import iv.root.modeling.util.Evaluation;
import iv.root.modeling.util.IntegerAdapter;
import iv.root.modeling.util.ListenerEditText;
import iv.root.modeling.util.RandomGenerator;

public class RandomActivity extends AppCompatActivity {
    private static final int COUNT_VALUE = 100;    // Количество запрашиваемых чисел

    @BindView(R.id.progressLoading)
    ProgressBar progressLoading;
    @BindView(R.id.input)
    EditText input;
    @BindView(R.id.labelInput)
    TextView labelInput;

    private ListenerEditText listenerInput;

    // 0 ... 9
    private RecyclerView listLow;
    private RecyclerView listLowGenerator;
    private IntegerAdapter adapterLow;
    private IntegerAdapter adapterLowGenerator;

    // 10 ... 99
    private RecyclerView listMiddle;
    private RecyclerView listMiddleGenerator;
    private IntegerAdapter adapterMiddle;
    private IntegerAdapter adapterMiddleGenerator;

    // 100 ... 999
    private RecyclerView listHigh;
    private RecyclerView listHighGenerator;
    private IntegerAdapter adapterHigh;
    private IntegerAdapter adapterHighGenerator;

    private ListsResponseObserver listsObserver;

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

        listenerInput = new ListenerEditText(input);
        listsObserver = new ListsResponseObserver(this::bindRandomLists, this::error);
    }

    private void bindRandomLists(ContainerForList container) {
        bindLow(container.listLow);
        bindMiddle(container.listMiddle);
        bindHigh(container.listHigh);
        progressLoading.setVisibility(View.GONE);
    }

    private void bindLow(List<Integer> list) {
        adapterLow = new IntegerAdapter(new LinkedList<>(), LayoutInflater.from(this));
        listLow = findViewById(R.id.list_low);
        listLow.setAdapter(adapterLow);
        listLow.setLayoutManager(new LinearLayoutManager(this));

        adapterLowGenerator = new IntegerAdapter(new LinkedList<>(), LayoutInflater.from(this));
        listLowGenerator = findViewById(R.id.list_lowGenerator);
        listLowGenerator.setAdapter(adapterLowGenerator);
        listLowGenerator.setLayoutManager(new LinearLayoutManager(this));

        RandomGenerator.rand(0);
        for (Integer value : list) {
            adapterLow.append(value % 10);
            double r = Math.abs(RandomGenerator.rand(2)*1000);
            adapterLowGenerator.append((int)Math.round(r)%10);
        }
    }

    private void bindMiddle(List<Integer> list) {
        adapterMiddle = new IntegerAdapter(new LinkedList<>(), LayoutInflater.from(this));
        listMiddle = findViewById(R.id.list_middle);
        listMiddle.setAdapter(adapterMiddle);
        listMiddle.setLayoutManager(new LinearLayoutManager(this));


        adapterMiddleGenerator = new IntegerAdapter(new LinkedList<>(), LayoutInflater.from(this));
        listMiddleGenerator = findViewById(R.id.list_middleGenerator);
        listMiddleGenerator.setAdapter(adapterMiddleGenerator);
        listMiddleGenerator.setLayoutManager(new LinearLayoutManager(this));

        RandomGenerator.rand(0);
        for (Integer value : list) {
            adapterMiddle.append(10 + value % 90);
            double r = Math.abs(RandomGenerator.rand(2)*1000);
            adapterMiddleGenerator.append((int)(10 + Math.round(r) % 90));
        }
    }

    private void bindHigh(List<Integer> list) {
        adapterHigh = new IntegerAdapter(new LinkedList<>(), LayoutInflater.from(this));
        listHigh = findViewById(R.id.list_high);
        listHigh.setAdapter(adapterHigh);
        listHigh.setLayoutManager(new LinearLayoutManager(this));

        adapterHighGenerator = new IntegerAdapter(new LinkedList<>(), LayoutInflater.from(this));
        listHighGenerator = findViewById(R.id.list_highGenerator);
        listHighGenerator.setAdapter(adapterHighGenerator);
        listHighGenerator.setLayoutManager(new LinearLayoutManager(this));

        RandomGenerator.rand(0);
        for (Integer value : list) {
            adapterHigh.append(100 + value % 900);
            double r = Math.abs(RandomGenerator.rand(2)*1000);
            adapterHighGenerator.append((int)(100 + Math.round(r) % 900));
        }
    }

    private void initAustralianRandom() {
        progressLoading.setVisibility(View.VISIBLE);
        Single<ResponseQRNS> resLow = App.requestRandomInteger(COUNT_VALUE);
        Single<ResponseQRNS> resMiddle = App.requestRandomInteger(COUNT_VALUE);
        Single<ResponseQRNS> resHigh = App.requestRandomInteger(COUNT_VALUE);

        Observable.zip(resLow.toObservable(), resMiddle.toObservable(), resHigh.toObservable(), this::initLists)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listsObserver);

    }

    private ContainerForList initLists(ResponseQRNS r1, ResponseQRNS r2, ResponseQRNS r3) {
        return new ContainerForList(r1.getData(), r2.getData(), r3.getData());
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
        initAustralianRandom();
    }

    @Override
    protected void onStop() {
        super.onStop();
        listsObserver.unsubscribe();
        listenerInput.unsubscribe();
    }

    public void error(Throwable t) {
        App.logE(t.getMessage());
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_LONG).show();
        progressLoading.setVisibility(View.GONE);
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, RandomActivity.class);
        activity.startActivity(intent);
    }
}
