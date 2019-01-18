package iv.root.modeling.distribution.puasson;

import com.jjoe64.graphview.series.DataPoint;

import iv.root.modeling.util.Factorial;
import iv.root.modeling.distribution.DistributionAPI;

public class PuassonDistribution implements DistributionAPI {
    private Double lambda;

    public PuassonDistribution(Double l) {
        lambda = l;
    }



    @Override
    public double F(double x) {
        long k = Math.round(x);
        double sum = 0;

        for (int i = 0; i <= k; i++) {
            sum += Math.pow(lambda, i)/Factorial.factorial(i);
        }

        return Math.exp(-lambda)*sum;
    }

    @Override
    public double f(double x) {
        long k = Math.round(x);
        return Math.pow(lambda, k) * Math.exp(-lambda) / Factorial.factorial(k);
    }

    @Override
    public DataPoint[] getPoints_F(double x0, double xn, int quality) {
        int X0 = (int)Math.round(x0);
        int Xn = (int)Math.round(xn);

        DataPoint[] points = new DataPoint[Xn-X0+1];
        int index = 0;
        for (int t = X0; t <= Xn; t++, index++)
            points[index] = new DataPoint(t, F(t));

        return points;
    }

    @Override
    public DataPoint[] getPoints_f(double x0, double xn, int quality) {
        int X0 = (int)Math.round(x0);
        int Xn = (int)Math.round(xn);

        DataPoint[] points = new DataPoint[Xn-X0+1];
        int index = 0;
        for (int t = X0; t <= Xn; t++, index++)
            points[index] = new DataPoint(t, f(t));
        return points;
    }
}
