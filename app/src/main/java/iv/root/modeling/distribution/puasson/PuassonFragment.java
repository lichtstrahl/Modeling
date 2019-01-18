package iv.root.modeling.distribution.puasson;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jjoe64.graphview.series.DataPoint;

import iv.root.modeling.R;
import iv.root.modeling.distribution.FragmentAPI;

public class PuassonFragment extends Fragment implements FragmentAPI  {
    private static final String ARGS_X0 = "args:x0";
    private static final String ARGS_XN = "args:xn";
    private EditText inputLambda;
    private Double x0;
    private Double xn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_puasson_layout, container, false);

        inputLambda = view.findViewById(R.id.inputLambda);

        Bundle bundle = getArguments();
        if (bundle != null) {
            x0 = bundle.getDouble(ARGS_X0);
            xn = bundle.getDouble(ARGS_XN);
        }

        return view;
    }

    @Override
    public DataPoint[] getPoints_F(int quality) {
        String strLambda = inputLambda.getText().toString();
        if (!strLambda.isEmpty()) {
            Double lambda = Double.valueOf(strLambda);
            PuassonDistribution distribution = new PuassonDistribution(lambda);
            return distribution.getPoints_F(x0, xn, quality);
        }
        return new DataPoint[0];
    }

    @Override
    public DataPoint[] getPoints_f(int quality) {
        String strLambda = inputLambda.getText().toString();
        if (!strLambda.isEmpty()) {
            Double lambda = Double.valueOf(strLambda);
            PuassonDistribution distribution = new PuassonDistribution(lambda);
            return distribution.getPoints_f(x0, xn, quality);
        }
        return new DataPoint[0];
    }

    public static PuassonFragment getInstance(Double x0, Double xn) {
        PuassonFragment fragment = new PuassonFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble(ARGS_X0, x0);
        bundle.putDouble(ARGS_XN, xn);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void updateX0(Double newX0) {
        x0 = newX0;
    }

    @Override
    public void updateXn(Double newXn) {
        xn = newXn;
    }
}
