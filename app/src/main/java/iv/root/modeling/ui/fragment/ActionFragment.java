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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;

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

public class ActionFragment extends BaseFragment {
    private static final String NAME = "Action";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modeling_action, container, false);
        ButterKnife.bind(this, view);

        modelingObserver = new ModelingObserver(this::processingDataPoint, this::stdError);

        graphView.getViewport().setScalableY(true);
        setHasOptionsMenu(true);
        return view;
    }

    public static ActionFragment newInstance() {
        return new ActionFragment();
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

    private DataPoint[] runModeling() {
        int count = Integer.valueOf(inputCountRequest.getText().toString());
        int a = Integer.valueOf(inputA.getText().toString());
        int b = Integer.valueOf(inputB.getText().toString());
        double lambda = Double.valueOf(inputLambda.getText().toString());
        int back = Integer.valueOf(inputBack.getText().toString());
        int pullSize = Integer.valueOf(inputPullSize.getText().toString());

        if (!(a != 0 && b != 0 && count != 0 && pullSize > 0)) {
            getActivity().runOnUiThread(()->Toast.makeText(this.getActivity(), R.string.incorrectData, Toast.LENGTH_LONG).show());
        }

        machine = new Machine(Processor.getInstance(a, b), Generator.getInstance(lambda), pullSize, count, back);
        return machine.modelingAction();
    }

    @Override
    public String getName() {
        return NAME;
    }
}
