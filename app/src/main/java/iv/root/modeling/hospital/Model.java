package iv.root.modeling.hospital;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Locale;

import iv.root.modeling.app.App;

public class Model {
    private static final int time = 720;    // 740 - минут, рабочий день
    private static final int dt = 1;        // 1 Минута - шаг по времени
    private ClientGenerator clientGenerator;
    private RegisterOffice office;
    private Doctor[] doctors;

    public Model(int clientMin, int clientMax, int regMin, int regMax, int docMin, int docMax, int printTime, int printP) {
        clientGenerator = new ClientGenerator(clientMin, clientMax);
        office = new RegisterOffice(regMin, regMax, printTime);
        doctors = new Doctor[] {
                new Doctor(DoctorType.DENTIST, docMin, docMax, printP),
                new Doctor(DoctorType.OCULIST, docMin, docMax, printP),
                new Doctor(DoctorType.SURGEON, docMin, docMax, printP),
                new Doctor(DoctorType.THERAPIST, docMin, docMax, printP)
        };
    }

    public void modeling() {
        int currentTime = 0;

        while (currentTime <= time) {
            // Поступление новых клиентов в регистратуру
            Client newClient = clientGenerator.step(dt);
            if (newClient != null) {
                App.logI(String.format(Locale.ENGLISH, "Клиент %d встал в очередь", newClient.getId()));
                office.putClient(newClient);
            }

            // Работа регистратуры.
            List<Client> clients = office.step(dt);
            // Распределение зарегестрированных клиентов по врачам
            if (!clients.isEmpty()) {
                for (Client c : clients) {
                    if (c.isNeedPrint()) {
                        App.logI(String.format(Locale.ENGLISH, "Клиент %d проставил печать и идёт домой", c.getId()));
                    } else {
                        Doctor doc = selectBestDoctor(c.getDoctor());
                        doc.putClient(c);
                    }
                }
            }


            for (Doctor d : doctors) {
                Client client = d.step(dt);
                if (client != null) {
                    if (client.isNeedPrint()) {
                        office.putPrintClient(client);
                        App.logI(String.format(Locale.ENGLISH, "Клиент %d ушел за печатью", client.getId()));
                    } else {
                        App.logI(String.format(Locale.ENGLISH, "Клиент %d прошёл врача", client.getId()));
                    }
                }
            }

            currentTime += dt;
        }
    }

    /**
     * Подразумевается, что если врача нет, значит пациент вообще бы не пришёл сегодня
     * @param type  - специализация врача
     * @return - найденный врач с наименьшей очередью
     */
    @NonNull
    private Doctor selectBestDoctor(DoctorType type) {
        Doctor result = null;
        for (Doctor doctor : doctors) {
            if (doctor.getType() == type) {
                if (result == null) {
                    result = doctor;
                } else if (result.getClients() > doctor.getClients()) {
                    result = doctor;
                }
            }
        }

        return result;
    }
}
