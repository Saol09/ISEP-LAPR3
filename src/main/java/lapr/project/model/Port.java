package lapr.project.model;

import java.util.Objects;
import lapr.project.utils.portsCapitalsGraph.IPortsAndCapitals;

/**
 * The type Port.
 */
public class Port implements IPortsAndCapitals, Comparable<Port>{

    private String continent;
    private String country;
    private String code;
    private String port;
    private Double lat;
    private Double lon;

    /**
     * Empty Port constructor
     */
    public Port(){
    }

    /**
     * Instantiates a new Port.
     *
     * @param continent the continent
     * @param country   the country
     * @param code      the code
     * @param port      the port
     * @param lat       the lat
     * @param lon       the lon
     */
    public Port(String continent, String country,
                String code, String port, Double lat, Double lon){
        this.continent = continent;
        this.country = country;
        this.code = code;
        this.port = port;
        this.lat = lat;
        this.lon = lon;
    }

    /**
     * Gets continent.
     *
     * @return the continent
     */
    public String getContinent(){ return continent; }

    @Override
    public String getName() {
        return this.port;
    }

    /**
     * Gets country.
     *
     * @return the country
     */
    public String getCountry(){ return country; }

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode(){ return code; }

    /**
     * Gets Port.
     *
     * @return the port
     */
    public String getPort(){ return port; }

    /**
     * Gets Lat.
     *
     * @return the lat
     */
    public Double getLat(){ return lat; }

    /**
     * Gets Lon.
     *
     * @return the lon
     */
    public Double getLon(){ return lon; }

    @Override
    public String toString() {
        return "\nPort" +
                "\n\tcontinent='" + continent + '\'' +
                ", \n\tcountry='" + country + '\'' +
                ", \n\tcode='" + code + '\'' +
                ", \n\tport='" + port + '\'' +
                ", \n\tlat=" + lat +
                ", \n\tlon=" + lon ;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.code);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Port other = (Port) obj;
        if (!Objects.equals(this.code, other.code)) {
            return false;
        }
        return true;
    }

    
    
    @Override
    public int compareTo(Port o) {
        return this.code.compareTo(o.getCode());
    }

}
