package lapr.project.controller;

import javafx.util.Pair;

import lapr.project.model.*;
import lapr.project.utils.*;
import lapr.project.utils.portsCapitalsGraph.IPortsAndCapitals;
import lapr.project.utils.portsCapitalsGraph.PortsCapitalsGraph;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

import java.time.LocalDateTime;
import java.util.*;

/**
 * The type Search information controller.
 */
public class SearchInformationController {
    /**
     * The Tree ships mmsi.
     */
    TREE_SHIPS_MMSI tree_ships_mmsi;
    /**
     * The Tree ships imo.
     */
    TREE_SHIPS_IMO tree_ships_imo;
    /**
     * The Tree ships callsign.
     */
    TREE_SHIPS_CALLSIGN tree_ships_callsign;
    /**
     * KDTree
     */
    KDTREE_PORTS kdtree_ports;

    PortsCapitalsGraph portsCapitalsGraph;

    /**
     * Instantiates a new Search information controller.
     *
     * @throws IOException the io exception
     */
    public SearchInformationController() throws IOException {
        tree_ships_mmsi = new TREE_SHIPS_MMSI(new Ship_MMSI());
        tree_ships_callsign = new TREE_SHIPS_CALLSIGN(new Ship_CallSign());
        tree_ships_imo = new TREE_SHIPS_IMO(new Ship_IMO());
        kdtree_ports = new KDTREE_PORTS();
        tree_ships_callsign.createTree();
        tree_ships_imo.createTree();
        tree_ships_mmsi.createTree();
        kdtree_ports.createKDTree();
        portsCapitalsGraph = new PortsCapitalsGraph(kdtree_ports);


    }

    /**
     * Instantiates a new Search information controller.
     *
     * @param path the path
     * @throws IOException the io exception
     */
    public SearchInformationController(String path) throws IOException {
        tree_ships_mmsi = new TREE_SHIPS_MMSI(new Ship_MMSI(), path);
        tree_ships_callsign = new TREE_SHIPS_CALLSIGN(new Ship_CallSign(), path);
        tree_ships_imo = new TREE_SHIPS_IMO(new Ship_IMO(), path);
        //kdtree_ports = new KDTREE_PORTS(path);
        tree_ships_callsign.createTree();
        tree_ships_imo.createTree();
        tree_ships_mmsi.createTree();
        kdtree_ports.createKDTree();
    }

/////////////////////////////////////////////////////////////////Sprint 1///////////////////////////////////////////////////////////////

    /**
     * Search information ship pair.
     *
     * @param code the code
     * @return the pair
     */
//EX2
    public Pair<String, List<String>> searchInformationShip(String code) {

        Ship ship = null;
        if (tree_ships_mmsi.getElementByIdenfication(code) != null)
            ship = tree_ships_mmsi.getElementByIdenfication(code);
        if (tree_ships_imo.getElementByIdenfication(code) != null)
            ship = tree_ships_imo.getElementByIdenfication(code);

        if (tree_ships_callsign.getElementByIdenfication(code) != null)
            ship = tree_ships_callsign.getElementByIdenfication(code);

        if (ship == null) return null;

        String finalCode = ship.getCode();

        DecimalFormat df = new DecimalFormat("0.000");

        String dateInicioStr = "";
        if (ship.getInicioPartidaDate() != null)
            dateInicioStr += ship.getInicioPartidaDate().toString();

        String dateFinalStr = "";
        if (ship.getFimPartidaDate() != null)
            dateFinalStr += ship.getFimPartidaDate();


        List<String> list = Arrays.asList
                (ship.getName(), String.valueOf(ship.getVesselTypeCode()), String.valueOf(ship.getInicioPartidaDate()),
                        String.valueOf(ship.getFimPartidaDate()), String.valueOf(ship.getNumeroTotalMovimentos()),
                        String.valueOf(ship.getMaxSog()), df.format(ship.getMeanSog()),
                        String.valueOf(ship.getMaxCog()), df.format(ship.getMeanCog()),
                        String.valueOf(ship.getDepartureLatitude()), String.valueOf(ship.getDepartureLongitude()),
                        String.valueOf(ship.getArrivalLatitude()), String.valueOf(ship.getArrivalLongitude()),
                        df.format(ship.getTravelledDistance()), df.format(ship.getDeltaDistance()));

        return new Pair<>(finalCode, list);
    }

