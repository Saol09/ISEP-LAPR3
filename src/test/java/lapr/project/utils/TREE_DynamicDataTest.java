/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.utils;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.util.Pair;
import lapr.project.model.Ship;
import lapr.project.model.ShipDynamicData;
import lapr.project.model.Ship_CallSign;
import lapr.project.model.Ship_MMSI;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author andre
 */
public class TREE_DynamicDataTest {

    private Ship ship;
    private TREE_SHIPS_MMSI tree_mmsi;
    private TREE_SHIPS_CALLSIGN tree_ships_callsign;

    @Test
    public void getInicioPartidaDateTest() throws FileNotFoundException {
        
        System.out.println("inicioPartida");
        tree_mmsi= new TREE_SHIPS_MMSI(new Ship_MMSI());
        tree_mmsi.createTree();
        ship = tree_mmsi.getElementByIdenfication("210950000");            
        
        LocalDate date = LocalDate.of(2020, 12, 31);
        LocalTime time = LocalTime.of(16, 4);
        LocalDateTime expected = LocalDateTime.of(date, time);
        System.out.println("expected:"+expected);
        
        LocalDateTime result=ship.getDynamicDataTree().getInicioPartidaDate();
        
        assertEquals(expected, result);
    }

    @Test
    void getFimPartidaTest() throws FileNotFoundException {
        System.out.println("fimPartida");
        tree_mmsi= new TREE_SHIPS_MMSI(new Ship_MMSI());
        tree_mmsi.createTree();
        ship = tree_mmsi.getElementByIdenfication("210950000");

        LocalDate date = LocalDate.of(2020, 12, 31);
        LocalTime time = LocalTime.of(18, 31);
        LocalDateTime expected = LocalDateTime.of(date, time);
        System.out.println("expected:"+expected);

        LocalDateTime result=ship.getDynamicDataTree().getFimPartida();

        assertEquals(expected, result);
    }

    @Test
    void getNumeroTotalMovimentosTest() throws FileNotFoundException {
        System.out.println("NumeroTotalMovimentos");
        tree_mmsi= new TREE_SHIPS_MMSI(new Ship_MMSI());
        tree_mmsi.createTree();
        ship = tree_mmsi.getElementByIdenfication("257881000");

        Integer expected = 6;
        System.out.println("expected:"+expected);

        Integer result = ship.getDynamicDataTree().getNumeroTotalMovimentos();

        assertEquals(expected, result);
    }

