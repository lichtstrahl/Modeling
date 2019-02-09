package iv.root.modeling.center;

public class Model {
    private Client client;
    private final Operator[] operators;
    private final ProcessingSystem[] processingSystems = {  // Компьютеры
            new ProcessingSystem(15, 15),
            new ProcessingSystem(30, 30)
    };
    private double timeStep;
    private double currentTime;
    private int countRequest;       // Количество обработанных заявок
    private int countMissRequest;   // Количество утерянных заявок

    public Model(double dt) {
        timeStep = dt;
        client = new Client(8, 12);
        operators = new Operator[] {
                new Operator(15, 25, processingSystems[0]),
                new Operator(20, 60, processingSystems[1]),
                new Operator(30, 50, processingSystems[0]),
        };
    }

    /**
     * Активна ли система. Однако могут быть ещё и заявки в очереди у компьютеров
     * @return - ture, если хотя бы один из операторов или компьютеров продолжает работать
     */
    public boolean isActive() {
        for (Operator o : operators) {
            if (o.isActive()) {
                return true;
            }
        }

        for (ProcessingSystem system : processingSystems) {
            if (system.isActive()) {
                return true;
            }
        }

        return false;
    }

    public void restart() {
        currentTime = 0;
    }

    public void step() {
        currentTime += timeStep;

        if (client.moveOn(timeStep)) {
            // Выбор свободных операторов
            int bestFreeOperator = 0;
            while (bestFreeOperator < operators.length) {
                if (!operators[bestFreeOperator].isActive()) {
                    break;
                } else {
                    bestFreeOperator++;
                }
            }

            // Если все операторы заняты
            if (bestFreeOperator == operators.length) {
                countMissRequest++;
            } else {
                operators[bestFreeOperator].startService();
            }
        }

        for (Operator o : operators) {
            o.continueService(timeStep);
        }

        for (ProcessingSystem system : processingSystems) {
            if (system.continueService(timeStep)) {
                countRequest++;
            }
        }
    }

    public int getCountRequest() {
        return countRequest;
    }

    public int getCountMissRequest() {
        return countMissRequest;
    }

    public double getModelingTime() {
        return currentTime;
    }
}
