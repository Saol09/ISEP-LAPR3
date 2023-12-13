package lapr.project.utils;

import lapr.project.model.Port;
import lapr.project.model.Ship;
import lapr.project.model.Ship_MMSI;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class KDTREETest {

//    @Test
//    void root() throws IOException {
//        KDTREE_PORTS kdtree_ports = new KDTREE_PORTS("src/main/java/lapr/project/files/sports.csv");
//        kdtree_ports.createKDTree();
//
//        KDTREE.Node2D result = kdtree_ports.root();
//
//        assertEquals(result.element.toString(),"Port{continent='Europe', country='Portugal', code='18476', port='Ponta Delgada', lat=37.73333333, lon=-25.66666667}");
//    }

    @Test
    void isEmpty() throws IOException {
        KDTREE_PORTS kdtree_ports = new KDTREE_PORTS("src/main/java/lapr/project/files/sports.csv");
        kdtree_ports.createKDTree();

        boolean result = kdtree_ports.isEmpty();
        assertEquals(result, false);
    }

    @Test
    void height() throws IOException {
        KDTREE_PORTS kdtree_ports = new KDTREE_PORTS("src/main/java/lapr/project/files/sports.csv");
        kdtree_ports.createKDTree();

        int result = kdtree_ports.height();
        assertEquals(result, 4);
    }


    @Test
    void insert() throws IOException {
        KDTREE_PORTS kdtree_ports = new KDTREE_PORTS("src/main/java/lapr/project/files/sports.csv");
        kdtree_ports.createKDTree();

        KDTREE.Node2D node = new KDTREE.Node2D(null,null,null,54.15,12.1);
        kdtree_ports.insert(node);
    }

    @Test
    void numberOfNodes() throws IOException {

        KDTREE_PORTS kdtree_ports = new KDTREE_PORTS("src/main/java/lapr/project/files/sports.csv");
        kdtree_ports.createKDTree();

        int result = kdtree_ports.numberOfNodes();

        assertEquals(result, 22);

    }

    @Test
    void findNearestNeighbour() throws IOException {
        KDTREE_PORTS kdtree_ports = new KDTREE_PORTS("src/main/java/lapr/project/files/sports.csv");
        kdtree_ports.createKDTree();

        Port result = kdtree_ports.findNearestNeighbour(33.95038,-119.08169);

        assertEquals(result.getPort(), "Los Angeles");
        assertEquals(result.getLat().toString(), "33.71666667");
        assertEquals(result.getLon().toString(), "-118.2666667");

    }

    @Test
    void verifyBalanced() throws IOException {
        KDTREE_PORTS kdtree_ports = new KDTREE_PORTS("src/main/java/lapr/project/files/sports.csv");
        kdtree_ports.createKDTree();

        boolean result = kdtree_ports.verifyBalanced();

        assertEquals(result, true);
    }
}