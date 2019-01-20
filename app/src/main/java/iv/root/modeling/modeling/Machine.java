package iv.root.modeling.modeling;

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
    private int lostRequest;               // Сколько заявок было утеряно

    public Machine(Processor pr, Generator gen, int pullSize) {
        generator = gen;
        processor = pr;
        maxSize = 0;
        lostRequest = 0;
        pull = new Pull(pullSize);
    }

    public int getCountLostRequest() {
        return lostRequest;
    }

    public int getMaxSize() {
        return maxSize;
    }

    /**
     * timeNewRequest - Момент времени, когда появится следующая заявка. Если < 0, значит время для генерации обновляется
     * timeProcessingRequest - момент времени, когда обработается очередная заявка. Если < 0, значит время обновляется
     * Работа алгоритма продолжается если:
        * Не все заявки были сгенерированы
        * Не все заявки были обработаны (pull не пуст)
        * ОА не освободился
     * Начальные условия:
         * Время появления нового запроса отрциательное, значит генератор ничем не занят.
         * Время обработки запроса отрицательное, значит ОА ничем не занят
     * Если генератор ничем не занят и нужно ещё сгенерировать заявок, тогда задаётся время появления следующей заявки
     *
     * Если время для генерации пришло, то в pull попадает новая заявка, а время генерации новой становится отрицательной,
     * что спровоцирует новую генерацию
     *
     * Если пулл не пустой и ОА ничем не занят, то ОА принимает новую заявку и устанавливает время обработки
     *
     * Если ОА был чем-то занят, и время обработки заявки прошло, то она выбрасывается из ОА
     *
     * Цикл заканчивается, когда больше нет заявок и последняя была обработана
     * @param count - количество заявок, которое необходимо обработать
     * @param dt    - шаг по времени
     * @param back  - вероятность (в процентах), что обработанная заявка вернётся обратно в Pull
     * @return  максмальная длина pull, которая была за это время
     */
    public int modelingDT(int count, int dt, int back) {
        // Создаём список нужной длины из уникальных заявок
        List<Request> requests = Request.getPullUniqueRequests(count);
        int timeNewRequest = -1;
        int timeProcessingRequest = -1;

        for (int t = 0; !requests.isEmpty() || !pull.isEmpty() || timeProcessingRequest > 0; t += dt) {
            App.logI(String.format(Locale.ENGLISH, "time: %d; count: %d;", t, requests.size()));
            if (timeNewRequest < 0 && !requests.isEmpty()) {
                timeNewRequest = t + generator.timeForNextRequest();
                App.logI("\tВремя создания следующей заявки: " + timeNewRequest);
            }

            if (t >= timeNewRequest && timeNewRequest > 0) {
                Request r = requests.get(requests.size()-1);
                App.logI("\tГенератор произвёл заявку: " + r.getId());
                lostRequest += pull.put(requests.remove(requests.size()-1)) ? 0 : 1;
                timeNewRequest = -1;
                if (pull.getCurSize() > maxSize) {
                    maxSize = pull.getCurSize();
                    App.logI("\tМаксимальная длина обновилась: " + maxSize);
                }
            }

            if (!pull.isEmpty() && timeProcessingRequest < 0) {
                Request r = pull.get();
                processor.receive(r);
                timeProcessingRequest = t + processor.timeForProcessingRequest();
                App.logI(String.format(Locale.ENGLISH, "\tЗаявка %d ушла на обработку и будет обработана в %d", r.getId(), timeProcessingRequest));
            }

            if (t >= timeProcessingRequest && timeProcessingRequest > 0) {
                Request r = processor.release();
                App.logI("\tОкончена обработка заявки " + r.getId());
                timeProcessingRequest = -1;
                int backMove = new Random().nextInt(100);
                if (backMove < back) {
                    App.logI("\tИ она тут же отправляется на второй круг");
                    lostRequest += pull.put(r) ? 0 : 1;
                }
            }
        }

        return maxSize;
    }
}
