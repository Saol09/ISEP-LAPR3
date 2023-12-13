/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import lapr.project.model.Port;
import lapr.project.utils.KDTREE_PORTS;
import lapr.project.utils.graph.Algorithms;
import lapr.project.utils.graph.Graph;
import lapr.project.utils.graph.matrix.MatrixGraph;
import lapr.project.utils.portsCapitalsGraph.IPortsAndCapitals;
import lapr.project.utils.portsCapitalsGraph.PortsCapitalsGraph;

/**
 *
 * @author andre
 */
public class NewMain {

//    /**
//     * @param args the command line arguments
//     */
//    private static Graph<IPortsAndCapitals, Double> portsCapitalsGraph;
//    public static void main(String[] args) throws FileNotFoundException, IOException {
//
//
//        System.out.println("test");
//        KDTREE_PORTS KDP = new KDTREE_PORTS("src/main/java/lapr/project/files/bports.csv");
//        KDP.createKDTree();
//        PortsCapitalsGraph PCG = new PortsCapitalsGraph(KDP);
//        PCG.createPortCapitalGraph(4);
//
//
//
//
//        IPortsAndCapitals local1 = PCG.getCountryByName("Denmark").getCapital();
//        IPortsAndCapitals local2 = KDP.getPortByCode("10358");
//        IPortsAndCapitals local3 = KDP.getPortByCode("10136");
//        IPortsAndCapitals local4 = PCG.getCountryByName("Netherlands").getCapital();
//
//        LinkedList<IPortsAndCapitals> path = new LinkedList<>();
//
//
//        PCG.hamCycle(local3);
//
//
//    }
    
}
