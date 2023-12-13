package lapr.project.utils;


import lapr.project.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * The type Supertree.
 *
 * @param <E> the type parameter
 */
public class SUPERTREE<E extends Ship> extends AVL<Ship> {
    private final E element;
    private final String path;
    private final double limite = 5.00;

    /**
     * Instantiates a new Supertree.
     *
     * @param e the e
     */
    public SUPERTREE(E e) {
        element = e;
        path = "src/main/java/lapr/project/files/bships.csv";
    }


    /**
     * Instantiates a new Supertree.
     *
     * @param e    the e
     * @param path the path
     */
    public SUPERTREE(E e, String path) {
        element = e;
        this.path = path;
    }

    /**
     * Create tree.
     *
     * @throws FileNotFoundException the file not found exception
     */
    public void createTree() throws FileNotFoundException {

        if (this.element instanceof Ship_IMO) {
            createIMOTree();
        }
        if (element instanceof Ship_MMSI) {
            createMMSITree();
        }
        if (element instanceof Ship_CallSign) {
            createCallSignTree();
        }
    }

    private void createIMOTree() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(path));
        Ship_IMO ship_imo = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        sc.nextLine();
        while (sc.hasNextLine()) {
            String[] itens = sc.nextLine().split(",");
            if (ship_imo == null) {
                ship_imo = new Ship_IMO(itens[7], Integer.parseInt(itens[10]),
                        Double.parseDouble(itens[11]), Double.parseDouble(itens[12]),
                        Double.parseDouble(itens[13]), itens[8]);
            }
            if (!ship_imo.getCode().equals(itens[8])) {
                insert(ship_imo);
                ship_imo = new Ship_IMO(itens[7], Integer.parseInt(itens[10]),
                        Double.parseDouble(itens[11]), Double.parseDouble(itens[12]),
                        Double.parseDouble(itens[13]), itens[8]);
            }

            LocalDateTime dateTime = LocalDateTime.parse(itens[1], formatter);

            if (verifyDynamicData(itens)) {
                ship_imo.addToDynamicDataTree(new ShipDynamicData
                        (dateTime, Double.parseDouble(itens[2]), Double.parseDouble(itens[3])
                                , Double.parseDouble(itens[4])
                                , Double.parseDouble(itens[5]),
                                Double.parseDouble(itens[6]),
                                itens[14],
                                itens[15]));
            }

        }
        if (ship_imo != null)  //para o ultimo ship
            insert(ship_imo);
    }

    private void createMMSITree() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(path));

        Ship_MMSI ship_mmsi = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        sc.nextLine();

        while (sc.hasNextLine()) {
            String[] itens = sc.nextLine().split(",");
            if (ship_mmsi == null) {
                ship_mmsi = new Ship_MMSI(itens[7], Integer.parseInt(itens[10]),
                        Double.parseDouble(itens[11]), Double.parseDouble(itens[12]),
                        Double.parseDouble(itens[13]), itens[0]);
            }
            if (!ship_mmsi.getCode().equals(itens[0])) {
                insert(ship_mmsi);
                ship_mmsi = new Ship_MMSI(itens[7], Integer.parseInt(itens[10]),
                        Double.parseDouble(itens[11]), Double.parseDouble(itens[12]),
                        Double.parseDouble(itens[13]), itens[0]);
            }
            LocalDateTime dateTime = LocalDateTime.parse(itens[1], formatter);

            if (verifyDynamicData(itens)) {
                ship_mmsi.addToDynamicDataTree(new ShipDynamicData
                        (dateTime, Double.parseDouble(itens[2]), Double.parseDouble(itens[3])
                                , Double.parseDouble(itens[4])
                                , Double.parseDouble(itens[5]),
                                Double.parseDouble(itens[6]),
                                itens[14],
                                itens[15]));
            }

        }

        if (ship_mmsi != null)
            insert(ship_mmsi);
    }

    private void createCallSignTree() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(path));

        Ship_CallSign ship_callSign = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        sc.nextLine();

        while (sc.hasNextLine()) {
            String[] itens = sc.nextLine().split(",");
            if (ship_callSign == null) {
                ship_callSign = new Ship_CallSign(itens[7], Integer.parseInt(itens[10]),
                        Double.parseDouble(itens[11]), Double.parseDouble(itens[12]),
                        Double.parseDouble(itens[13]), itens[9]);
            }
            if (!ship_callSign.getCode().equals(itens[9])) {
                insert(ship_callSign);
                ship_callSign = new Ship_CallSign(itens[7], Integer.parseInt(itens[10]),
                        Double.parseDouble(itens[11]), Double.parseDouble(itens[12]),
                        Double.parseDouble(itens[13]), itens[9]);
            }

            LocalDateTime dateTime = LocalDateTime.parse(itens[1], formatter);
            if (verifyDynamicData(itens)) {
                ship_callSign.addToDynamicDataTree(new ShipDynamicData
                        (dateTime, Double.parseDouble(itens[2]), Double.parseDouble(itens[3])
                                , Double.parseDouble(itens[4])
                                , Double.parseDouble(itens[5]),
                                Double.parseDouble(itens[6]),
                                itens[14],
                                itens[15]));
            }
        }
        if (ship_callSign != null)
            insert(ship_callSign);
    }

    /**
     * Top n map.
     *
     * @param n           the n
     * @param dataInicial the data inicial
     * @param dataFinal   the data final
     * @param vesselType  the vessel type
     * @return the map
     */
