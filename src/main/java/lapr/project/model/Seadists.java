package lapr.project.model;

public class Seadists {
    private String from_Country;
    private String from_port_id;
    private String from_port;
    private String to_Country;
    private String to_port_id;
    private String to_port;
    private double sea_distance;

    public Seadists(String from_Country, String from_port_id, String from_port, String to_Country, String to_port_id, String to_port, double sea_distance) {
        this.from_Country = from_Country;
        this.from_port_id = from_port_id;
        this.from_port = from_port;
        this.to_Country = to_Country;
        this.to_port_id = to_port_id;
        this.to_port = to_port;
        this.sea_distance = sea_distance;
    }

    public String getFrom_Country() {
        return from_Country;
    }

    public String getFrom_port_id() {
        return from_port_id;
    }

    public String getFrom_port() {
        return from_port;
    }

    public String getTo_Country() {
        return to_Country;
    }

    public String getTo_port_id() {
        return to_port_id;
    }

    public String getTo_port() {
        return to_port;
    }

    public double getSea_distance() {
        return sea_distance;
    }

    @Override
    public String toString() {
        return "Seadists{" +
                "from_Country='" + from_Country + '\'' +
                ", from_port_id='" + from_port_id + '\'' +
                ", from_port='" + from_port + '\'' +
                ", to_Country='" + to_Country + '\'' +
                ", to_port_id='" + to_port_id + '\'' +
                ", to_port='" + to_port + '\'' +
                ", sea_distance=" + sea_distance +
                '}';
    }
}
