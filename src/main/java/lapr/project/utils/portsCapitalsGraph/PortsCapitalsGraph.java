package lapr.project.utils.portsCapitalsGraph;


import lapr.project.model.Capital;
import lapr.project.model.Country;
import lapr.project.model.Port;
import lapr.project.utils.DistancesCalculus;
import lapr.project.utils.KDTREE_PORTS;
import lapr.project.utils.graph.Algorithms;
import lapr.project.utils.graph.Edge;
import lapr.project.utils.graph.Graph;
import lapr.project.utils.graph.matrix.MatrixGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;



/**
 * The type Ports capitals graph.
 */
public class PortsCapitalsGraph {

    private final Graph<IPortsAndCapitals, Double> portsCapitalsGraph;

    private static final String COUNTRIES_FILE_PATH = "src/main/java/lapr/project/files/countries.csv";

    private static final String SEADISTS_FILE_PATH = "src/main/java/lapr/project/files/seadists.csv";

    private static final String BORDERS_FILE_PATH = "src/main/java/lapr/project/files/borders.csv";


    private final KDTREE_PORTS kdtree_ports;

    private final List<Country> countries;


    /**
     * Instantiates a new Ports capitals graph.
     *
     * @param kdtree_ports the kdtree ports
     */
    public PortsCapitalsGraph(KDTREE_PORTS kdtree_ports) {
        this.kdtree_ports = kdtree_ports;
        this.portsCapitalsGraph = new MatrixGraph<>(false);
        countries = new ArrayList<>();
    }


    /**
     * Create port capital graph.
     *
     * @param n the n
     * @throws FileNotFoundException the file not found exception
     */
    public void createPortCapitalGraph(int n) throws FileNotFoundException {
        insertCapitals();
        insertEdgesBetweenCapitals();
        insertPortsAsVertex();
        insertEdgesBetweenPorts(n);
        insertEdgesBetweenPortsSameCountry();
        insertEdgeBetweenCapitalAndClosestPortWithinCountry();
    }