//Ex4
    public Map<Ship, Double> topN(Integer n, LocalDateTime dataInicial, LocalDateTime dataFinal, Integer vesselType) {

        Map<Ship, Double> map = new LinkedHashMap<>(); //mapa de keys com ships (top N), values com velocidade média

        List<Ship> listShip = new ArrayList<>(); //Top N de ships em função da travelled distance para cada vessel type

        for (Ship ship : inOrder()) {
            if (ship.getVesselTypeCode() == vesselType &&
                    ship.hasTwoMovementsBetweenDates(dataInicial, dataFinal)) {
                listShip.add(ship);                            //adiciona a lista so os ships com o respetivo vessel type
            }
        }

        //compara ships relativamente a traveled distance nesse periodo de tempo
        Comparator<Ship> comparator = (s1, s2) -> Double.compare(s2.getDynamicDataTree().getTravelledDistanceBetweenDates(dataInicial, dataFinal), s1.getDynamicDataTree().getTravelledDistanceBetweenDates(dataInicial, dataFinal));
        Collections.sort(listShip, comparator); //organiza a lista de acordo com o comparator

        for (int i = 0; i < n; i++) {
            try {
                map.put(listShip.get(i), listShip.get(i).getMeanSogBetweenDates(dataInicial, dataFinal));

            } catch (Exception e) {
                e.getCause();
                break;
            }
        }

        return map;
    }


    /**
     * Compare by movement int.
     *
     * @param s1 the s 1
     * @param s2 the s 2
     * @return the int
     */
//For exercise 3
    public int compareByMovement(Ship s1, Ship s2) {
        if (s1.getDynamicDataTree().getNumeroTotalMovimentos() > s2.getDynamicDataTree().getNumeroTotalMovimentos()) {
            return 1;
        }
        if (s1.getDynamicDataTree().getNumeroTotalMovimentos() < s2.getDynamicDataTree().getNumeroTotalMovimentos()) {
            return -1;
        }

        return 0;
    }


    /**
     * Ships movements and distance map.
     *
     * @return the map
     */
//ex3
    public Map<String, List<Double>> shipsMovementsAndDistance() {

        List<Ship> list = new ArrayList<>();
        List<Double> valuesList = new ArrayList<>();
        Map<String, List<Double>> map = new LinkedHashMap<>();

        for (Ship ship : inOrder()) {
            list.add(ship);
        }


        Comparator<Ship> comparator = new Comparator<Ship>() {   //compara ships relativamente a traveled distance nesse periodo de tempo
            @Override
            public int compare(Ship s1, Ship s2) {
                if (s1.getDynamicDataTree().getTravelledDistance() > s2.getDynamicDataTree().getTravelledDistance()) {
                    return -1;
                }
                if (s1.getDynamicDataTree().getTravelledDistance() < s2.getDynamicDataTree().getTravelledDistance()) {
                    return 1;
                }
                if (s1.getDynamicDataTree().getTravelledDistance() == s2.getDynamicDataTree().getTravelledDistance()) {
                    return compareByMovement(s1, s2);
                }

                return 0;
            }
        };


//        //compara ships relativamente a traveled distance nesse periodo de tempo
//        Comparator<Ship> comparator = (s1, s2) ->
//                Double.compare(s2.getDynamicDataTree().getTravelledDistance(), s1.getDynamicDataTree().getTravelledDistance());
        Collections.sort(list, comparator); //organiza a lista de acordo com o comparator


        for (Ship ship : list) {
            if (!map.containsKey(ship.getCode())) {
                map.put(ship.getCode(), new ArrayList<>());
                map.get(ship.getCode()).add(ship.getDynamicDataTree().getNumeroTotalMovimentos().doubleValue());
                map.get(ship.getCode()).add(ship.getDynamicDataTree().getTravelledDistance());
                map.get(ship.getCode()).add(ship.getDynamicDataTree().getDeltaDistance());
            }

        }


        return map;
    }


    /**
     * Navios pares list.
     *
     * @return the list
     */
