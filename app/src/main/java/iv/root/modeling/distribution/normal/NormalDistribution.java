package iv.root.modeling.distribution.normal;

import org.apache.commons.math3.special.Erf;

import iv.root.modeling.distribution.DistributionAPI;

public class NormalDistribution implements DistributionAPI {
    private Double mu;
    private Double sigma;

    NormalDistribution(Double m, Double s) {
        mu = m;
        sigma = s;
    }

    @Override
    public double F(double x) {
        return (1 + Erf.erf((x-mu)/(sigma* Math.sqrt(2))))/2;
    }

    @Override
    public double f(double x) {
        return 1.0/(sigma*Math.sqrt(2*Math.PI)) * Math.exp(-Math.pow(x-mu,2)/(2*sigma*sigma));
    }

}