    /**
     * Insert capitals.
     *
     * @throws FileNotFoundException the file not found exception
     */
    public void insertCapitals() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(COUNTRIES_FILE_PATH));
        sc.nextLine();
        while (sc.hasNextLine()) {
            String[] itens = sc.nextLine().split(",");
            Country country = new Country(itens[0], itens[1], itens[2], itens[3],
                    Double.parseDouble(itens[4]), itens[5], Double.parseDouble(itens[6]),
                    Double.parseDouble(itens[7]));
            if (!countries.contains(country)) {
                countries.add(country);
                insertVertex(country.getCapital());
            }
        }
    }

    /**
     * Insert ports as vertex.
     */
    public void insertPortsAsVertex() {
        this.kdtree_ports.inOrder().forEach(port -> {
            if (countries.contains(getCountryByName(port.getCountry()))) {
                insertVertex(port);
                getCountryByName(port.getCountry()).addPort(port); // adiciona o port a lista de ports do seu país
            }
        });
    }


    /**
     * Insert edges between ports.
     *
     * @param n the n
     * @throws FileNotFoundException the file not found exception
     */
    public void insertEdgesBetweenPorts(int n) throws FileNotFoundException {
        for (Port p : this.kdtree_ports.inOrder()) {
            for (Port p2 : getClosestPortsOtherCountries(p, n)) {
                insertEdge(p, p2, DistancesCalculus.distance(p.getLat(), p.getLon(), p2.getLat(), p2.getLon(), "K"));
            }
        }
    }

    /**
     * Gets closest ports other countries.
     *
     * @param port the port
     * @param n    the n
     * @return the closest ports other countries
     */
    public List<Port> getClosestPortsOtherCountries(Port port, int n) {
        List<Port> listPorts = new ArrayList<>();

        for (Port p : this.kdtree_ports.inOrder()) {
            if (p.getCountry().compareTo(port.getCountry()) != 0)
                listPorts.add(p);
        }
        //System.out.println("List ports:"+listPorts);

        Comparator<Port> comparator = (o1, o2) -> {
            double dis1 = DistancesCalculus.distance(port.getLat(), port.getLon(), o1.getLat(), o1.getLon(), "K");
            double dis2 = DistancesCalculus.distance(port.getLat(), port.getLon(), o2.getLat(), o2.getLon(), "K");
            if (dis1 > dis2) return 1;
            if (dis1 < dis2) return -1;
            return 0;
        };


        Collections.sort(listPorts, comparator);

        //System.out.println("List ports Final:"+listPorts);
        int contador = 0;
        List<Port> listFinal = new ArrayList<>();
        for (Port p : listPorts) {
            if (contador == n)
                return listFinal;
            listFinal.add(p);
            contador++;
        }
        return null;
    }


    /**
     * Insert edges between capitals.
     *
     * @throws FileNotFoundException the file not found exception
     */
    public void insertEdgesBetweenCapitals() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(BORDERS_FILE_PATH));
        sc.nextLine();
        while (sc.hasNextLine()) {
            String[] itens = sc.nextLine().split(",");
            Country from = getCountryByName(itens[0]);
            Country to = getCountryByName(itens[1]);
            if (from != null && to != null) {
                double distance = DistancesCalculus.distance(from.getCapitalLatitude(), from.getCapitalLongitude(),
                        to.getCapitalLatitude(), to.getCapitalLongitude(), "K");
                insertEdge(from.getCapital(), to.getCapital(), distance);
            }
        }
    }

    /**
     * insere os edges entre os ports do mesmo country
     */
    public void insertEdgesBetweenPortsSameCountry() {
        for (Country c : countries) {
            for (Port p : c.getPortTree().inOrder()) {
                for (Port p2 : c.getPortTree().inOrder()) {
                    if (p.getCode().compareTo(p2.getCode()) != 0) {
                        insertEdge(p, p2, DistancesCalculus.distance(p.getLat(), p.getLon(),
                                p2.getLat(), p2.getLon()));
                    }

                }
            }
        }
    }

    /**
     * Insert edge between capital and closest port within country.
     */
    public void insertEdgeBetweenCapitalAndClosestPortWithinCountry() {
        for (Country c : countries) {
            if (c.getPortTree().getElementClosestToCapital() != null) {
                Port port = c.getPortTree().getElementClosestToCapital();
                double distance = DistancesCalculus.distance
                        (c.getCapitalLatitude(), c.getCapitalLongitude(),
                                port.getLat(), port.getLon(), "K");
                insertEdge(c.getCapital(), port, distance);
            }
        }
    }

    /**
     * Gets country by name.
     *
     * @param name the name
     * @return the country by name
     */
    public Country getCountryByName(String name) {
        for (Country c : countries) {
            if (c.getCountry().trim().compareTo(name.trim()) == 0) return c;
        }
        return null;
    }

    /**
     * Insert vertex boolean.
     *
     * @param obj the obj
     * @return the boolean
     */
    public boolean insertVertex(IPortsAndCapitals obj) {
        if (!this.portsCapitalsGraph.validVertex(obj))
            return portsCapitalsGraph.addVertex(obj);
        return false;

    }

    /**
     * Insert edge boolean.
     *
     * @param from     the from
     * @param to       the to
     * @param distance the distance
     * @return the boolean
     */
    public boolean insertEdge(IPortsAndCapitals from, IPortsAndCapitals to, double distance) {

        return portsCapitalsGraph.addEdge(from, to, distance);

    }

    /**
     * Gets ports capitals graph.
     *
     * @return the ports capitals graph
     */
    public Graph<IPortsAndCapitals, Double> getPortsCapitalsGraph() {
        return portsCapitalsGraph;
    }

    /**
     * Gets country by capital.
     *
     * @param capital the capital
     * @return the country by capital
     */
