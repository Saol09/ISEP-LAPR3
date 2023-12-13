package lapr.project.utils;

import lapr.project.model.Port;
import lapr.project.model.Ship;
import lapr.project.model.Ship_MMSI;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class KDTREE_PORTSTest {

    @Test
    void createKDTree() throws IOException {

        KDTREE_PORTS KDP = new KDTREE_PORTS("src/main/java/lapr/project/files/sports.csv");
        KDP.createKDTree();

    }

    @Test
    void insertNodes() {
        KDTREE_PORTS KDP = new KDTREE_PORTS("src/main/java/lapr/project/files/sports.csv");

        Port p1 = new Port("Europe","Cyprus","10136","Larnaca",34.91666667,33.65);
        Port p2 = new Port("America","Argentina","14113","Bahia Blanca",-38.78333333,-62.26666667);
        Port p3 = new Port("America","Panama","216593","Balboa",8.966666667,-79.56666667);

        List<Port> list = new LinkedList<>();
        list.add(p1);
        list.add(p2);
        list.add(p3);

        KDP.insertNodes(list,true);
    }
}