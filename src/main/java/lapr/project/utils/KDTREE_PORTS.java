package lapr.project.utils;

import lapr.project.model.Port;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * The type Kdtree ports.
 */
public class KDTREE_PORTS extends KDTREE<Port> {

    /**
     * The Document path.
     */
    public String documentPath;

    private final Comparator<Port> cmpX = (o1, o2) -> Double.compare(o1.getLat(), o2.getLat());

    private final Comparator<Port> cmpY = (o1, o2) -> Double.compare(o1.getLon(), o2.getLon());


    /**
     * Instantiates a new Kdtree ports.
     */
    public KDTREE_PORTS() {
        documentPath = "src/main/java/lapr/project/files/bports.csv";
    }

    /**
     * Instantiates a new Kdtree ports.
     *
     * @param path the path
     */
    public KDTREE_PORTS(String path) {
        this.documentPath = path;
    }

    /**
     * creaacao da arvore 2dTree de ports
     *
     * @throws IOException the io exception
     */
    public void createKDTree() throws IOException {
        Scanner sc = new Scanner(new File(documentPath));
        sc.nextLine();  // skip first line

        List<Port> listPortInsertion = new LinkedList<>();
        while (sc.hasNextLine()) {
            String[] itens = sc.nextLine().split(",");

            Port port = new Port(itens[0], itens[1], itens[2],
                    itens[3], Double.parseDouble(itens[4]),
                    Double.parseDouble(itens[5]));
            listPortInsertion.add(port);
        }

        insertNodes(listPortInsertion, true);

    }

    /**
     * metodo de insercao de modo a que fique balanceada
     *
     * @param list list
     * @param divx divx
     */
    public void insertNodes(List<Port> list, boolean divx) {
        if (list.size() == 0) return;

        Collections.sort(list, divx ? cmpX : cmpY);

        Port port = list.get(list.size() / 2);
        int index = list.indexOf(port);
        Node2D<Port> portNode2D = new Node2D<Port>(port, null, null, port.getLat(), port.getLon());

        super.insert(portNode2D);

        List<Port> list1 = new ArrayList<>();
        List<Port> list2 = new ArrayList<>();
        for (int i = 0; i < index; i++)
            list1.add(list.get(i));

        for (int i = index + 1; i < list.size(); i++)
            list2.add(list.get(i));

        insertNodes(list1, !divx);
        insertNodes(list2, !divx);

    }

    /**
     * Get port by code port.
     *
     * @param code the code
     * @return the port
     */
    public Port getPortByCode(String code){
       for(Port port : this.inOrder()){
           if(port.getCode().trim().compareTo(code.trim()) == 0 ) return port;
       }

       return null;
    }

}