    /**
     * Ships movements and distance controller.
     */
//EX3 (chamar metodos supertree)
    public void shipsMovementsAndDistanceController() {
        Map<String, List<Double>> map = new HashMap<>();

        map = tree_ships_mmsi.shipsMovementsAndDistance();

        for (Map.Entry<String, List<Double>> entry : map.entrySet()) {
            System.out.println("MMSI:" + entry.getKey() + "\n" + "Numero Movimentos:" + entry.getValue().get(0).intValue() + "\n"
                    + "Travelled Distance:" + entry.getValue().get(1) + "\n" + "Delta Distance:" + entry.getValue().get(2) + "\n\n");
            System.out.println("-------------------------------------------");
        }

    }

    /**
     * Top n ships controller.
     *
     * @param n           the n
     * @param dataInicial the data inicial
     * @param dataFinal   the data final
     */
//Ex4
    public void topNShipsController(Integer n, LocalDateTime dataInicial, LocalDateTime dataFinal) {

        List<Integer> list = new ArrayList<>(); //lista de vessel Types
        DecimalFormat df = new DecimalFormat("0.00000"); //numero de casas decimais nos prints

        for (Ship ship : tree_ships_mmsi.inOrder()) {
            if (!list.contains(ship.getVesselTypeCode())) {
                list.add(ship.getVesselTypeCode());       //preenche a lista cm todos os vessel types dos ships existentes
            }
        }

        for (Integer vesselType : list) {
            Map<Ship, Double> map = tree_ships_mmsi.topN(n, dataInicial, dataFinal, vesselType);
            for (Map.Entry<Ship, Double> maps : map.entrySet()) {
                System.out.println(maps.getKey() + "\n    Velocidade Média=" + df.format(maps.getValue()));
                System.out.println("    Travelled Distance=" + df.format(maps.getKey().getTravelledDistanceBetweenDates(dataInicial, dataFinal))); //apenas para verificar que organiza corretamente
            }
            System.out.println("----------------------------------------------------------------------------------------");
        }
    }

    /**
     * Pares iguais.
     */
//EX5
    public void paresIguais() {

        List<Map.Entry<Ship, Ship>> pairs = tree_ships_mmsi.naviosPares();

        for (Map.Entry<Ship, Ship> pair : pairs) {
            System.out.println(pair.getKey() + " | " + pair.getValue());
            System.out.println("---------------------------------------");
        }
    }


//////////////////////////////////////////////////////////////////////Sprint 2//////////////////////////////////////////////////////////


    //EX2
    public Port portoMaisProximo(String code, LocalDateTime data) {

        if (code == null)
            return null;


        Ship ship = tree_ships_callsign.getElementByIdenfication(code);

        Pair<Double, Double> pair = ship.getDynamicDataTree().getCoordsByData(data);

        if (pair != null) {


            Port port = kdtree_ports.findNearestNeighbour(pair.getKey(), pair.getValue());

            System.out.println(pair);

            return port;
        }
        return null;

    }


/////////////////////////////////////////////////////////////////////////Sprint 3//////////////////////////////////////////////////////

    //EX1
    public void createGraph(int n) throws FileNotFoundException {
        portsCapitalsGraph.createPortCapitalGraph(n);
        //System.out.println(portsCapitalsGraph.getPortsCapitalsGraph());
    }

    //EX2
    public void mapColour() {


        portsCapitalsGraph.colourTheMap().forEach( country1 -> {
            System.out.println(country1.getCountry()+"   "+country1.getColour());
        });

    }