    @Test
    void getMaxSogTest() {
        System.out.println("MaxSog");

        Ship_MMSI ship_mmsi1 = new Ship_MMSI("CONTI LYON", 79,  300, 40, 9.2, "636019825");

        ship_mmsi1.addToDynamicDataTree(new ShipDynamicData(LocalDateTime.of(2020,12, 31, 23,48) , 54.28232, -164.1642, 9.8, -122.7, 274, "79", "B"));
        ship_mmsi1.addToDynamicDataTree(new ShipDynamicData(LocalDateTime.of(2020,12, 31, 23,42) , 54.28012, -164.13995, 9.9, -125.1, 274, "79", "B"));
        ship_mmsi1.addToDynamicDataTree(new ShipDynamicData(LocalDateTime.of(2020,12, 31, 23,38) , 54.27817, -164.12098, 9.7, -129.9, 275, "79", "B"));

        double expected = 9.9;
        System.out.println("expected:"+expected);

        double result = ship_mmsi1.getDynamicDataTree().getMaxSog();
        System.out.println("result:"+result);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void getMeanSogTest() {
        System.out.println("MeanSog");

        Ship_MMSI ship_mmsi1 = new Ship_MMSI("CONTI LYON", 79,  300, 40, 9.2, "636019825");

        ship_mmsi1.addToDynamicDataTree(new ShipDynamicData(LocalDateTime.of(2020,12, 31, 23,48) , 54.28232, -164.1642, 9.8, -122.7, 274, "79", "B"));
        ship_mmsi1.addToDynamicDataTree(new ShipDynamicData(LocalDateTime.of(2020,12, 31, 23,42) , 54.28012, -164.13995, 9.9, -125.1, 274, "79", "B"));
        ship_mmsi1.addToDynamicDataTree(new ShipDynamicData(LocalDateTime.of(2020,12, 31, 23,38) , 54.27817, -164.12098, 9.7, -129.9, 275, "79", "B"));

        double expected = 9.8;
        System.out.println("expected:"+expected);

        double result = ship_mmsi1.getDynamicDataTree().getMeanSog();
        System.out.println("result:"+result);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void getMeanSogBetweenDatesTest() {
        System.out.println("MeanSogBetweenDates");

        Ship_MMSI ship_mmsi1 = new Ship_MMSI("CONTI LYON", 79,  300, 40, 9.2, "636019825");

        ship_mmsi1.addToDynamicDataTree(new ShipDynamicData(LocalDateTime.of(2020,12, 31, 23,48) , 54.28232, -164.1642, 9.8, -122.7, 274, "79", "B"));
        ship_mmsi1.addToDynamicDataTree(new ShipDynamicData(LocalDateTime.of(2020,12, 31, 23,42) , 54.28012, -164.13995, 9.9, -125.1, 274, "79", "B"));
        ship_mmsi1.addToDynamicDataTree(new ShipDynamicData(LocalDateTime.of(2020,12, 31, 23,38) , 54.27817, -164.12098, 9.7, -129.9, 275, "79", "B"));
        ship_mmsi1.addToDynamicDataTree(new ShipDynamicData(LocalDateTime.of(2020,12, 31, 23,16) , 54.2674, -164.02421, 9.5, -129.4, 276, "79", "B"));
        ship_mmsi1.addToDynamicDataTree(new ShipDynamicData(LocalDateTime.of(2020,12, 31, 23,22) , 54.27031, -164.04899, 9.6, -125.5, 277, "79", "B"));

        LocalDate date = LocalDate.of(2020, 12, 31);
        LocalTime time = LocalTime.of(23, 20);
        LocalDateTime dataInicial = LocalDateTime.of(date, time);

        LocalDate date1 = LocalDate.of(2020, 12, 31);
        LocalTime time1 = LocalTime.of(23, 45);
        LocalDateTime dataFinal = LocalDateTime.of(date1, time1);

        double expected = 9.7;
        System.out.println("expected:"+expected);

        double result = ship_mmsi1.getDynamicDataTree().getMeanSogBetweenDates(dataInicial, dataFinal);
        DecimalFormat df = new DecimalFormat("0.0");
        System.out.println("result:"+df.format(result));

        Assertions.assertEquals(df.format(expected), df.format(result));
    }

    @Test
    void getMaxCogTest() {
        System.out.println("MaxCog");

        Ship_MMSI ship_mmsi1 = new Ship_MMSI("CONTI LYON", 79,  300, 40, 9.2, "636019825");

        ship_mmsi1.addToDynamicDataTree(new ShipDynamicData(LocalDateTime.of(2020,12, 31, 23,48) , 54.28232, -164.1642, 9.8, -122.7, 274, "79", "B"));
        ship_mmsi1.addToDynamicDataTree(new ShipDynamicData(LocalDateTime.of(2020,12, 31, 23,42) , 54.28012, -164.13995, 9.9, -125.1, 274, "79", "B"));
        ship_mmsi1.addToDynamicDataTree(new ShipDynamicData(LocalDateTime.of(2020,12, 31, 23,38) , 54.27817, -164.12098, 9.7, -129.9, 275, "79", "B"));

        double expected = -122.7;
        System.out.println("expected:"+expected);

        double result = ship_mmsi1.getDynamicDataTree().getMaxCog();
        System.out.println("result:"+result);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void getMeanCogTest() {
        System.out.println("MeanCog");

        Ship_MMSI ship_mmsi1 = new Ship_MMSI("CONTI LYON", 79,  300, 40, 9.2, "636019825");

        ship_mmsi1.addToDynamicDataTree(new ShipDynamicData(LocalDateTime.of(2020,12, 31, 23,48) , 54.28232, -164.1642, 9.8, -122.7, 274, "79", "B"));
        ship_mmsi1.addToDynamicDataTree(new ShipDynamicData(LocalDateTime.of(2020,12, 31, 23,42) , 54.28012, -164.13995, 9.9, -125.1, 274, "79", "B"));
        ship_mmsi1.addToDynamicDataTree(new ShipDynamicData(LocalDateTime.of(2020,12, 31, 23,38) , 54.27817, -164.12098, 9.7, -129.9, 275, "79", "B"));

        double expected = -125.9;
        System.out.println("expected:"+expected);

        double result = ship_mmsi1.getDynamicDataTree().getMeanCog();
        DecimalFormat df = new DecimalFormat("0.0");
        System.out.println("result:"+df.format(result));

        Assertions.assertEquals( df.format(expected), df.format(result));
    }

    @Test
    void getDepartureLatitudeTest() throws FileNotFoundException {
        System.out.println("DepartureLatitude");
        tree_mmsi= new TREE_SHIPS_MMSI(new Ship_MMSI());
        tree_mmsi.createTree();
        ship = tree_mmsi.getElementByIdenfication("210950000");

        double expected = 42.71055;
        System.out.println("expected:"+expected);

        double result = ship.getDynamicDataTree().getDepartureLatitude();
        System.out.println("result:"+result);

        Assertions.assertEquals(expected, result);

    }

    @Test
    void getDepartureLongitudeTest() throws FileNotFoundException {
        System.out.println("DepartureLongitude");
        tree_mmsi= new TREE_SHIPS_MMSI(new Ship_MMSI());
        tree_mmsi.createTree();
        ship = tree_mmsi.getElementByIdenfication("210950000");

        double expected = -66.97776;
        System.out.println("expected:"+expected);

        double result = ship.getDynamicDataTree().getDepartureLongitude();
        System.out.println("result:"+result);

        Assertions.assertEquals(expected, result);

    }

    @Test
    void getArrivalLatitudeTest() throws FileNotFoundException {
        System.out.println("ArrivalLatitude");
        tree_mmsi= new TREE_SHIPS_MMSI(new Ship_MMSI());
        tree_mmsi.createTree();
        ship = tree_mmsi.getElementByIdenfication("210950000");

        double expected = 43.22513;
        System.out.println("expected:"+expected);

        double result = ship.getDynamicDataTree().getArrivalLatitude();
        System.out.println("result:"+result);

        Assertions.assertEquals(expected, result);

    }

    @Test
    void getArrivalLongitudeTest() throws FileNotFoundException {
        System.out.println("ArrivalLongitude");
        tree_mmsi= new TREE_SHIPS_MMSI(new Ship_MMSI());
        tree_mmsi.createTree();
        ship = tree_mmsi.getElementByIdenfication("210950000");

        double expected = -66.96725;
        System.out.println("expected:"+expected);

        double result = ship.getDynamicDataTree().getArrivalLongitude();
        System.out.println("result:"+result);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void getCoordsByData() throws FileNotFoundException{
        System.out.println("CoordsByData");
        tree_ships_callsign = new TREE_SHIPS_CALLSIGN(new Ship_CallSign());
        tree_ships_callsign.createTree();

        ship = tree_ships_callsign.getElementByIdenfication("C4SQ2");

        Pair<Double, Double> expected = new Pair<>(42.97875, -66.97001);
        System.out.println("expected:"+expected);

        LocalDate date1 = LocalDate.of(2020, 12, 31);
        LocalTime time1 = LocalTime.of(17, 19);
        LocalDateTime data = LocalDateTime.of(date1, time1);

        Pair<Double, Double> result = ship.getDynamicDataTree().getCoordsByData(data);
        System.out.println("result:"+result);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void getTravelledDistanceTest() throws FileNotFoundException {
        System.out.println("TravelledDistance");
        tree_mmsi= new TREE_SHIPS_MMSI(new Ship_MMSI());
        tree_mmsi.createTree();
        ship = tree_mmsi.getElementByIdenfication("212180000");

        double expected = 54.07;
        System.out.println("expected:"+expected);

        double result = ship.getDynamicDataTree().getTravelledDistance();
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println("result:"+df.format(result));

        Assertions.assertEquals(df.format(expected), df.format(result));
    }

    @Test
    void getTravelledDistanceBetweenDatesTest() throws FileNotFoundException {
        System.out.println("TravelledDistanceBetweenDates");
        tree_mmsi= new TREE_SHIPS_MMSI(new Ship_MMSI());
        tree_mmsi.createTree();
        ship = tree_mmsi.getElementByIdenfication("212180000");

        LocalDate date = LocalDate.of(2020, 12, 31);
        LocalTime time = LocalTime.of(20, 0);
        LocalDateTime dataInicial = LocalDateTime.of(date, time);

        LocalDate date1 = LocalDate.of(2020, 12, 31);
        LocalTime time1 = LocalTime.of(22, 0);
        LocalDateTime dataFinal = LocalDateTime.of(date1, time1);

        double expected = 32.29;
        System.out.println("expected:"+expected);

        double result = ship.getDynamicDataTree().getTravelledDistanceBetweenDates(dataInicial, dataFinal);
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println("result:"+df.format(result));

        Assertions.assertEquals(df.format(expected), df.format(result));
    }

    @Test
    void getDeltaDistanceTest() throws FileNotFoundException {
        System.out.println("DeltaDistance");
        tree_mmsi= new TREE_SHIPS_MMSI(new Ship_MMSI());
        tree_mmsi.createTree();
        ship = tree_mmsi.getElementByIdenfication("212180000");

        double expected = 54.03;
        System.out.println("expected:"+expected);

        double result = ship.getDynamicDataTree().getDeltaDistance();
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println("result:"+df.format(result));

        Assertions.assertEquals(df.format(expected), df.format(result));
    }

    @Test
    void hasTwoMovementsBetweenDatesTest() throws FileNotFoundException {
        System.out.println("TwoMovementsBetweenDates");
        tree_mmsi= new TREE_SHIPS_MMSI(new Ship_MMSI());
        tree_mmsi.createTree();
        ship = tree_mmsi.getElementByIdenfication("212180000");

        LocalDate date = LocalDate.of(2020, 12, 31);
        LocalTime time = LocalTime.of(20, 0);
        LocalDateTime dataInicial = LocalDateTime.of(date, time);

        LocalDate date1 = LocalDate.of(2020, 12, 31);
        LocalTime time1 = LocalTime.of(21, 30);
        LocalDateTime dataFinal = LocalDateTime.of(date1, time1);

        boolean result = ship.getDynamicDataTree().hasTwoMovementsBetweenDates(dataInicial, dataFinal);

        Assertions.assertTrue(result);
    }
}
