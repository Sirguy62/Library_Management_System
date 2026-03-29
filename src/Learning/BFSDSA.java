public class BFSDSA {
    public static void main(String[] args){

        //Depth First Search = Pick a route, keep going,
        //                     if you rach a dead end, or an already visited node,
        //                     backtrack to a previous node with unvisited adjacent neighbors

        BFSGraph bfsGraph = new BFSGraph(5);

        bfsGraph.addNode(new Node('A'));
        bfsGraph.addNode(new Node('B'));
        bfsGraph.addNode(new Node('C'));
        bfsGraph.addNode(new Node('D'));
        bfsGraph.addNode(new Node('E'));

        bfsGraph.addEdge(0, 1);
        bfsGraph.addEdge(1, 2);
        bfsGraph.addEdge(1, 4);
        bfsGraph.addEdge(2, 3);
        bfsGraph.addEdge(2, 4);
        bfsGraph.addEdge(4, 0);
        bfsGraph.addEdge(4, 2);

        bfsGraph.print();
        bfsGraph.breadthFirstSearch(0);
    }
}
