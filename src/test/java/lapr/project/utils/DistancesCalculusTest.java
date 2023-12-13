package lapr.project.utils;

import lapr.project.model.CalculatorExample;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DistancesCalculusTest {

    @Test
    void distanceTestK() {
        double lat1 = 42.97875;
        double lat2 = 54.27718;
        double long1 = -66.97001;
        double long2 = -164.11121;

        double distance = DistancesCalculus.distance(lat1,long1,lat2,long2,"K");
        double result = Math.floor(distance * 100) / 100;
        double expected = 6668.73;
        assertEquals(expected, result);
    }

    @Test
    void distanceTestK1() {
        double lat1 = 34.23331;
        double lat2 = 23.79429;
        double long1 = -120.11433;
        double long2 = -81.80324;

        double distance = DistancesCalculus.distance(lat1,long1,lat2,long2,"K");
        double result = Math.floor(distance * 100) / 100;
        double expected = 3876.13;
        assertEquals(expected, result);
    }

    @Test
    void distanceTestN() {
        double lat1 = 42.97875;
        double lat2 = 54.27718;
        double long1 = -66.97001;
        double long2 = -164.11121;

        double distance = DistancesCalculus.distance(lat1,long1,lat2,long2,"N");
        double result = Math.floor(distance * 100) / 100;
        double expected = 3598.44;
        assertEquals(expected, result);
    }

    @Test
    void distanceTestN1() {
        double lat1 = 34.23331;
        double lat2 = 23.79429;
        double long1 = -120.11433;
        double long2 = -81.80324;

        double distance = DistancesCalculus.distance(lat1,long1,lat2,long2,"N");
        double result = Math.floor(distance * 100) / 100;
        double expected = 2091.55;
        assertEquals(expected, result);
    }
}