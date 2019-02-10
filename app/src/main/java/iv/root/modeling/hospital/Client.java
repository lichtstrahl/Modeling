package iv.root.modeling.hospital;

public class Client {
    private int maxQueue;   // Максимальная очередь, при которой клиент согласен стоять
    private int id;
    private DoctorType doctor;  // Врач, к которому направляется пациент

    public Client(int id, int maxQueue, DoctorType doctor) {
        this.id = id;
        this.maxQueue = maxQueue;
        this.doctor = doctor;
    }

    public int getMaxQueue() {
        return maxQueue;
    }

    public int getId() {
        return id;
    }

    public DoctorType getDoctor() {
        return doctor;
    }
}
