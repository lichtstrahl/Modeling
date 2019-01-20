package iv.root.modeling.modeling;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import io.reactivex.functions.Action;
import iv.root.modeling.app.App;

/**
 * Общее представление системы
 */
public class Machine {
    private Generator generator;            // Генератор заявок (пуассоновсикй закон)
    private Processor processor;            // ОА (равномерное распределение)
    private Pull pull;                      // Накопитель заявок
    private int maxSize;                    // Максимальный размер очереди
    private int lostRequest;                // Сколько заявок было утеряно
    private List<Request> requests;         // Заранее сформированный список заявок
    private int back;                       // Вероятность (в процентах), что обработанная заявка вернётся обратно в очередь

    public Machine(Processor pr, Generator gen, int pullSize, int count, int b) {
        generator = gen;
        processor = pr;
        maxSize = 0;
        lostRequest = 0;
        pull = new Pull(pullSize);
        requests = Request.getPullUniqueRequests(count);
        back = b;
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
     * @param dt    - шаг по времени
     * @return      - количество утерянных заявок
     */
    public int modelingDT(int dt) {
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
                updateMaxSize();
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
                    updateMaxSize();
                }
            }
        }

        return maxSize;
    }


    private void updateMaxSize() {
        if (pull.getCurSize() > maxSize) {
            maxSize = pull.getCurSize();
            App.logI("\tМаксимальная длина обновилась: " + maxSize);
        }
    }

    /**
     * Изначально у нас ожидается лишь одно событие: появление заявки. Событие окончания обработки возможно только после него, поэтому
     * его не ждём (null)
     *
     * Моделирование продолжается до тех пор, пока либо не закончились запланированные заявки, либо ожидается какое-то событие в системе
     *
     * Если генерации новой заявки не ожидается (генератор простаивает) и запланированные заявки ещё не исчерпаны,
     * значит необходимо поставить на ожидание появление следующей заявки
     *
     * Если не ожидается окончание обработки заявки (ОА свободен) и в Pull-е есть заявки,
     * значит необходимо поставить на ожидание окончание обработки следующей заявки
     *
     * Если ожидается появление новой заявки, при этом не ожидается окончание обработки (ОА пустой),
     * значит автоматически срабатывает событие появления новой заявки и оно снимается с ожидания
     *
     * Если ожидается окончание обработки заявки, при этом не ожидается генерация новой,
     * значит автоматически срабатывает событие окончания обработки заявки и оно снимается с ожидания
     *
     * Если ожидаются оба события, то необходимо определить какое из них произойдёт раньше. И выполнить его.
     * Если оба события запланированы на одно время, то выполнить сразу два события. Выполняемое событие ВСЕГДА снимается с ожидания.
     *
     * @return      - количество утерянных заявок
     */
    public int modelingAction() {
        int time = 0;
        Event newRequest = new Event();
        scheduledNewRequest(time, newRequest);
        Event processRequest = new Event();


        while (!requests.isEmpty() || newRequest.isScheduled() || processRequest.isScheduled()) {
            if (!newRequest.isScheduled() && !requests.isEmpty()) {
                scheduledNewRequest(time, newRequest);
            }

            if (!processRequest.isScheduled() && !pull.isEmpty()) {
                Request r = pull.get();
                processor.receive(r);
                processRequest.scheduled(time + processor.timeForProcessingRequest(), processRequest::actionProcessRequest);
                App.logI(String.format(Locale.ENGLISH, "Заявка %d поступила на обработку и обработается в %d", r.getId(), processRequest.getTime()));
            }

            if (newRequest.isScheduled() && !processRequest.isScheduled()) {
                newRequest.action();
                time = newRequest.getTime();
            }

            if (processRequest.isScheduled() && !newRequest.isScheduled()) {
                processRequest.action();
                time = processRequest.getTime();
            }

            if (newRequest.isScheduled() && processRequest.isScheduled()) {
                if (newRequest.getTime() < processRequest.getTime()) {
                    newRequest.action();
                    time = newRequest.getTime();
                }

                if (newRequest.getTime() == processRequest.getTime()) {
                    newRequest.action();
                    processRequest.action();
                    time = newRequest.getTime();
                }

                if (newRequest.getTime() > processRequest.getTime()) {
                    processRequest.action();
                    time = processRequest.getTime();
                }
            }
        }
        return 0;
    }

    private void scheduledNewRequest(int time, Event newRequest) {
        newRequest.scheduled(time + generator.timeForNextRequest(), newRequest::actionNewRequest);
        App.logI(String.format(Locale.ENGLISH,"Следующая заявка появится в %d",newRequest.getTime()));
    }

    class Event {
        private int time;
        private boolean scheduled;
        private Action action;


        public int getTime() {
            return time;
        }

        public void scheduled(int time, Action a) {
            this.time = time;
            action = a;
            scheduled = true;
        }

        public void action() {
            try {
                action.run();
            } catch (Exception e) {
                App.logE("Event: " + e.getMessage());
            }
            scheduled = false;
        }

        public boolean isScheduled() {
            return scheduled;
        }

        void actionNewRequest() {
            Request r = requests.remove(requests.size()-1);
            lostRequest += pull.put(r) ? 0 : 1;
            updateMaxSize();
            App.logI(String.format(Locale.ENGLISH, "Сгенерирована заявка %d (time %d)", r.getId(), getTime()));
        }

        void actionProcessRequest() {
            Request r = processor.release();
            App.logI(String.format(Locale.ENGLISH, "Заявка %d обработана (time %d)", r.getId(), getTime()));

            int moveBack = new Random().nextInt(100);
            if (moveBack < back) {
                App.logI(String.format(Locale.ENGLISH, "Заявка %d отправлена на второй круг", r.getId()));
                lostRequest += pull.put(r) ? 0 : 1;
                updateMaxSize();
            }
        }
    }
}