//EX2
    public Country getCountryByCapital(Capital capital) {

        for (Country c : countries) {
            if (c.getCapital().equals(capital))
                return c;
        }
        return null;
    }

    /**
     * Gets capital vertices.
     *
     * @return the capital vertices
     */
    public ArrayList<Capital> getCapitalVertices() {

        ArrayList<Capital> capitals = new ArrayList<>();
        portsCapitalsGraph.vertices().forEach(c -> {
            if (c instanceof Capital)
                capitals.add((Capital) c);
        });

        return capitals;

    }

    /**
     * Gets adj vertices from capital.
     *
     * @param capital the capital
     * @return the adj vertices from capital
     */
    public Collection<Capital> getAdjVerticesFromCapital(Capital capital) {

        Collection<Capital> adjVerticesCapital = new ArrayList<>();
        portsCapitalsGraph.adjVertices(capital).forEach(c -> {
            if (c instanceof Capital)
                adjVerticesCapital.add((Capital) c);
        });


        return adjVerticesCapital;
    }


    /**
     * Colour the map list.
     *
     * @return the list
     */
    public List<Country> colourTheMap() {

        List<Country> listCountry = new ArrayList<>(this.countries);
        for (Country c : listCountry) {
            c.setColour(-1);
        }
        listCountry.get(0).setColour(0);

        boolean available[] = new boolean[getCapitalVertices().size()];

        Arrays.fill(available, true);

        for (Capital c : getCapitalVertices()) {
            Iterator<Capital> it = getAdjVerticesFromCapital(c).iterator();
            while (it.hasNext()) {
                Capital i = it.next();
                if (getCountryByCapital(i).getColour() != -1) {
                    int num = listCountry.indexOf(getCountryByCapital(i));
                    available[countries.get(num).getColour()] = false;
                }
            }

            for (int cr = 0; cr < listCountry.size(); cr++) {
                if (available[cr]) {
                    int index = listCountry.indexOf(getCountryByCapital(c));
                    listCountry.get(index).setColour(cr);
                    break;
                }

            }

            Arrays.fill(available, true);
        }
        return listCountry;
    }

    /**
     * N closest locals by continent map.
     *
     * @param n         the n
     * @param continent the continent
     * @return the map
     */
    public Map<Double, IPortsAndCapitals> nClosestLocalsByContinent(int n, String continent) {

        Map<Double, IPortsAndCapitals> map = new LinkedHashMap<>(); //mapa a retornar de keys com distancia média, values com ports/capitals
        Map<Double, IPortsAndCapitals> mapAux = new TreeMap<>(Double::compare); //mapa auxiliar de keys com distancia média, values com ports/capitals

        List<IPortsAndCapitals> arrayLocals = new ArrayList<>(); //Top N de locals em função da average distance para cada continent

        for (IPortsAndCapitals local : portsCapitalsGraph.vertices()) {
            if (local.getContinent().compareTo(continent) == 0) {
                arrayLocals.add(local);  //adiciona à lista so os locals do respetivo continent
            }
        }

//        for (IPortsAndCapitals local1 : arrayLocals) {
//            double sum = 0;
//            for (IPortsAndCapitals local2 : arrayLocals) {
//               sum+=DistancesCalculus.distance(local1.getLat(), local1.getLon(),
//                       local2.getLat(), local2.getLon(), "K");
//            }
//            mapAux.put(sum/arrayLocals.size()-1, local1);
//        }

        for (IPortsAndCapitals local1 : arrayLocals) {
            double sum = 0;
            for (IPortsAndCapitals local2 : arrayLocals) {
                LinkedList<IPortsAndCapitals> path = new LinkedList<>(); //caminho mais curto
                Algorithms.shortestPath(portsCapitalsGraph, local1, local2, Double::compare, Double::sum, 0.0, path);
                for (int i = 0; i < path.size(); i++) {
                    if (i + 1 == path.size()) break;
                    sum += portsCapitalsGraph.edge(path.get(i), path.get(i + 1)).getWeight();
                }
            }
            mapAux.put(sum / arrayLocals.size(), local1);
        }

        int contador = 0;
        for (Map.Entry<Double, IPortsAndCapitals> entry : mapAux.entrySet()) {
            if (contador == n) {
                return map;
            }
            map.put(entry.getKey(), entry.getValue());
            contador++;
        }

        return map;
    }


    /**
     * Gets countries.
     *
     * @return the countries
     */
    public List<Country> getCountries() {
        return countries;
    }

