package iv.root.modeling.distribution.uniform;

import iv.root.modeling.distribution.DistributionAPI;

public class UniformDistribution implements DistributionAPI {
    private double a, b;
    UniformDistribution(double a, double b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public double F(double x) {
        if (x >= b)
            return 1.0;
        if (x <= a)
            return 0.0;
        return (x-a)/(b-a);
    }

    @Override
    public double f(double x) {
        if (x < a || x > b)
            return 0;
        return 1.0/(b-a);
    }
}
