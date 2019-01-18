package iv.root.modeling.distribution.uniform;

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

public class UniformFragment extends Fragment implements FragmentAPI {
    private static final String ARGS_X0 = "args:x0";
    private static final String ARGS_XN = "args:xn";
    private EditText inputA;
    private EditText inputB;
    private Double x0;
    private Double xn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uniform_layout, container, false);

        inputA = view.findViewById(R.id.inputA);
        inputB = view.findViewById(R.id.inputB);

        Bundle bundle = getArguments();
        if (bundle != null) {
            x0 = bundle.getDouble(ARGS_X0);
            xn = bundle.getDouble(ARGS_XN);
        }

        return view;
    }

    @Override
    public DataPoint[] getPoints_F(int quality) {
        String strA = inputA.getText().toString();
        String strB = inputB.getText().toString();
        if (!strA.isEmpty() && !strB.isEmpty()) {
            Double a = Double.valueOf(strA);
            Double b = Double.valueOf(strB);
            UniformDistribution distribution = new UniformDistribution(a,b);
            return distribution.getPoints_F(x0, xn, quality);
        }
        return new DataPoint[]{};
    }

    @Override
    public DataPoint[] getPoints_f(int quality) {
        String strA = inputA.getText().toString();
        String strB = inputB.getText().toString();
        if (!strA.isEmpty() && !strB.isEmpty()) {
            Double a = Double.valueOf(strA);
            Double b = Double.valueOf(strB);
            UniformDistribution distribution = new UniformDistribution(a,b);
            return distribution.getPoints_f(x0, xn, quality);
        }

        return new DataPoint[]{};

    }

    public static UniformFragment getInstance(Double x0, Double xn) {
        UniformFragment fragment = new UniformFragment();
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
