package lapr.project.model;


/**
 * The type Ship call sign.
 */
public class Ship_CallSign extends Ship{
    private String callSign;

    /**
     * Instantiates a new Ship call sign.
     *
     * @param name           the name
     * @param vesselTypeCode the vessel type code
     * @param length         the length
     * @param width          the width
     * @param draft          the draft
     * @param callSign       the call sign
     */
    public Ship_CallSign(String name, int vesselTypeCode, double length, double width, double draft, String callSign){
        super(name, vesselTypeCode, length, width, draft);
        this.callSign = callSign;
    }

    /**
     * Instantiates a new Ship call sign.
     */
    public Ship_CallSign(){
        super();
    }
    @Override
    public String getCode() {
        return callSign;
    }

    @Override
    public int compareTo(Ship o) {
        return this.callSign.compareTo(o.getCode());
    }

    @Override
    public String toString() {
        return "\nShip:" +
                "\n\tCall Sign: " + callSign + super.toString();
    }
}
