package iv.root.modeling.util;

import java.util.LinkedList;
import java.util.List;
import static java.lang.Math.*;

/**
 * Проверка статистических гипотез, в данном случае о наличии тенденции в последовательности чисел
 * осуществляется с помощью квантилей распределения Стьюдента. В данном примере ква
 */

public class Evaluation {
    // Квантили распределения Стьюдента
    private static final double[] T_1000 = new double[] {
            Double.MIN_VALUE,   // 0%
            0.1257,     // 10%
            0.2534,     // 20%
            0.3854,     // 30%
            0.5246,     // 40%
            0.6747,     // 50%
            0.8420,     // 60%
            1.0370,     // 70%
            1.2824,     // 80%
            1.6464,     // 90%
    };
    private static final double[] T_10 = new double[] {
            Double.MIN_VALUE,
            0.1289,
            0.2602,
            0.3966, // 30%
            0.5415,
            0.6998, // 50%
            0.8791,
            1.0931, // 70%
            1.3722,
            1.8125

    };

    private static final double STUDENT_10_0975 = 2.228;
    private static final double STUDENT_1000_0975 = 2.3301;
    // 15,987	13,442	11,781	10,473	9,3418	8,2955	7,2672	6,1791	4,8652

    private Evaluation() { }
    /**
     * Критерий Фостера-Стюарта
     * @param values - последовательность чисел
     * @return - вероятность того, что в данной последовательности есть тренд
     */
    public static double evaluation(List<Integer> values) {
        int n = values.size(); // Число степеней свободы (для Квантилей Стьюдента)
        if (n != 10 && n != 1000) throw new IllegalArgumentException("Некорректная длина массива: " + n);
        List<Integer> u = calcuteU(values);
        List<Integer> l = calcuteL(values);

        int s = calcuteS(u, l);         // Обнаружение тенденций в дисперсии
        int d = calcuteD(u, l);         // Обнаружение тенденции в средней

        double f = (n > 50)
                ? Math.sqrt(2*Math.log(n) - 0.8456) // Для 1000
                : 1.288;                            // Для 10
        double k = (n > 50)
                ? Math.sqrt(2*Math.log(n) - 3.4253) // Для 1000
                : 1.964;                            // Для 10

        double t = Math.abs(d/f);
        double t_ = Math.abs((s - f*f) / (k));

        // Берём alpha = 0.95 (доверительная вероятность)
        // Значит квантиль Стьюдента будет 1+0.95/2 = 0.975
        // С числом степеней свободы: n
        // Квантиль со степенями свободы либо 10 либо 1000
        double T = (n == 10) ? STUDENT_10_0975 : STUDENT_1000_0975;

        if (t > T) {  // Тренд найден хотя бы в одной из виличин
            return 1.0;
        } else {                // В обеих величинах отсутствует тренд
            return t/T;
        }
    }

    /** Проверка на равномерное распределение для последовательности Xi=[0..9]
     *
     * @param values - последовательность чисел
     * @return вероятность того, что данная последовательность распределена равномерно/дискретно
     */
    public static double evaluationR(List<Integer> values) {
        int n = values.size();
        // Необходимо составить вариацонный ряд
        List<Communication> var = new LinkedList<>();
        for (Integer v : values) {
            int index;
            for (index = 0; index < var.size(); index++) {
                if (var.get(index).x == v) break;
            }

            if (index == var.size()) {
                var.add(new Communication(v, 1));
            } else {
                var.get(index).inc();
            }
        }

        double M_ = n*1.0 / var.size();

        // Xнабл
        double sum = 0.0;
        for (int i = 0; i < var.size(); i++) {
            sum += pow((var.get(i).m - M_), 2) / M_;
        }

        double pr = 0.0;
        // Число степеней свободы
        int k = var.size()-1;
        if (k == 0)
            return 1.0;

        for (int i = 9; i > 0; i--) {
            double Xi = Xi2(k, i*0.1);
            if (sum < Xi) {
                pr = 1-i*0.1;
            }
        }

        return pr;
    }

    // 0.1 <= alpha <= 0.9 - значимость
    private static double Xi2(int n, double alpha) {
        double d = alpha < 0.5 ? -2.0637*pow(log(1/alpha) - 0.16, 0.4274) + 1.5774 : 2.0637*pow(log(1/(1-alpha)) - 0.16, 0.4274) - 1.5774;
        double A = d*sqrt(2);
        double B = 2/3 * (d*d-1);
        double C = d* (d*d-7)/(9*sqrt(2));
        double D = -(6*pow(d,4) + 15*d*d - 32)/(405);
        double E = d * (9*pow(d,4) + 256*d*d - 433)/(4860*sqrt(2));

        return n + A*sqrt(n) + B + C/sqrt(n) + D/n + E/(n*sqrt(n));
    }

    static class Communication {
        private int x;
        private int m;

        public Communication(int x, int m) {
            this.x = x;
            this.m = m;
        }

        public void inc() {
            m++;
        }
    }

    /**
     * 1 если больше всех предыдущих, 0 иначе
     * @param array
     * @return
     */
    private static List<Integer> calcuteU(List<Integer> array) {
        int curMax = array.get(0);
        List<Integer> result = new LinkedList<>();
        result.add(0);

        for (int i = 1; i < array.size(); i++) {
            if (array.get(i) > curMax) {
                curMax = array.get(i);
                result.add(1);
            } else {
                result.add(0);
            }
        }

        return result;
    }

    /**
     * 1 если меньше всех предыдущих, 0 иначе
     * @param array
     * @return
     */
    private static List<Integer> calcuteL(List<Integer> array) {
        int curMin = array.get(0);
        List<Integer> result = new LinkedList<>();
        result.add(0);

        for (int i = 1; i < array.size(); i++) {
            if (array.get(i) < curMin) {
                curMin = array.get(i);
                result.add(1);
            } else {
                result.add(0);
            }
        }

        return result;
    }

    private static int calcuteS(List<Integer> u, List<Integer> l) {
        int n = u.size();
        int sum = 0;
        for (int i = 1; i < n; i++) {
            sum += (u.get(i) + l.get(i));
        }
        return sum;
    }

    private static int calcuteD(List<Integer> u, List<Integer> l) {
        int n = u.size();
        int sum = 0;
        for (int i = 1; i < n; i++) {
            sum += (u.get(i) - l.get(i));
        }
        return sum;
    }
}
