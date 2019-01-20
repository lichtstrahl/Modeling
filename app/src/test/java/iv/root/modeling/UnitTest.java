package iv.root.modeling;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import iv.root.modeling.modeling.PoissonRandom;
import iv.root.modeling.util.Evaluation;
import iv.root.modeling.util.RandomGenerator;

import static org.junit.Assert.assertTrue;

public class UnitTest {
    @Test
    public void test1() {
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < 1000; i++)
            list.add(i);

        double pr = Evaluation.evaluation(list);
        pr = Evaluation.evaluationR(list);

        assertTrue(pr > 0.5);

        list.clear();
        for (int i = 0; i < 1000; i++)
            list.add(i %500);

        pr = Evaluation.evaluation(list);
        pr = Evaluation.evaluationR(list);

        assertTrue(pr > 0.5);

        list.clear();
        for (int i = 0; i < 10; i++)
            list.add(0);
        pr = Evaluation.evaluation(list);
        pr = Evaluation.evaluationR(list);

        list.clear();
        for (int i = 0; i < 500; i++) {
            list.add(1);
            list.add(0);
        }
        pr = Evaluation.evaluation(list);
        pr = Evaluation.evaluationR(list);
    }

    @Test
    public void testPoissonRandom() {
        PoissonRandom random = new PoissonRandom(20);
        int min = random.generate();
        int max = min;

        for (int i = 1; i < 10; i++) {
            int r = random.generate();
            if (r > max) max = r;
            if (r < min) min = r;
        }

        System.out.println("min: " + min + " max: " + max);
    }

    @Test
    public void testLow() {
        List<Integer> values = new LinkedList<>();
        RandomGenerator.rand(0);
        for (int i = 0; i < 1000; i++) {
            values.add(Math.abs(new Random().nextInt(10)));
        }
        Assert.assertTrue(Evaluation.evaluation(values) > 0.9);
    }
}