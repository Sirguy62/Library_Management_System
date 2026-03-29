public class DFSDSA {
    public static void main(String[] args){

        //Depth First Search = Pick a route, keep going,
        //                     if you rach a dead end, or an already visited node,
        //                     backtrack to a previous node with unvisited adjacent neighbors

        DFSGraph dfsGraph = new DFSGraph(5);

        dfsGraph.addNode(new Node('A'));
        dfsGraph.addNode(new Node('B'));
        dfsGraph.addNode(new Node('C'));
        dfsGraph.addNode(new Node('D'));
        dfsGraph.addNode(new Node('E'));

        dfsGraph.addEdge(0, 1);
        dfsGraph.addEdge(1, 2);
        dfsGraph.addEdge(1, 4);
        dfsGraph.addEdge(2, 3);
        dfsGraph.addEdge(2, 4);
        dfsGraph.addEdge(4, 0);
        dfsGraph.addEdge(4, 2);

        dfsGraph.print();
        dfsGraph.depthFirstSearch(0);
    }
}