//////////////////////////////////////////////////////////////////Sprint4//////////////////////////////////////////////////////////////////////
    //EX1 (US401)

    /**
     * Retorna os n portos com maior centralidade
     *
     * @param n numero de portos com maior centralidade a retornar
     * @return mapa com o porto e respetiva centralidade
     */
    public Map<Port, Integer> getNPortsGreaterCentrality(int n) {

        List<Map.Entry<Port, Integer>> listPortCentrality = new ArrayList<>(fillPortsCentrality().entrySet()); //cria uma lista e recebe o mapa com os portos e respetivas centralidades

        listPortCentrality.sort(new Comparator<Map.Entry<Port, Integer>>() { //organiza a list por ordem decrescente de centralidade
            @Override
            public int compare(Map.Entry<Port, Integer> p1, Map.Entry<Port, Integer> p2) {
                return p2.getValue() - p1.getValue();
            }
        });

        Map<Port, Integer> listNPorts = new LinkedHashMap<>();

        for (int i = 0; i < n; i++) {        //adiciona ao mapa a retornar apenas os n pedidos
            if (i > listPortCentrality.size() - 1) break;
            listNPorts.put(listPortCentrality.get(i).getKey(), listPortCentrality.get(i).getValue());
        }

        return listNPorts;
    }

    /**
     * Calcula a centralidade para todos os portos
     *
     * @return mapa com portos e respetivas centralidades
     */
    public Map<Port, Integer> fillPortsCentrality() {
        Map<Port, Integer> portsCentrality = new LinkedHashMap<>();

        for (IPortsAndCapitals local1 : portsCapitalsGraph.vertices()) {
            ArrayList<LinkedList<IPortsAndCapitals>> paths = new ArrayList<>();   //Lista dos shortest paths de cada vértice do grafo
            ArrayList<Double> dists = new ArrayList<>();                        //Lista de distancias dos shortest paths

            Algorithms.shortestPaths(portsCapitalsGraph, local1, Double::compare, Double::sum, 0.0, paths, dists); //Calcula todos os shortests paths para o respetivo local

            for (LinkedList<IPortsAndCapitals> path : paths) {
                if (path != null) {
                    for (IPortsAndCapitals local : path) {
                        if (local instanceof Port) {
                            Integer count = portsCentrality.get(local);
                            if (count == null) {
                                count = 0;
                            }
                            portsCentrality.put((Port) local, count + 1);
                        }
                    }
                }
            }
        }
        return portsCentrality;
    }

