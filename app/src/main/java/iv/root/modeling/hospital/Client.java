package iv.root.modeling.hospital;

public class Client {
    private int maxQueue;   // Максимальная очередь, при которой клиент согласен стоять
    private int id;
    private DoctorType doctor;  // Врач, к которому направляется пациент
    private boolean needPrint;

    public Client(int id, int maxQueue, DoctorType doctor) {
        this.id = id;
        this.maxQueue = maxQueue;
        this.doctor = doctor;
        this.needPrint = false;
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

    public boolean isNeedPrint() {
        return needPrint;
    }

    public void setNeedPrint(boolean needPrint) {
        this.needPrint = needPrint;
    }
}
