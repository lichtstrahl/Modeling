package iv.root.modeling.ui.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;

import java.util.Locale;

import butterknife.BindView;
import iv.root.modeling.R;
import iv.root.modeling.app.App;
import iv.root.modeling.modeling.Machine;
import iv.root.modeling.modeling.ModelingObserver;
import iv.root.modeling.util.Named;

public abstract class BaseFragment extends Fragment implements Named {
    @BindView(R.id.progressModeling)
    ProgressBar progressModeling;
    @BindView(R.id.inputCountRequest)
    EditText inputCountRequest;
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
    @BindView(R.id.graphView)
    GraphView graphView;

    protected ModelingObserver modelingObserver;
    protected Machine machine;

    @Override
    public void onStop() {
        super.onStop();
        modelingObserver.unsubscribe();
    }

    protected void processingDataPoint(DataPoint[] points) {
        progressModeling.setVisibility(View.GONE);

        viewResult.setText(String.format(Locale.ENGLISH, "Количество утерянных заявок: %d \n" +
                        "Максимальный размер памяти: %d \n"
                ,machine.getCountLostRequest(), machine.getMaxSize()));
        App.logI(""+points.length);
    }

    protected void stdError(Throwable t) {
        Toast.makeText(getActivity(), R.string.modelingFailed, Toast.LENGTH_LONG).show();
        App.logE(t.getMessage());
        progressModeling.setVisibility(View.GONE);
    }
}
