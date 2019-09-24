package app;

import structures.Graph;
import structures.Arc;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class used for testing the MST
 */
public class Driver {

    public static void main(String[] args) {
        Graph graph = null;
        try {
            graph = new Graph("graph1.txt");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        //
        PartialTreeList partialTreeList = PartialTreeList.initialize(graph);
        //
        ArrayList<Arc> arcArrayList = PartialTreeList.execute(partialTreeList);
        //

        for (int i = 0; i < arcArrayList.size(); i++) {
            Arc anArcArrayList = arcArrayList.get(i);
            System.out.println(anArcArrayList);
        }
    }
}