package iv.root.modeling.distribution.erlang;

import iv.root.modeling.distribution.util.Factorial;
import iv.root.modeling.distribution.DistributionAPI;

public class ErlangDistribution implements DistributionAPI {
    private Integer K;
    private Double lambda;

    ErlangDistribution(Integer k, Double l) {
        K = k;
        lambda = l;
    }

    @Override
    public double F(double x) {
        double sum = 0.0;
        for (int i = 0; i < K; i++) {
            sum += Math.pow(lambda * x, i) / Factorial.factorial(i);
        }
        return 1 - Math.exp(-lambda * x) * sum;
    }

    @Override
    public double f(double x) {
        return Math.pow(lambda, K) * Math.pow(x, K-1) * Math.exp(-lambda*x) / Factorial.factorial(K-1);
    }
}
