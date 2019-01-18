package iv.root.modeling.distribution;

import com.jjoe64.graphview.series.DataPoint;

public interface FragmentAPI {
    DataPoint[] getPoints_F(int quality);

    DataPoint[] getPoints_f(int quality);

    void updateX0(Double newX0);

    void updateXn(Double newXn);
}
