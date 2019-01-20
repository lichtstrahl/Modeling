package iv.root.modeling.modeling;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import iv.root.modeling.app.App;

/**
 * Общее представление системы
 */
public class Machine {
    private Generator generator;            // Генератор заявок (пуассоновсикй закон)
    private Processor processor;            // ОА (равномерное распределение)
    private Pull pull;                      // Накопитель заявок
    private int maxSize;                    // Максимальный размер очереди
    private int countRequest;               // Сколько заявок было обработано

    public Machine(Processor pr, Generator gen) {
        generator = gen;
        processor = pr;
        maxSize = 0;
        countRequest = 0;
        pull = new Pull();
    }

    public int getCountRequest() {
        return countRequest;
    }

    public int getMaxSize() {
        return maxSize;
    }

    /**
     *
     * @param back - вероятность (в процентах) того, что заявка вернётся обратно в очередь
     */
    public void processRequest(int back) {
        if (!pull.isEmpty()) {
            Request r = pull.get();
            App.logI("Обработана заявка: " + r.getId());
            int backMove = new Random().nextInt(100);
            if (back > backMove) {
                pull.put(r);
                App.logI(String.format(Locale.ENGLISH, "Заявка %d ушла на второй круг", r.getId()));
            }

        } else {
            App.logI("Обращение к пустой очереди");
        }
    }

    /**
     * timeNewRequest - Момент времени, когда появится следующая заявка. Если < 0, значит время для генерации обновляется
     * timeProcessingRequest - момент времени, когда обработается очередная заявка. Если < 0, значит время обновляется
     * Начальные условия:
         * Время появления нового запроса отрциательное, значит генератор ничем не занят.
         * Время обработки запроса отрицательное, значит ОА ничем не занят
     * Если генератор ничем не занят, тогда задаётся время появления следующей заявки
     * Если ОА ничем не занят, тогда задаётся время обработки следующей заявки
     *
     * Если время для генерации пришло, то в pull попадает новая заявка, а время генерации новой становится отрицательной
     *
     * @param count - количество заявок, которое необходимо обработать
     * @param dt    - шаг по времени
     * @return  максмальная длина pull, которая была за это время
     */
    public int modelingAction(int count, int dt) {
        // Создаём список нужной длины из уникальных заявок
        List<Request> requests = Request.getPullUniqueRequests(count);
        int timeNewRequest = -1;
        int timeProcessingRequest = -1;
        int maxSize = 0;

        for (int t = 0; !requests.isEmpty(); t += dt) {
            App.logI(String.format(Locale.ENGLISH, "Cur time: %d; Count: %d;", t, count));
            if (timeNewRequest < 0) timeNewRequest = t + generator.timeForNextRequest();
            if (timeProcessingRequest < 0) timeProcessingRequest = t + processor.timeForProcessingRequest();


            if (t >= timeNewRequest) {
                App.logI("Добавление новой заявки");
                pull.put(requests.remove(requests.size()-1));
                timeNewRequest = -1;
                if (pull.getCurSize() > maxSize) {
                    maxSize = pull.getCurSize();
                    App.logI("Максимальная длина обновилась: " + maxSize);
                }
            }
        }

        return maxSize;
    }
}
