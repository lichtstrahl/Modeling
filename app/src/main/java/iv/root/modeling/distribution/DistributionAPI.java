package iv.root.modeling.distribution;

import com.jjoe64.graphview.series.DataPoint;

public interface DistributionAPI {
    /**
     * Функция распределения
     * @param x
     * @return
     */
    double F(double x);

    /**
     * Плотность распределения
     * @param x
     * @return
     */
    double f(double x);

    default DataPoint[] getPoints_F(double x0, double xn, int quality) {
        DataPoint[] line = new DataPoint[quality];

        double step = (xn-x0)/quality;
        for (int i = 0; i < quality; i++) {
            double t = x0 + i*step;
            line[i] = new DataPoint(t, F(t));
        }

        // Заменяем последнюю
        line[quality-1] = new DataPoint(xn, F(xn));

        return line;
    }

    default DataPoint[] getPoints_f(double x0, double xn, int quality) {
        DataPoint[] line = new DataPoint[quality];

        double step = (xn-x0)/quality;
        for (int i = 0; i < quality; i++) {
            double t = x0 + i*step;
            line[i] = new DataPoint(t, f(t));
        }

        // Заменяем последнюю
        line[quality-1] = new DataPoint(xn, f(xn));

        return line;
    }
}
