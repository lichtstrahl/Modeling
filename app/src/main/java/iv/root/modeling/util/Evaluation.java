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


        double pr1 = (t>T) ? 1.0 : t/T;
        double pr2 = (t_ >T ) ? 1.0 : t/T;

        return (pr1*3+pr2)/4;
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