    //EX3
    public void nClosestLocalsByContinent(int n) {

        List<String> list = new ArrayList<>(); //lista de Continentes

        for(IPortsAndCapitals iPortsAndCapitals : portsCapitalsGraph.getPortsCapitalsGraph().vertices()){
            if(!list.contains(iPortsAndCapitals.getContinent())){
               list.add(iPortsAndCapitals.getContinent());
            }
        }

        for(String continent : list){
            System.out.println("\nContinent: "+continent);
            Map<Double, IPortsAndCapitals> map = portsCapitalsGraph.nClosestLocalsByContinent(n, continent);
            for(Map.Entry<Double, IPortsAndCapitals> maps : map.entrySet()){
                System.out.println("\nAverage Distance= " + maps.getKey() + maps.getValue());
            }
            System.out.println("----------------------------------------------------------------------------------------");
        }
    }

////////////////////////////////////////////////////////////////Sprint4//////////////////////////////////////////////////////////////////

    //EX1
    public void getNPortsGreaterCentrality(int n) {

        Map<Port, Integer> mapNPorts = portsCapitalsGraph.getNPortsGreaterCentrality(n); //recebe o mapa dos n portos e respetivas centralidades

        for(Map.Entry<Port, Integer> entry :mapNPorts.entrySet()){
            System.out.println(entry.getKey()+"\n    Centrality: "+entry.getValue()+"\n");
        }
    }

    //EX2
    public List<IPortsAndCapitals> getShortestPathPassingN(String localInicioStr, String localFinalStr, List<String> nPassing) {
        IPortsAndCapitals localInicial = portsCapitalsGraph.getVerticeByName(localInicioStr);
        IPortsAndCapitals localFinal = portsCapitalsGraph.getVerticeByName(localFinalStr);
        LinkedList<IPortsAndCapitals> listN = new LinkedList<>();

        for (String s : nPassing) {
            listN.add(portsCapitalsGraph.getVerticeByName(s));
        }


        return this.portsCapitalsGraph.shortestPathPassingOnN(listN, localInicial, localFinal);

    }

    public List<IPortsAndCapitals> getShortestPathLand(String localInicioStr, String localFinalStr) {
        return this.portsCapitalsGraph.shortestPathLand(portsCapitalsGraph.getVerticeByName(localInicioStr), portsCapitalsGraph.getVerticeByName(localFinalStr));
    }

    public List<Port> getShortestPathMaritime(String localInicioCodigo, String localFinalCodigo) {
        return this.portsCapitalsGraph.shortestMaritimePath(kdtree_ports.getPortByCode(localFinalCodigo), kdtree_ports.getPortByCode(localFinalCodigo));
    }

    public List<IPortsAndCapitals> getShortestPathLandOrSea(String localInicio, String localFinal){
        return this.portsCapitalsGraph.shortestPathLandOrSeaPath(portsCapitalsGraph.getVerticeByName(localInicio), portsCapitalsGraph.getVerticeByName(localFinal));
    }
    

    //EX3 a devolver
//    public List<IPortsAndCapitals> getCircuitFromInicialLoc(IPortsAndCapitals initialLoc){
//        
//        List<IPortsAndCapitals> path = new LinkedList<>();
//        
//        path=portsCapitalsGraph.hamCycle(initialLoc);
//        
//        return path;
//    }
    
    //EX3 chama para port
    public void getCircuitFromInicialLocPort(String initialLoc){
        IPortsAndCapitals port = kdtree_ports.getPortByCode(initialLoc);
        
        portsCapitalsGraph.hamCycle(port);
        
    }
    
    //EX3 chama para capital
    public void getCircuitFromInicialLocCapital(String initialLoc){
        
        IPortsAndCapitals capital = portsCapitalsGraph.getVerticeByName(initialLoc);
        
        //IPortsAndCapitals capital = country.getCapital();
        
        
        portsCapitalsGraph.hamCycle(capital);
        
    }
    
    
}