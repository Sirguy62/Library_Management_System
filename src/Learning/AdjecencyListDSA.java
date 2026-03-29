public class AdjecencyListDSA {
    public static void main(String[] args){

        // Adjacency List = An array/arraylist of linkedlists.
        //                  Each LinkedList has a unique node at the head.
        //                  All adjacent neighbors to that node are added to that node's linkedlist
        //
        //                  runtime complexity to check an Edge: O(v)
        //                  space complexity: O(v + e)

        GraphAd graphAd = new GraphAd();

        graphAd.addNode(new Node('A'));
        graphAd.addNode(new Node('B'));
        graphAd.addNode(new Node('C'));
        graphAd.addNode(new Node('D'));
        graphAd.addNode(new Node('E'));

        graphAd.addEdge(0, 1);
        graphAd.addEdge(1, 2);
        graphAd.addEdge(1, 4);
        graphAd.addEdge(2, 3);
        graphAd.addEdge(2, 4);
        graphAd.addEdge(4, 0);
        graphAd.addEdge(4, 2);

        graphAd.print();
    }
}
