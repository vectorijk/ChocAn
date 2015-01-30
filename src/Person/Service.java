package Person;

/**
 * Created by Kai Jiang on 12/4/2014.
 */
public class Service {
    public int service_id;
    public String service_name;
    public double fee;

    public Service() {
        service_id = 0;
        service_name = "";
        fee = 0.0;
    }

    public Service(int sID, String s_name, double fee) {
        this.service_id = sID;
        this.service_name = s_name;
        this.fee = fee;
    }
}
