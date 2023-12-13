package lapr.project.utils.portsCapitalsGraph;

import lapr.project.model.Capital;
import lapr.project.model.Country;
import lapr.project.model.Port;
import lapr.project.utils.DistancesCalculus;
import lapr.project.utils.KDTREE_PORTS;
import lapr.project.utils.graph.Graph;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PortsCapitalsGraphTest {

    @Test
    void createPortCapitalGraph() throws FileNotFoundException {
        System.out.println("createPortCapitalGraph");
        KDTREE_PORTS KDP = new KDTREE_PORTS("src/main/java/lapr/project/files/sports.csv");
        PortsCapitalsGraph PCG = new PortsCapitalsGraph(KDP);
        PCG.createPortCapitalGraph(2);
    }

    @Test
    void insertCapitals() throws FileNotFoundException {
        System.out.println("insertCapitals");
        KDTREE_PORTS KDP = new KDTREE_PORTS("src/main/java/lapr/project/files/sports.csv");
        PortsCapitalsGraph PCG = new PortsCapitalsGraph(KDP);
        PCG.insertCapitals();
        Graph<IPortsAndCapitals,Double> g = PCG.getPortsCapitalsGraph();

        ArrayList<Capital> capitals = new ArrayList<>();
        g.vertices().forEach(c -> {
            if (c instanceof Capital)
                capitals.add((Capital) c);
        });

        assertEquals("Nicosia",capitals.get(0).getName());
        assertEquals("Valletta",capitals.get(1).getName());
        assertEquals("Athens",capitals.get(2).getName());

        assertEquals("Caracas",capitals.get(capitals.size()-1).getName());
        assertEquals("Georgetown",capitals.get(capitals.size()-2).getName());
        assertEquals("Paramaribo",capitals.get(capitals.size()-3).getName());

    }

    @Test
    void insertPortsAsVertex() throws FileNotFoundException {
        System.out.println("insertPortsAsVertex");
        KDTREE_PORTS KDP = new KDTREE_PORTS("src/main/java/lapr/project/files/sports.csv");
        PortsCapitalsGraph PCG = new PortsCapitalsGraph(KDP);
        PCG.createPortCapitalGraph(2);
        PCG.insertPortsAsVertex();
        Graph<IPortsAndCapitals,Double> g = PCG.getPortsCapitalsGraph();

        KDP.inOrder().forEach(port -> {
            if (PCG.getCountries().contains(PCG.getCountryByName(port.getCountry()))) {
                assertTrue(PCG.insertVertex(port));
                PCG.getCountryByName(port.getCountry()).addPort(port);
            }
        });
    }

    @Test
    void insertEdgesBetweenPorts() throws FileNotFoundException {
        System.out.println("insertEdgesBetweenPorts");
        KDTREE_PORTS KDP = new KDTREE_PORTS("src/main/java/lapr/project/files/sports.csv");
        PortsCapitalsGraph PCG = new PortsCapitalsGraph(KDP);

        int n = 2;

        for (Port p : KDP.inOrder()) {
            for (Port p2 : PCG.getClosestPortsOtherCountries(p, n)) {
                assertTrue(PCG.insertEdge(p, p2, DistancesCalculus.distance(p.getLat(), p.getLon(), p2.getLat(), p2.getLon(), "K")));
            }
        }

    }

    @Test
    void insertEdgesBetweenCapitals() throws FileNotFoundException {
        System.out.println("insertEdgesBetweenCapitals");
        KDTREE_PORTS KDP = new KDTREE_PORTS("src/main/java/lapr/project/files/sports.csv");
        PortsCapitalsGraph PCG = new PortsCapitalsGraph(KDP);

        Scanner scanner = new Scanner(new File("src/main/java/lapr/project/files/borders.csv"));
        scanner.nextLine();

        while(scanner.hasNextLine()){
            String[] itens = scanner.nextLine().split(",");
            Country country1 = PCG.getCountryByName(itens[0]);
            Country country2 = PCG.getCountryByName(itens[1]);
            if(country1 != null && country2 != null){
                assertTrue(PCG.insertEdge(country1.getCapital(), country2.getCapital(),
                        DistancesCalculus.distance(country1.getCapitalLatitude(), country1.getCapitalLongitude(),
                                country2.getCapitalLatitude(), country2.getCapitalLongitude(), "K")));
            }
        }
    }

    @Test
    void insertEdgesBetweenPortsSameCountry() throws FileNotFoundException {
        System.out.println("insertEdgesBetweenPortsSameCountry");
        KDTREE_PORTS KDP = new KDTREE_PORTS("src/main/java/lapr/project/files/sports.csv");
        PortsCapitalsGraph PCG = new PortsCapitalsGraph(KDP);

        for(Country c : PCG.getCountries()){
            for (Port p : c.getPortTree().inOrder()) {
                for (Port p2 : c.getPortTree().inOrder()) {
                    if (p.getCode().compareTo(p2.getCode()) != 0) {
                        assertTrue(PCG.insertEdge(p, p2, DistancesCalculus.distance(p.getLat(), p.getLon(),
                                p2.getLat(), p2.getLon())));
                    }

                }
            }
        }
    }

    @Test
    void insertEdgeBetweenCapitalAndClosestPortWithinCountry() {
        System.out.println("insertEdgeBetweenCapitalAndClosestPortWithinCountry");
        KDTREE_PORTS KDP = new KDTREE_PORTS("src/main/java/lapr/project/files/sports.csv");
        PortsCapitalsGraph PCG = new PortsCapitalsGraph(KDP);

        for (Country c : PCG.getCountries()) {
            if (c.getPortTree().getElementClosestToCapital() != null) {
                Port port = c.getPortTree().getElementClosestToCapital();
                double distance = DistancesCalculus.distance(c.getCapitalLatitude(), c.getCapitalLongitude(),
                        port.getLat(), port.getLon(), "K");
                assertTrue(PCG.insertEdge(c.getCapital(), port, distance));
            }
        }

    }

    @Test
    void getCountryByName() throws FileNotFoundException {
        System.out.println("getCountryByName");
        KDTREE_PORTS KDP = new KDTREE_PORTS("src/main/java/lapr/project/files/sports.csv");
        PortsCapitalsGraph PCG = new PortsCapitalsGraph(KDP);

        PCG.createPortCapitalGraph(2);
        Country country = PCG.getCountryByName("Spain");
        assertEquals("Spain",country.getCountry());
    }


    @Test
    void getPortsCapitalsGraph() throws FileNotFoundException {
        System.out.println("getPortsCapitalsGraph");
        KDTREE_PORTS KDP = new KDTREE_PORTS("src/main/java/lapr/project/files/sports.csv");
        PortsCapitalsGraph PCG = new PortsCapitalsGraph(KDP);
        PCG.createPortCapitalGraph(2);


        assertEquals("Europe",PCG.getPortsCapitalsGraph().vertices().get(0).getContinent());
        assertEquals("Europe",PCG.getPortsCapitalsGraph().vertices().get(1).getContinent());

        System.out.println(PCG.getPortsCapitalsGraph());
    }

    @Test
    void getCountryByCapital() throws FileNotFoundException {
        System.out.println("getCountryByCapital");
        KDTREE_PORTS KDP = new KDTREE_PORTS("src/main/java/lapr/project/files/sports.csv");
        PortsCapitalsGraph PCG = new PortsCapitalsGraph(KDP);

        Capital c = new Capital("Madrid",40.4,-3.683333,"Europe");
        PCG.createPortCapitalGraph(2);
        Country country = PCG.getCountryByCapital(c);
        assertEquals("Spain",country.getCountry());
    }

    @Test
    void getCapitalVertices() throws FileNotFoundException {
        System.out.println("getCapitalVertices");
        KDTREE_PORTS KDP = new KDTREE_PORTS("src/main/java/lapr/project/files/sports.csv");
        PortsCapitalsGraph PCG = new PortsCapitalsGraph(KDP);

        PCG.insertCapitals();
        ArrayList<Capital> capitais = PCG.getCapitalVertices();


        assertEquals("Nicosia",capitais.get(0).getName());
        assertEquals("Valletta",capitais.get(1).getName());

        assertEquals("Caracas",capitais.get(capitais.size()-1).getName());
        assertEquals("Georgetown",capitais.get(capitais.size()-2).getName());

    }

    @Test
    void getAdjVerticesFromCapital() throws FileNotFoundException {
        System.out.println("getAdjVerticesFromCapital");
        KDTREE_PORTS KDP = new KDTREE_PORTS("src/main/java/lapr/project/files/sports.csv");
        PortsCapitalsGraph PCG = new PortsCapitalsGraph(KDP);

        PCG.createPortCapitalGraph(2);
        Capital c = new Capital("Madrid",40.4,-3.683333,"Europe");
        Collection<Capital> adj = PCG.getAdjVerticesFromCapital(c);
        System.out.println(adj);

        Collection<Capital> adjVerticesCapital = new ArrayList<>();

        adjVerticesCapital.add(new Capital("Lisbon",38.71666667,-9.133333,"Europe"));
        adjVerticesCapital.add(new Capital("Paris",48.86666667,2.333333,"Europe"));

        assertEquals(adjVerticesCapital,adj);

    }

    @Test
    void colourTheMap() throws FileNotFoundException {
        System.out.println("colourTheMap");
        KDTREE_PORTS KDP = new KDTREE_PORTS("src/main/java/lapr/project/files/sports.csv");
        PortsCapitalsGraph PCG = new PortsCapitalsGraph(KDP);

        PCG.createPortCapitalGraph(2);
        List<Country> l = PCG.colourTheMap();

        for(int i = 0; i < l.size(); i++){
            System.out.println(l.get(i).getColour());
        }


        assertEquals(0,l.get(0).getColour());
        assertEquals(0,l.get(1).getColour());
        assertEquals(0,l.get(2).getColour());
        assertEquals(0,l.get(3).getColour());
        assertEquals(1,l.get(4).getColour());
        assertEquals(0,l.get(5).getColour());


        assertEquals(0,l.get(l.size()-1).getColour());
        assertEquals(0,l.get(l.size()-2).getColour());
        assertEquals(0,l.get(l.size()-3).getColour());
        assertEquals(2,l.get(l.size()-4).getColour());
        assertEquals(0,l.get(l.size()-5).getColour());
        assertEquals(1,l.get(l.size()-6).getColour());
    }

    @Test
    void nClosestLocalsByContinent() throws FileNotFoundException {
        System.out.println("nClosestLocalsByContinent");
        KDTREE_PORTS KDP = new KDTREE_PORTS("src/main/java/lapr/project/files/sports.csv");
        PortsCapitalsGraph PCG = new PortsCapitalsGraph(KDP);

        PCG.createPortCapitalGraph(0);
        Map<Double, IPortsAndCapitals> res =  PCG.nClosestLocalsByContinent(2,"Europe");

        List<Capital> capitals = new ArrayList<>();

        for (Map.Entry<Double, IPortsAndCapitals> entry : res.entrySet()) {
            capitals.add((Capital)entry.getValue());
        }

        System.out.println(capitals);

        assertEquals("Reykjavik",capitals.get(0).getName());
        assertEquals("Dublin",capitals.get(1).getName());

    }

    ////////////////////////////////////////////////////////////Sprint4///////////////////////////////////////////////////////////////////

    @Test
    void getNPortsGreaterCentrality() throws IOException {
        System.out.println("getNPortsGreaterCentrality");
        KDTREE_PORTS KDP = new KDTREE_PORTS("src/main/java/lapr/project/files/bports.csv");
        KDP.createKDTree();
        PortsCapitalsGraph PCG = new PortsCapitalsGraph(KDP);

        PCG.createPortCapitalGraph(0);

        Map<Port, Integer> map = PCG.getNPortsGreaterCentrality(1);
        Port result = map.entrySet().stream().findFirst().get().getKey();

        Port expected = KDP.getPortByCode("13390");

        System.out.println("Expected: " + expected + "\n");
        System.out.println("Result: " + result + "\n");

        assertEquals(expected, result);
    }

    @Test
    void fillPortsCentrality() throws IOException {
        System.out.println("fillPortsCentrality");
        KDTREE_PORTS KDP = new KDTREE_PORTS("src/main/java/lapr/project/files/bports.csv");
        KDP.createKDTree();
        PortsCapitalsGraph PCG = new PortsCapitalsGraph(KDP);

        PCG.createPortCapitalGraph(0);

        Map<Port, Integer> map = PCG.fillPortsCentrality();

        Port result = map.entrySet().stream().findFirst().get().getKey();

        Port expected =  KDP.getPortByCode("10136");

        System.out.println("Expected: " + expected);
        System.out.println("Result: " + result);

        assertEquals(expected, result);
    }
    
    
    @Test
    void shortestPathLandOrSeaPath() throws IOException {
        System.out.println("shortestPathLandOrSeaPath");
        KDTREE_PORTS KDP = new KDTREE_PORTS("src/main/java/lapr/project/files/sports.csv");
        KDP.createKDTree();
        PortsCapitalsGraph PCG = new PortsCapitalsGraph(KDP);
        PCG.createPortCapitalGraph(0);
        
        
        Port local1 = KDP.getPortByCode("18012");
        Port local2 = KDP.getPortByCode("18326");
        
        
        List<IPortsAndCapitals> path = new LinkedList<>();
        List<IPortsAndCapitals> expected = new LinkedList<>();
        
        expected.add(local1);
        expected.add(local2);
        
        path=PCG.shortestPathLandOrSeaPath(local1, local2);
        
        System.out.println(path);
        
        assertEquals(expected, path);
    
    }



    @Test
    void shortestMaritimePath() throws IOException {
        System.out.println("shortestMaritimePath");
        KDTREE_PORTS KDP = new KDTREE_PORTS("src/main/java/lapr/project/files/sports.csv");
        KDP.createKDTree();
        PortsCapitalsGraph PCG = new PortsCapitalsGraph(KDP);
        PCG.createPortCapitalGraph(0);

        Port port1 = KDP.getPortByCode("18012");
        Port port3 = KDP.getPortByCode("18326");
        IPortsAndCapitals c1 = PCG.getVerticeByName("Paris");
        IPortsAndCapitals c2 = PCG.getVerticeByName("Madrid");
        Port port4 = KDP.getPortByCode("18937");


        PCG.shortestMaritimePath(port1, port4);

        List<IPortsAndCapitals> path = new LinkedList<>();
        List<IPortsAndCapitals> expected = new LinkedList<>();

        expected.add(port1);
        expected.add(port3);
        expected.add(c1);
        expected.add(c2);
        expected.add(port4);

        path = PCG.shortestPathLandOrSeaPath(port1, port4);

        assertEquals(expected, path);

    }


    @Test
    void shortestPathLand() throws IOException {
        System.out.println("shortestPathLand");
        KDTREE_PORTS KDP = new KDTREE_PORTS("src/main/java/lapr/project/files/sports.csv");
        KDP.createKDTree();
        PortsCapitalsGraph PCG = new PortsCapitalsGraph(KDP);
        PCG.createPortCapitalGraph(0);


        Port port1 = KDP.getPortByCode("13390");
        Port port3 = KDP.getPortByCode("18937");

        IPortsAndCapitals c1 = PCG.getVerticeByName("Madrid");
        IPortsAndCapitals c2 = PCG.getVerticeByName("Lisbon");

        List<IPortsAndCapitals> res = PCG.shortestPathLand(port1,c1);

        List<IPortsAndCapitals> expected = new LinkedList<>();

        expected.add(port1);
        expected.add(c2);
        expected.add(c1);

        assertEquals(expected, res);

    }

    @Test
    void shortestPathPassingOnN() throws IOException {
        System.out.println("shortestPathPassingOnN");
        KDTREE_PORTS KDP = new KDTREE_PORTS("src/main/java/lapr/project/files/sports.csv");
        KDP.createKDTree();
        PortsCapitalsGraph PCG = new PortsCapitalsGraph(KDP);
        PCG.createPortCapitalGraph(3);



        IPortsAndCapitals c1 = PCG.getVerticeByName("London");
        IPortsAndCapitals c2 = PCG.getVerticeByName("Luxembourg");
        IPortsAndCapitals c3 = PCG.getVerticeByName("Bern");

        IPortsAndCapitals c4 = PCG.getVerticeByName("Lisbon");
        IPortsAndCapitals c5 = PCG.getVerticeByName("Sofia");


        LinkedList<IPortsAndCapitals> expected = new LinkedList<>();

        expected.add(c1);
        expected.add(c2);
        expected.add(c3);

        List<IPortsAndCapitals> res = PCG.shortestPathPassingOnN(expected,c4,c5);

        IPortsAndCapitals capital = PCG.getVerticeByName("Lisbon");
        Port Port = KDP.getPortByCode("13390");
        Port p2 = KDP.getPortByCode("18012");
        Port p3 = KDP.getPortByCode("29002");
        IPortsAndCapitals capital1 = PCG.getVerticeByName("London");
        IPortsAndCapitals capital2 = PCG.getVerticeByName("London");
        Port p4 = KDP.getPortByCode("29002");
        Port p5 = KDP.getPortByCode("18326");
        IPortsAndCapitals capital3 = PCG.getVerticeByName("Paris");
        IPortsAndCapitals capital4 = PCG.getVerticeByName("Luxembourg");
        IPortsAndCapitals capital5 = PCG.getVerticeByName("Luxembourg");
        IPortsAndCapitals capital6 = PCG.getVerticeByName("Paris");
        IPortsAndCapitals capital7 = PCG.getVerticeByName("Bern");
        IPortsAndCapitals capital8 = PCG.getVerticeByName("Bern");
        IPortsAndCapitals capital9 = PCG.getVerticeByName("Vienna");
        IPortsAndCapitals capital10 = PCG.getVerticeByName("Budapest");
        IPortsAndCapitals capital11 = PCG.getVerticeByName("Belgrade");
        IPortsAndCapitals capital12 = PCG.getVerticeByName("Sofia");


        List<IPortsAndCapitals> l = new ArrayList<>();

        l.add(capital);
        l.add(Port);
        l.add(p2);
        l.add(p3);
        l.add(capital1);
        l.add(capital2);
        l.add(p4);
        l.add(p5);
        l.add(capital3);
        l.add(capital4);
        l.add(capital5);
        l.add(capital6);
        l.add(capital7);
        l.add(capital8);
        l.add(capital9);
        l.add(capital10);
        l.add(capital11);
        l.add(capital12);


        assertEquals(l, res);

    }
    

    @Test
    void hamCycle() throws IOException{
        
        System.out.println("testHamCycle");
        KDTREE_PORTS KDP = new KDTREE_PORTS("src/main/java/lapr/project/files/bports.csv");
        KDP.createKDTree();
        PortsCapitalsGraph PCG = new PortsCapitalsGraph(KDP);
        PCG.createPortCapitalGraph(4);
        
        
        IPortsAndCapitals startingLocal = KDP.getPortByCode("10136");
        
        IPortsAndCapitals local1 = KDP.getPortByCode("10136");
        IPortsAndCapitals local2 = KDP.getPortByCode("22522");
        IPortsAndCapitals local3 = KDP.getPortByCode("246265");

        
        List<IPortsAndCapitals> result = new LinkedList<>();
        List<IPortsAndCapitals> expected = new LinkedList<>();
        
        expected.add(local1);
        expected.add(local2);
        expected.add(local3);
        
       
        result=PCG.hamCycle(startingLocal);
        
        assertEquals(result, expected);
        
        
    }
}