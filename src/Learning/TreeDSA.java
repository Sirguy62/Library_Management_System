public class TreeDSA {
    public static void main(String[] args) {


        // Binary Search Tree = A tree data structure, where each node is greater than it's left child,
        //                      but less than it's right.

        //                      benefit: easy to locate a node when they are in this order

        //                      time complexity: best case  O(log n)
        //                                        worst case O(n)

        //                      space complexity: O(n)

        BinarySearchTree tree = new BinarySearchTree();

        tree.insert(new TreeNode(5));
        tree.insert(new TreeNode(1));
        tree.insert(new TreeNode(9));
        tree.insert(new TreeNode(2));
        tree.insert(new TreeNode(7));
        tree.insert(new TreeNode(3));
        tree.insert(new TreeNode(6));
        tree.insert(new TreeNode(4));
        tree.insert(new TreeNode(8));

        tree.remove(5);
        tree.display();
        System.out.println(tree.search(5));


    }
}
