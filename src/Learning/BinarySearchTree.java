public class BinarySearchTree {

    TreeNode root;

    public void insert(TreeNode node) {
        root = insertHelper(root, node);
    }
    private TreeNode insertHelper(TreeNode root, TreeNode node) {
        int data = node.data;

        if (root == null){
            root = node;
            return root;
        } else if (data < root.data) {
            root.left = insertHelper(root.left, node);
        }
        else {
            root.right = insertHelper(root.right, node);
        }
        return root;
    }
    public void display() {
       displayHelper(root);
    }

    public void displayHelper(TreeNode root){
     if (root != null) {
         displayHelper(root.left);
         System.out.println(root.data);
         displayHelper(root.right);
     }
    }

    public boolean search(int data){
       return searchHelper(root, data);
    }

    private boolean searchHelper (TreeNode root, int data) {
      if (root == null) {
          return false;
      } else if (root.data == data) {
          return true;
      } else if (root.data > data) {
          return searchHelper(root.left, data);
      }else {
          return searchHelper(root.right, data);
      }
    }

    public void remove(int data) {
          if (search(data)) {
              removeHelper(root, data);
          }else {
              System.out.println(data + " could not be found");
          }
    }
    public TreeNode removeHelper (TreeNode root, int data) {
        if (root == null){
            return root;
        }
        else if (data < root.data){
            root.left = removeHelper(root.left, data);
        } else if (data > root.data) {
            root.right = removeHelper(root.right, data);
        }
        else {
            if (root.left == null && root.right == null){
                root = null;
            } else if (root.right != null) {
                root.data = successor(root);
                root.right = removeHelper(root.right, root.data);
            }
            else {
                root.data = predecessor(root);
                root.left = removeHelper(root.left, root.data);
            }
        }
        return root;
    }

    private int successor(TreeNode root) {
       root = root.right;
       while (root.left != null){
           root = root.left;
       }
       return root.data;
    }
    private int predecessor(TreeNode root) {
        root = root.left;
        while (root.right != null) {
            root = root.right;
        }
        return root.data;
    }
}