//Ex5
    public List<Map.Entry<Ship, Ship>> naviosPares() {

        List<Ship> listaNaviosMais10 = new ArrayList<>();
        for (Ship s : inOrder()) {
            if (s.getTravelledDistance() > 10)
                listaNaviosMais10.add(s);
        }
        List<Map.Entry<Ship, Ship>> pairList = new ArrayList<Map.Entry<Ship, Ship>>();
        for (int i = 0; i < listaNaviosMais10.size(); i++) {
            for (int a = i + 1; a < listaNaviosMais10.size(); a++) {
                double departureDistance = DistancesCalculus.distance(listaNaviosMais10.get(i).getDepartureLatitude(),
                        listaNaviosMais10.get(i).getDepartureLongitude(),
                        listaNaviosMais10.get(a).getDepartureLatitude(),
                        listaNaviosMais10.get(a).getDepartureLongitude(), "K");
                double arrivalDistance = DistancesCalculus.distance(listaNaviosMais10.get(i).getArrivalLatitude(),
                        listaNaviosMais10.get(i).getArrivalLongitude(),
                        listaNaviosMais10.get(a).getArrivalLatitude(),
                        listaNaviosMais10.get(a).getArrivalLongitude(), "K");
                if (departureDistance <= 5 && arrivalDistance <= 5) {
                    Map.Entry<Ship, Ship> pair = new AbstractMap.SimpleImmutableEntry<>(listaNaviosMais10.get(i), listaNaviosMais10.get(a));
                    pairList.add(pair);
                }
            }
        }

        pairList.sort(new Comparator<Map.Entry<Ship, Ship>>() {
            @Override
            public int compare(Map.Entry<Ship, Ship> s1, Map.Entry<Ship, Ship> s2) {
                return Double.compare(Math.abs(s2.getKey().getDynamicDataTree().getTravelledDistance() - s2.getValue().getDynamicDataTree().getTravelledDistance()), Math.abs(s1.getKey().getDynamicDataTree().getTravelledDistance() - s1.getValue().getDynamicDataTree().getTravelledDistance()));
            }
        });

        return pairList;


    }


    /**
     * Gets element by idenfication.
     *
     * @param code the code
     * @return the element by idenfication
     */
    public Ship getElementByIdenfication(String code) {
        return getElementByIdenfication(root, code);
    }

    private Ship getElementByIdenfication(Node<Ship> node, String code) {
        if (root == null) return null;

        if (node == null) return null;

        if (node.getElement().getCode().compareTo(code) == 0) return node.getElement();

        if (code.compareTo(node.getElement().getCode()) > 0) return getElementByIdenfication(node.getRight(), code);

        if (code.compareTo(node.getElement().getCode()) < 0) return getElementByIdenfication(node.getLeft(), code);

        return null;
    }

    /**
     * Verify dynamic data boolean.
     *
     * @param itens the itens
     * @return the boolean
     */
    public boolean verifyDynamicData(String[] itens) {
        return Double.parseDouble(itens[2]) >= -90 &&       //Latitude
                Double.parseDouble(itens[2]) <= 90 &&       //Latitude
                Double.parseDouble(itens[3]) >= -180 &&     //Longitude
                Double.parseDouble(itens[3]) <= 180 &&      //Longitude
                Double.parseDouble(itens[5]) >= 0 &&        //Cog
                Double.parseDouble(itens[5]) <= 359 &&      //Cog
                Double.parseDouble(itens[6]) >= 0 &&        //heading
                Double.parseDouble(itens[6]) <= 359         //heading
                ;
    }

}