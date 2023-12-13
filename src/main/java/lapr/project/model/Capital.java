package lapr.project.model;

import lapr.project.utils.portsCapitalsGraph.IPortsAndCapitals;

import java.util.Objects;

/**
 * The type Capital.
 */
public class Capital implements IPortsAndCapitals {

    private String name;
    private double latitude;
    private double longitude;
    private String continente;

    /**
     * Instantiates a new Capital.
     *
     * @param name       the name
     * @param latitude   the latitude
     * @param longitude  the longitude
     * @param continente the continente
     */
    public Capital(String name, double latitude, double longitude,String continente) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.continente = continente;
    }


    /**
     * Gets name.
     *
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    public Double getLat() {
        return latitude;
    }

    public Double getLon() {
        return longitude;
    }

    @Override
    public String getContinent() {
        return this.continente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Capital)) return false;
        Capital capital = (Capital) o;
        return Objects.equals(name, capital.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, latitude, longitude);
    }

    @Override
    public String toString() {
        return "\nCapital" +
                "\n\tname='" + name + '\'' +
                ", \n\tlatitude=" + latitude +
                ", \n\tlongitude=" + longitude ;
    }
}