/*    // implementation of traveling
    // Salesman Problem
    public double travllingSalesmanProblem(IPortsAndCapitals s) {

        ArrayList<IPortsAndCapitals> res = new ArrayList<>();

        for (IPortsAndCapitals local : portsCapitalsGraph.vertices()) {
            if (!local.equals(s)) {
                res.add(local);
            }
        }

        // store minimum weight
        // Hamiltonian Cycle.
        double min_path = Double.MAX_VALUE;
        do {
            // store current Path weight(cost)
            int current_pathweight = 0;

            // compute current path weight
            IPortsAndCapitals k = s;

            for (IPortsAndCapitals local1 : res) {
                current_pathweight += DistancesCalculus.distance(k.getLat(), k.getLon(), local1.getLat(), local1.getLon(), "K");
                k = local1;
            }

            current_pathweight += DistancesCalculus.distance(k.getLat(), k.getLon(), s.getLat(), s.getLon(), "K");

            // update minimum
            min_path = Math.min(min_path,
                    current_pathweight);

        } while (findNextPermutation(res));

        return min_path;
    }*/


    // implementation of traveling
    // Salesman Problem
    public double travllingSalesmanProblem1(int s) {
        // store all vertex apart
        // from source vertex
        ArrayList<Integer> vertex =
                new ArrayList<Integer>();

        for (int i = 0; i < portsCapitalsGraph.numVertices(); i++)
            if (i != s)
                vertex.add(i);

        // store minimum weight
        // Hamiltonian Cycle.
        int min_path = Integer.MAX_VALUE;
        do {
            // store current Path weight(cost)
            int current_pathweight = 0;

            // compute current path weight
            int k = s;

            for (int i = 0;
                 i < vertex.size(); i++) {
                current_pathweight += portsCapitalsGraph.edge(k, vertex.get(i)).getWeight();
                k = vertex.get(i);
            }
            current_pathweight += portsCapitalsGraph.edge(k, s).getWeight();

            // update minimum
            min_path = Math.min(min_path,
                    current_pathweight);

        } while (findNextPermutation1(vertex));

        return min_path;
    }

    // Function to find the next permutation
    // of the given integer array
    public static boolean findNextPermutation1(ArrayList<Integer> data) {
        // If the given dataset is empty
        // or contains only one element
        // next_permutation is not possible
        if (data.size() <= 1)
            return false;

        int last = data.size() - 2;

        // find the longest non-increasing
        // suffix and find the pivot
        while (last >= 0) {
            if (data.get(last) <
                    data.get(last + 1)) {
                break;
            }
            last--;
        }

        // If there is no increasing pair
        // there is no higher order permutation
        if (last < 0)
            return false;

        int nextGreater = data.size() - 1;

        // Find the rightmost successor
        // to the pivot
        for (int i = data.size() - 1;
             i > last; i--) {
            if (data.get(i) >
                    data.get(last)) {
                nextGreater = i;
                break;
            }
        }

        // Swap the successor and
        // the pivot
        data = swap(data,
                nextGreater, last);

        // Reverse the suffix
        data = reverse(data, last + 1,
                data.size() - 1);

        // Return true as the
        // next_permutation is done
        return true;
    }

    // Function to swap the data
    // present in the left and right indices
    public static ArrayList<Integer> swap(
            ArrayList<Integer> data,
            int left, int right) {
        // Swap the data
        int temp = data.get(left);
        data.set(left, data.get(right));
        data.set(right, temp);

        // Return the updated array
        return data;
    }

    // Function to reverse the sub-array
    // starting from left to the right
    // both inclusive
    public static ArrayList<Integer> reverse(
            ArrayList<Integer> data,
            int left, int right) {
        // Reverse the sub-array
        while (left < right) {
            int temp = data.get(left);
            data.set(left++,
                    data.get(right));
            data.set(right--, temp);
        }

        // Return the updated array
        return data;
    }



   /* // Function to find the next permutation
    // of the given integer array
    public static boolean findNextPermutation(ArrayList<IPortsAndCapitals> data) {
        // If the given dataset is empty
        // or contains only one element
        // next_permutation is not possible
        if (data.size() <= 1)
            return false;

        int last = data.size() - 2;


        // find the longest non-increasing
        // suffix and find the pivot
        while (last >= 0) {
            if (data.get(last) < data.get(last + 1)) {
                break;
            }
            last--;
        }

        // If there is no increasing pair
        // there is no higher order permutation
        if (last < 0)
            return false;

        int nextGreater = data.size() - 1;

        // Find the rightmost successor
        // to the pivot
        for (int i = data.size() - 1;
             i > last; i--) {
            if (data.get(i) > data.get(last)) {
                nextGreater = i;
                break;
            }
        }

        // Swap the successor and
        // the pivot
        data = swap(data,
                nextGreater, last);

        // Reverse the suffix
        data = reverse(data, last + 1,
                data.size() - 1);

        // Return true as the
        // next_permutation is done
        return true;
    }*/


    /**
     * shortestPathNormal
     *
     * @param l1
     * @param l2
     * @return
     */
    public List<IPortsAndCapitals> shortestPathLandOrSeaPath(IPortsAndCapitals l1, IPortsAndCapitals l2) {
        LinkedList<IPortsAndCapitals> path = new LinkedList<>();
        Algorithms.shortestPath(portsCapitalsGraph, l1, l2, Double::compare, Double::sum, 0.0, path);
        return path;
    }

    /**
     * Shortest Path Maritime Path
     *
     * @param l1
     * @param l2
     * @return
     */
    public List<Port> shortestMaritimePath(Port l1, Port l2) {
        LinkedList<Port> path = new LinkedList<>();
        Graph<Port, Double> portsGraph = new MatrixGraph<>(false);
        for (IPortsAndCapitals local : portsCapitalsGraph.vertices()) {
            if (local instanceof Port) {
                portsGraph.addVertex((Port) local);
            }
        }

        for (Edge<IPortsAndCapitals, Double> edge : portsCapitalsGraph.edges()) {
            if ((edge.getVOrig() instanceof Port) && (edge.getVDest() instanceof Port)) {
                portsGraph.addEdge((Port) edge.getVOrig(), (Port) edge.getVDest(), edge.getWeight());
            }
        }
        Algorithms.shortestPath(portsGraph, l1, l2, Double::compare, Double::sum, 0.0, path);

        return path;
    }

    /**
     * @param local1 l1
     * @param local2 l2
     * @return
     */
    public List<IPortsAndCapitals> shortestPathLand(IPortsAndCapitals local1, IPortsAndCapitals local2) {
        LinkedList<IPortsAndCapitals> path = new LinkedList<>();
        Graph<IPortsAndCapitals, Double> graph = new MatrixGraph<>(false);
        for (IPortsAndCapitals local : this.portsCapitalsGraph.vertices()) {
            graph.addVertex(local);
        }
        for (Edge<IPortsAndCapitals, Double> edge : this.portsCapitalsGraph.edges()) {
            if (!(edge.getVOrig() instanceof Port && edge.getVDest() instanceof Port)) {
                graph.addEdge(edge.getVOrig(), edge.getVDest(), edge.getWeight());
            }
        }
        Algorithms.shortestPath(graph, local1, local2, Double::compare, Double::sum, 0.0, path);
        return path;
    }

    public List<IPortsAndCapitals> shortestPathPassingOnN(LinkedList<IPortsAndCapitals> passing, IPortsAndCapitals first, IPortsAndCapitals last) {
        LinkedList<IPortsAndCapitals> pathFinal = new LinkedList<>();
        Graph<IPortsAndCapitals, Double> graph = Algorithms.minDistGraph(this.portsCapitalsGraph, Double::compare, Double::sum);//n^3
        double pesoInicial = getPathWeightFromWarshall(graph, first, last, passing);
        LinkedList<IPortsAndCapitals> listaInicial = getPathFrom(first, last, passing);
        return getMinPathFromWarshell(graph, first, last, passing.size(), passing, listaInicial, pesoInicial);
    }

    private void swap(List<IPortsAndCapitals> input, int a, int b) {
        IPortsAndCapitals tmp = input.get(a);
        input.set(a, input.get(b));
        input.set(b, tmp);
    }

    public List<IPortsAndCapitals> getMinPathFromWarshell(Graph<IPortsAndCapitals, Double> graph,
                                                          IPortsAndCapitals verticeInicial,
                                                          IPortsAndCapitals verticeFinal,
                                                          int n,
                                                          List<IPortsAndCapitals> elements,
                                                          List<IPortsAndCapitals> listaFinal, double pesoFinal) {

        if (n == 1) {
            return listaFinal;
        }
        double peso = getPathWeightFromWarshall(graph, verticeInicial, verticeFinal, elements);
        if (peso < pesoFinal) {
            listaFinal = new LinkedList<>(getPathFrom(verticeInicial, verticeFinal, elements));
            pesoFinal = peso;
        }
        for (int i = 0; i < n - 1; i++) {
            if (n % 2 == 0) {
                swap(elements, i, n - 1);
            } else {
                swap(elements, 0, n - 1);
            }
        }
        return getMinPathFromWarshell(graph, verticeInicial, verticeFinal, n - 1, elements, listaFinal, pesoFinal);

    }

    public double getPathWeightFromWarshall(Graph<IPortsAndCapitals, Double> graph,
                                            IPortsAndCapitals verticeInicial,
                                            IPortsAndCapitals verticeFinal,
                                            List<IPortsAndCapitals> elements) {

        double value = graph.edge(verticeInicial, elements.get(0)).getWeight();

        for (int i = 0; i < elements.size() - 1; i++) {
            value += graph.edge(elements.get(i), elements.get(i + 1)).getWeight();
        }
        value += graph.edge(elements.get(elements.size() - 1), verticeFinal).getWeight();

        return value;
    }

    public LinkedList<IPortsAndCapitals> getPathFrom(IPortsAndCapitals verticeInicial,
                                                     IPortsAndCapitals verticeFinal,
                                                     List<IPortsAndCapitals> elements) {

        LinkedList<IPortsAndCapitals> listaFinal = new LinkedList<>();

        Algorithms.shortestPath(this.portsCapitalsGraph, verticeInicial, elements.get(0), Double::compare, Double::sum, 0.0, listaFinal);

        for (int i = 0; i < elements.size() - 1; i++) {
            LinkedList<IPortsAndCapitals> pathAux = new LinkedList<>();
            Algorithms.shortestPath(this.portsCapitalsGraph, elements.get(i), elements.get(i + 1), Double::compare, Double::sum, 0.0, pathAux);
            listaFinal.addAll(pathAux);
        }

        LinkedList<IPortsAndCapitals> pathAux = new LinkedList<>();
        Algorithms.shortestPath(this.portsCapitalsGraph, elements.get(elements.size() - 1), verticeFinal, Double::compare, Double::sum, 0.0, pathAux);
        listaFinal.addAll(pathAux);
        return listaFinal;
    }

    public IPortsAndCapitals getVerticeByName(String name) {
        for (IPortsAndCapitals local : this.portsCapitalsGraph.vertices()) {
            if (local.getName().equalsIgnoreCase(name)) {
                return local;
            }
        }
        return null;
    }

    public IPortsAndCapitals getVerticeByNameCapital(String name) {
        for (IPortsAndCapitals local : this.portsCapitalsGraph.vertices()) {
            if (local.getName().equalsIgnoreCase(name) && local instanceof Capital) {
                return local;
            }
        }
        return null;
    }
    
    


    
