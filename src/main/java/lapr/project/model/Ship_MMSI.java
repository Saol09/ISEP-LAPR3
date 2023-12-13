package lapr.project.model;

/**
 * The type Ship mmsi.
 */
public class Ship_MMSI extends Ship {
    private String MMSI;

    /**
     * Instantiates a new Ship mmsi.
     *
     * @param name           the name
     * @param vesselTypeCode the vessel type code
     * @param length         the length
     * @param width          the width
     * @param draft          the draft
     * @param MMSI           the mmsi
     */
    public Ship_MMSI(String name, int vesselTypeCode, double length, double width, double draft, String MMSI) {
        super(name, vesselTypeCode, length, width, draft);
        this.MMSI = MMSI;
    }

    /**
     * Instantiates a new Ship mmsi.
     */
    public Ship_MMSI(){
        super();
    }

    @Override
    public String getCode() {
        return MMSI;
    }

    @Override
    public int compareTo(Ship o) {
        return this.MMSI.compareTo(o.getCode());
    }

    @Override
    public String toString() {
        return "\nShip:" +
                "\n\tMMSI: " + MMSI + super.toString();
    }
}
