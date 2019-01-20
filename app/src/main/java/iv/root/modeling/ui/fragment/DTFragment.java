package iv.root.modeling.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import iv.root.modeling.R;
import iv.root.modeling.app.App;
import iv.root.modeling.modeling.Generator;
import iv.root.modeling.modeling.Machine;
import iv.root.modeling.modeling.ModelingObserver;
import iv.root.modeling.modeling.Processor;

public class DTFragment extends BaseFragment {
    private static final String NAME = "dt";

    @BindView(R.id.progressModeling)
    ProgressBar progressModeling;
    @BindView(R.id.inputCountRequest)
    EditText inputCountRequest;
    @BindView(R.id.inputDT)
    EditText inputDT;
    @BindView(R.id.inputA)
    EditText inputA;
    @BindView(R.id.inputB)
    EditText inputB;
    @BindView(R.id.inputLambda)
    EditText inputLambda;
    @BindView(R.id.inputBack)
    EditText inputBack;
    @BindView(R.id.inputPullSize)
    EditText inputPullSize;
    @BindView(R.id.viewResult)
    TextView viewResult;
    private ModelingObserver modelingObserver;
    private Machine machine;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modeling_dt, container, false);
        ButterKnife.bind(this, view);

        modelingObserver = new ModelingObserver(this::processingInt, this::stdError);

        setHasOptionsMenu(true);
        return view;
    }

    public static DTFragment newInstance() {
        return new DTFragment();
    }

    @Override
    public void onStop() {
        super.onStop();
        modelingObserver.unsubscribe();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemRunModeling:
                progressModeling.setVisibility(View.VISIBLE);
                Single.fromCallable(this::runModeling)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(modelingObserver);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private int runModeling() {
        int count = Integer.valueOf(inputCountRequest.getText().toString());
        int dt = Integer.valueOf(inputDT.getText().toString());
        int a = Integer.valueOf(inputA.getText().toString());
        int b = Integer.valueOf(inputB.getText().toString());
        double lambda = Double.valueOf(inputLambda.getText().toString());
        int back = Integer.valueOf(inputBack.getText().toString());
        int pullSize = Integer.valueOf(inputPullSize.getText().toString());

        if (!(dt*4 < lambda && a != 0 && b != 0 && a > dt && dt != 0 && count != 0 && pullSize > 0)) {
            getActivity().runOnUiThread(()->Toast.makeText(this.getActivity(), R.string.incorrectData, Toast.LENGTH_LONG).show());
        }

        machine = new Machine(Processor.getInstance(a, b), Generator.getInstance(lambda), pullSize);
        return machine.modelingDT(count, dt, back);
    }

    private void processingInt(int result) {
        App.logI("Result: " + result);
        progressModeling.setVisibility(View.GONE);

        viewResult.setText(String.format(Locale.ENGLISH, "Количество утерянных заявок: %d \n" +
                                                                "Максимальный размер памяти: %d \n"
                ,machine.getCountLostRequest(), machine.getMaxSize()));
    }

    private void stdError(Throwable t) {
        Toast.makeText(getActivity(), R.string.modelingFailed, Toast.LENGTH_LONG).show();
        App.logE(t.getMessage());
        progressModeling.setVisibility(View.GONE);
    }
}
