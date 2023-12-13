package lapr.project.model;

/**
 * The type Ship imo.
 */
public class Ship_IMO extends Ship {
    private String IMO;

    /**
     * Instantiates a new Ship imo.
     *
     * @param name           the name
     * @param vesselTypeCode the vessel type code
     * @param length         the length
     * @param width          the width
     * @param draft          the draft
     * @param IMO            the imo
     */
    public Ship_IMO(String name, int vesselTypeCode, double length, double width, double draft, String IMO){
        super(name, vesselTypeCode, length, width, draft);
        this.IMO = IMO;
    }

    /**
     * Instantiates a new Ship imo.
     */
    public Ship_IMO(){
        super();
    }

    @Override
    public String getCode() {
        return IMO;
    }

    @Override
    public int compareTo(Ship o) {
        return this.IMO.compareTo(o.getCode());
    }

    @Override
    public String toString() {
        return "\nShip:" +
                "\n\tIMO: " + IMO + super.toString();
    }
}
