package lapr.project.model;


import lapr.project.utils.AVL;
import lapr.project.utils.DistancesCalculus;

import java.util.Objects;

/**
 * The type Country.
 */
public class Country {

    private String continent;
    private String alpha2;
    private String alpha3;
    private String country;
    private Double population;
    private Capital capital;
    private AVL_PORT portTree;
    private int colour;


    /**
     * vai criar uma arvore para organizar os ports pela distancia a capital para ser de menor complexidade os metodos
     * de criacao da edge mais proxima à capital
     */
    public static class AVL_PORT extends AVL<Port> {

        /**
         * Insert.
         *
         * @param port             the port
         * @param latitudeCapital  the latitude capital
         * @param longitudeCapital the longitude capital
         */
        public void insert(Port port, double latitudeCapital, double longitudeCapital) {
            root = insert(port, root, latitudeCapital, longitudeCapital);
        }


        private  Node<Port> insert(Port port, Node<Port> node, double latitudeCapital, double longitudeCapital) {
            if (node == null) return new Node<Port>(port, null, null);

            double distanciaPort = DistancesCalculus.distance(latitudeCapital, longitudeCapital, port.getLat(), port.getLon(), "K");
            double distanciaNode = DistancesCalculus.distance(latitudeCapital, longitudeCapital, node.getElement().getLat(), node.getElement().getLon(), "K");

            if (distanciaNode < distanciaPort)
                node.setRight(insert(port, node.getRight(), latitudeCapital, longitudeCapital));

            else if(distanciaNode>distanciaPort){
                node.setLeft(insert(port, node.getLeft(), latitudeCapital, longitudeCapital));
            }
            return balanceNode(node);
        }

        /**
         * Get element closest to capital port.
         *
         * @return the port
         */
        public Port getElementClosestToCapital(){
            return getElementClosestToCapital(root);
        }

        private Port getElementClosestToCapital(Node<Port> node){
            if (root == null) return null;
            if (node.getLeft() == null) return node.getElement();

            return getElementClosestToCapital(node.getLeft());
        }
    }

    /**
     * Empty Country constructor
     */
    public Country() {
    }

    /**
     * Instantiates a new Country.
     *
     * @param continent  the continent
     * @param alpha2     the alpha2-code
     * @param alpha3     the alpha3-code
     * @param country    the country name
     * @param population the population
     * @param capital    the capital
     * @param lat        the lat
     * @param lon        the lon
     */
    public Country(String continent, String alpha2, String alpha3, String country,
                   Double population, String capital, Double lat, Double lon) {
        this.continent = continent;
        this.alpha2 = alpha2;
        this.alpha3 = alpha3;
        this.country = country;
        this.population = population;
        this.capital = new Capital(capital, lat, lon, continent);    // cria a capital dos paises
        this.portTree = new AVL_PORT();

    }

    /**
     * Gets Continent.
     *
     * @return the continent
     */
    public String getContinent() {
        return continent;
    }

    /**
     * Gets Alpha2
     *
     * @return the alpha2-code
     */
    public String getAlpha2() {
        return alpha2;
    }

    /**
     * Gets Alpha3
     *
     * @return the alpha3-code
     */
    public String getAlpha3() {
        return alpha3;
    }

    /**
     * Gets Country name.
     *
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Gets Population
     *
     * @return the population
     */
    public Double getPopulation() {
        return population;
    }

    /**
     * Gets Capital
     *
     * @return the capital
     */
    public Capital getCapital() {
        return capital;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Country)) return false;
        Country country1 = (Country) o;
        return country.equals(country1.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country);
    }

    /**
     * Gets Lat.
     *
     * @return the lat
     */
    public Double getCapitalLatitude() {
        return this.capital.getLat();
    }

    /**
     * Gets Lon.
     *
     * @return the lon
     */
    public Double getCapitalLongitude() {
        return this.capital.getLon();
    }

    /**
     * Gets port tree.
     *
     * @return the port tree
     */
    public AVL_PORT getPortTree() {
        return portTree;
    }

    /**
     * Add port.
     *
     * @param port the port
     */
    public void addPort(Port port) {
         this.portTree.insert(port, this.capital.getLat(), this.capital.getLon());
    }

    /**
     * Gets colour.
     *
     * @return the colour
     */
    public int getColour() {
        return colour;
    }

    /**
     * Sets colour.
     *
     * @param colour the colour
     */
    public void setColour(int colour) {
        this.colour = colour;
    }

    @Override
    public String toString() {
        return "Country{" +
                "continent='" + continent + '\'' +
                ", alpha2='" + alpha2 + '\'' +
                ", alpha3='" + alpha3 + '\'' +
                ", country='" + country + '\'' +
                ", population=" + population +
                ", capital='" + capital + '\'' +
                '}';
    }
}