///// tentativa ///////////////
    
//    public Pair<List<IPortsAndCapitals>,Double> getPathAndTotalDistance(IPortsAndCapitals initialLocation) {
//        
//        
//        List<IPortsAndCapitals> list = new LinkedList<>();
//        
//        getMostEfficientRoute2(initialLocation, list);
//        
//        Double sum = 0.0;
//        for(IPortsAndCapitals l1 : list){
//           for(IPortsAndCapitals l2 : list){
//              sum = sum + portsCapitalsGraph.edge(l1, l2).getWeight();
//           } 
//        }
//        
//        return new Pair<>(list, sum);
//    }
//    
//    
//    
//    public boolean getMostEfficientRoute2(IPortsAndCapitals initialLocation, List<IPortsAndCapitals> list) {
//
//
//        
//        boolean[] visited = new boolean[portsCapitalsGraph.vertices().size()];
//        boolean[] recStack = new boolean[portsCapitalsGraph.vertices().size()];//keep track of vertices in the recursion stack.
//        
//        list.add(initialLocation);
//        // Call the recursive helper function to
//        // detect cycle in different DFS trees
//        for (IPortsAndCapitals ip: portsCapitalsGraph.vertices())
//            if (getMostEfficientRouteUtil(ip, visited, recStack, list)){
//                list.add(initialLocation);
//                return true;
//            }
//        
//        
//        
//        return false;
//        
//    }
//    
//    
//    
//    private boolean getMostEfficientRouteUtil(IPortsAndCapitals currentVertex, boolean visited[], boolean recStack[], List<IPortsAndCapitals>path) {
//
//        int currentKey = portsCapitalsGraph.key(currentVertex);
//        //boolean[] visited = new boolean[portsCapitalsGraph.vertices().size()];
//        
//        // Mark the current node as visited and
//        // part of recursion stack
//        if (recStack[currentKey]){
//            //path.add(currentVertex);
//            return true;
//        }
//            
//            
//        if (visited[currentKey])
//            return false;
//        
//        //marca como visitado
//        visited[currentKey] = true;
//        
//        recStack[currentKey] = true;
//        path.add(currentVertex);
//        
//        
//        
//        //percorre lista de adjacentes
//        for(IPortsAndCapitals adj : portsCapitalsGraph.adjVertices(currentVertex)){
//            if (getMostEfficientRouteUtil(adj, visited, recStack, path))
//                return true;
//        }
//        
//        recStack[currentKey] = false;
//        path.remove(currentVertex);
// 
//        return false;
//      
//        
//    }
//    
    
 
    
    
    ////////HAMILTONIAN
    //EX3
    
    private boolean isSafe(IPortsAndCapitals local, List<IPortsAndCapitals> path, int pos){
        
        /* Check if this vertex is an adjacent vertex of
           the previously added vertex. */
        if(portsCapitalsGraph.edge(path.get(pos-1), local)==null){
            return false;
        }
        
        /* Check if the vertex has already been included.
           This step can be optimized by creating an array
           of size V */        
        for(int i = 0; i<pos;i++){
            if(path.get(i).equals(local))
                return false;
        }
        
        return true;
        
     }
    
    private boolean hamCycleUtil(List<IPortsAndCapitals> path, int pos, int minimumPathSize, long maxTime, long startTime){
        
        //se maxTime dif de 0 ou se demorar mais do que (maxTime)segundos, retorna falso
        if (maxTime!=0 && System.currentTimeMillis()-startTime>maxTime)
            return false;
        
        /* base case: If all vertices are included in
           Hamiltonian Cycle */
        if(pos == portsCapitalsGraph.vertices().size()){
            // And if there is an edge from the last included
            // vertex to the first vertex
            if(portsCapitalsGraph.edge(path.get(pos-1), path.get(0))!=null)
                return true;
            else
                return false;
        }
        
        /*voltou ao vertice inicial && pos maior que minimumPathSize*/
        if(portsCapitalsGraph.edge(path.get(pos-1), path.get(0))!=null && pos>=minimumPathSize){
            return true;
        }
        
        // Try different vertices as a next candidate in
        // Hamiltonian Cycle. We don't try for 0 as we
        // included 0 as starting point in hamCycle()
        //for(IPortsAndCapitals l : portsCapitalsGraph.vertices()){
        IPortsAndCapitals last = path.get(path.size()-1);
        List<IPortsAndCapitals> adj = new ArrayList<>(portsCapitalsGraph.adjVertices(last));  
        //organiza a lista por distancia aos adjacentes
        Collections.sort(adj, new Comparator<IPortsAndCapitals>(){
            @Override
            public int compare(IPortsAndCapitals o1, IPortsAndCapitals o2) {
                return portsCapitalsGraph.edge(last, o1).getWeight().compareTo(portsCapitalsGraph.edge(last, o2).getWeight());
            }            
        });
        
        for(IPortsAndCapitals l : adj){
            if(path.get(0)!=l){
            /* Check if this vertex can be added to Hamiltonian
               Cycle */
            if(isSafe(l,path,pos)){
                
                //path.set(pos, l);
                path.add(l);
                
                /* recur to construct rest of the path */
                if(hamCycleUtil(path,pos+1,minimumPathSize, maxTime, startTime)==true)
                    return true;
                
                /* If adding vertex v doesn't lead to a solution,
                   then remove it */
                path.remove(pos);

                //se maxTime dif de 0 ou se demorar mais do que (maxTime)segundos, retorna falso
                if (maxTime!=0 && System.currentTimeMillis()-startTime>maxTime)
                    return false;
            }
          }
        }
        
        /* If no vertex can be added to Hamiltonian Cycle
           constructed so far, then return false */
        return false;
        
    }
    
    
    public List<IPortsAndCapitals> hamCycle(IPortsAndCapitals local){
        
//        for(IPortsAndCapitals l : portsCapitalsGraph.adjVertices(local)){
//            System.out.println(l);
//        }
        
        List<IPortsAndCapitals> path = new LinkedList<>();
        
//        for(IPortsAndCapitals l : portsCapitalsGraph.vertices()){
//            path.remove(l);
//        }

        //metodo de newton para ir tentanto com varios numeros de vertices 
        int left=0;
        int right=portsCapitalsGraph.vertices().size()-1;
        int best=0;
        int secondBest=0;
        //quando os limites se cruzarem, para o ciclo while
        while(left<right){            
            int mid=(left+right)/2;
            System.out.println("left limit:" +left+", right limit:"+right+", testing with:"+mid);
            path.clear();
            
            //add initial location
            path.add(local);
            if(hamCycleUtil(path,1,mid, 5000, System.currentTimeMillis())==false){
                System.out.println("\nSolution not found yet");
                right=mid-1;
            }else{
                System.out.println("\nFound with size=" + path.size());                
                left=path.size()+1;
                secondBest = mid;
                if (best<left)
                    best=left;
            }            
        }
        
        path.clear();

        //adicionar o initialLoc
        path.add(local);
        
        //final path
        boolean flag;
        flag=hamCycleUtil(path,1,best,10000, System.currentTimeMillis());
        
        //se estuver a demorar muito, usa o ultimo caminho que encontrou
        if(flag==false)
        hamCycleUtil(path,1,secondBest, 0, System.currentTimeMillis());
        
        
        /*
        path.add(local);
        if(hamCycleUtil(path,1,3)==false){
            System.out.println("\nSolution does not exist");
            return 0;
        }*/
        
        printSolution(path);
        
        //return finalPath
        return path;
        
    }
    
    
    //imprime path do circuito
    private void printSolution(List<IPortsAndCapitals> path){
        
        System.out.println("Solution Exists: Following" +
                           " is one Hamiltonian Cycle");
        
        for(int i = 0; i < path.size(); i++){
            System.out.print(" " + path.get(i) + " ");
        }
        
        // Let us print the first vertex again to show the
        // complete cycle
        System.out.println(" " + path.get(0) + " ");
    }
    
    

    
    
        
    

}

