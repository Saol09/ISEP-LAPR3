package lapr.project.model;

import java.time.LocalDateTime;

/**
 * The type Ship dynamic data.
 */
public class ShipDynamicData implements Comparable<ShipDynamicData> {

    private LocalDateTime baseDateTime;
    private double lat;
    private double lon;
    private double sog;
    private double cog;
    private double heading;            //rumo do navio (em graus: [0; 359], 511 indica 'não disponível')
    private String cargo;                 //código do navio em reboque
    private String transcieverClass;   //classe to transciever utilizado no envio dos dados

    /**
     * Instantiates a new Ship dynamic data.
     *
     * @param baseDateTime     the base date time
     * @param lat              the lat
     * @param lon              the lon
     * @param sog              the sog
     * @param cog              the cog
     * @param heading          the heading
     * @param cargo            the cargo
     * @param transcieverClass the transciever class
     */
    public ShipDynamicData(LocalDateTime baseDateTime, double lat, double lon, double sog, double cog, double heading, String cargo, String transcieverClass) {
        this.baseDateTime = baseDateTime;
        this.lat = lat;
        this.lon = lon;
        this.sog = sog;
        this.cog = cog;
        this.heading = heading;
        this.cargo = cargo;
        this.transcieverClass = transcieverClass;
    }

    /**
     * Gets cog.
     *
     * @return the cog
     */
    public double getCog() {
        return cog;
    }

    /**
     * Gets heading.
     *
     * @return the heading
     */
    public double getHeading() {
        return heading;
    }

    /**
     * Gets lat.
     *
     * @return the lat
     */
    public double getLat() {
        return lat;
    }

    /**
     * Gets lon.
     *
     * @return the lon
     */
    public double getLon() {
        return lon;
    }

    /**
     * Gets sog.
     *
     * @return the sog
     */
    public double getSog() {
        return sog;
    }

    /**
     * Gets base date time.
     *
     * @return the base date time
     */
    public LocalDateTime getBaseDateTime() {
        return baseDateTime;
    }

    /**
     * Gets cargo.
     *
     * @return the cargo
     */
    public String getCargo() {
        return cargo;
    }

    /**
     * Gets transciever class.
     *
     * @return the transciever class
     */
    public String getTranscieverClass() {
        return transcieverClass;
    }

    /*@Override
    public String toString() {
        return "\n\tShipDynamicData" +
                "\n\t\tbaseDateTime=" + baseDateTime +
                ", \n\t\tlat=" + lat +
                ", \n\t\tlon=" + lon +
                ", \n\t\tsog=" + sog +
                ", \n\t\tcog=" + cog +
                ", \n\t\theading=" + heading +
                ", \n\t\tcargo='" + cargo + '\'' +
                ", \n\t\ttranscieverClass='" + transcieverClass + '\'' ;
    }*/

    @Override
    public int compareTo(ShipDynamicData o) {
        if (this.baseDateTime.compareTo(o.baseDateTime) > 0) return 1;

        if (this.baseDateTime.compareTo(o.baseDateTime) < 0) return -1;

        return 0;
    }
}
