package lapr.project.utils;

import lapr.project.model.Ship;
import lapr.project.model.Ship_MMSI;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;

import lapr.project.model.Ship_CallSign;
import lapr.project.model.Ship_IMO;

import static org.junit.Assert.assertEquals;

import static org.junit.jupiter.api.Assertions.*;

class SUPERTREETest {

    private Ship_MMSI instance;
    private TREE_DynamicData dynamicData;
    private TREE_SHIPS_MMSI tree_mmsi;

    /**TODO METER BEFORE
     * Test of createTree method, of class SUPERTREE.
     */
    @Test
    public void testCreateMMSITree() throws Exception {
        System.out.println("createMMSITree");
        TREE_SHIPS_MMSI instance = new TREE_SHIPS_MMSI(new Ship_MMSI());
        instance.createTree();
        System.out.println(instance);
    }

    /**
     * Test of createTree method, of class SUPERTREE.
     */
    @Test
    public void testCreateIMOTree() throws Exception {
        System.out.println("createImoTree");
        TREE_SHIPS_IMO instance = new TREE_SHIPS_IMO(new Ship_IMO());
        instance.createTree();
        System.out.println(instance);
    }


    /**
     * Test of createTree method, of class SUPERTREE.
     */
    @Test
    public void testCreateCallSignTree() throws Exception {
        System.out.println("createCallSignTree");
        TREE_SHIPS_CALLSIGN instance = new TREE_SHIPS_CALLSIGN(new Ship_CallSign());
        instance.createTree();
        System.out.println(instance);
    }

    @Test
    void createTree() throws FileNotFoundException {
        Ship_MMSI s = new Ship_MMSI("Ship1", 12, 234, 80, 40, "366934280");
        SUPERTREE<Ship_MMSI> shipTree = new SUPERTREE<>(s);
        shipTree.createTree();
    }


    @Test
    public void getElementByIdenficationTest() throws FileNotFoundException {
        tree_mmsi = new TREE_SHIPS_MMSI(new Ship_MMSI());
        tree_mmsi.createTree();
        String result;
        String expectedShip = new Ship_MMSI("VARAMO", 70, 166, 25.0, 9.5, "210950000").toString();
        result = tree_mmsi.getElementByIdenfication("210950000").toString();

        assertEquals(expectedShip, result);
    }


//    @Test
//    void topNTest() throws FileNotFoundException {
//        System.out.println("topN");
//
//        LocalDateTime dataInicial = LocalDateTime.of(2020,12,31,9,5);
//        LocalDateTime dataFinal = LocalDateTime.of(2020,12,31,23,41);
//
//        TREE_SHIPS_MMSI tree_ships_mmsi = new TREE_SHIPS_MMSI(new Ship_MMSI(),"src/main/java/lapr/project/files/sships.csv");
//        tree_ships_mmsi.createTree();
//
//        Map<Ship, Double> expected = new LinkedHashMap<>();
//
//        Ship ship1 = tree_ships_mmsi.getElementByIdenfication("229857000");
//        tree_ships_mmsi.getElementByIdenfication(ship1.getCode()).getDynamicDataTree().inOrder().forEach(ship1::addToDynamicDataTree);
//
//        Ship ship2 = tree_ships_mmsi.getElementByIdenfication("249047000");
//        tree_ships_mmsi.getElementByIdenfication(ship2.getCode()).getDynamicDataTree().inOrder().forEach(ship2::addToDynamicDataTree);
//
//        Double media1 = ship1.getMeanSogBetweenDates(dataInicial, dataFinal);
//        Double media2 = ship2.getMeanSogBetweenDates(dataInicial, dataFinal);
//
//        expected.put(ship1, media1);
//        expected.put(ship2, media2);
//        System.out.println("Expected:" + expected);
//
//        System.out.println("------------------------------------------------------------------------------");
//
//        Map<Ship, Double> result = tree_ships_mmsi.topN(2, dataInicial, dataFinal, 60);
//        System.out.println("Result:" + result);
//
//        Assert.assertEquals(expected, result);
//    }

    @Test
    void shipsMovementsAndDistance() throws FileNotFoundException {
         System.out.println("ShipsMovemntAndDistance");
        
        Map<String, List<Double>> expected = new LinkedHashMap<>();
        Map<String, List<Double>> result = new LinkedHashMap<>();
        List<Double> values = new ArrayList<>();
        
        TREE_SHIPS_MMSI tree_ships_mmsi = new TREE_SHIPS_MMSI(new Ship_MMSI(),"src/main/java/lapr/project/files/sship.csv");
        tree_ships_mmsi.createTree();
        
        
        result=tree_ships_mmsi.shipsMovementsAndDistance();
        expected.put("305176000", new ArrayList<>());
        expected.put("229961000", new ArrayList<>());
        expected.put("305176002", new ArrayList<>());
        expected.put("305373000", new ArrayList<>());
        expected.put("305176001", new ArrayList<>());
        
        
        expected.get("305373000").add(1.0);
        expected.get("305373000").add(0.0);
        expected.get("305373000").add(0.0);
        
        expected.get("305176000").add(4.0);
        expected.get("305176000").add(1.5265730211327375);
        expected.get("305176000").add(1.5265542538517383);
        
        expected.get("229961000").add(2.0);
        expected.get("229961000").add(0.023177489016698367);
        expected.get("229961000").add(0.023177489016698367);
        
        expected.get("305176002").add(1.0);
        expected.get("305176002").add(0.0);
        expected.get("305176002").add(0.0);
        
        expected.get("305176001").add(2.0);
        expected.get("305176001").add(0.0);
        expected.get("305176001").add(0.0);
        
        
        assertEquals(result,expected);
        
    }


    @Test
    void paresNaviosTest() throws FileNotFoundException {

        TREE_SHIPS_MMSI tree_ships_mmsi = new TREE_SHIPS_MMSI(new Ship_MMSI(),"src/main/java/lapr/project/files/bships.csv");
        tree_ships_mmsi.createTree();

        List<Map.Entry<Ship, Ship>> result = tree_ships_mmsi.naviosPares();


        assertEquals(result.get(0).getKey().getCode(), "316018851");
        assertEquals(result.get(0).getValue().getCode(), "316028554");

        assertEquals(result.get(1).getKey().getCode(), "366998510");
        assertEquals(result.get(1).getValue().getCode(), "367122220");

        assertEquals(result.get(2).getKey().getCode(), "366772760");
        assertEquals(result.get(2).getValue().getCode(), "366772990");
        
    }  


}