import java.util.Stack;

public class DSAStack {
    public static void main(String[] args){

        //Stack = LIFO data structure. Last-In First-Out or LastCome FirstServe
        //stores objects into a sort of "vertical tower"
        //Push() to add to the top, Peek() to pick an item in the stack,
        //pop() to remove an item from the top, sort() method

        //uses of stack
        //1. undo/redo feature in text editors like CMD Z and CMD shift Z
        //2. moving back and forward through browser history
        //3. backtracking algorithms (maze, file directories)
        //4. calling functions (call stack)

        Stack<String> stack = new Stack<>();

        System.out.println(stack.empty());

        stack.push("Minecraft");
        stack.push("Skyrim");
        stack.push("DOOM");
        stack.push("Borderlands");
        stack.push("FFVII");

        String myFavGames = stack.pop();
        System.out.println(stack);
        System.out.println(myFavGames);
        System.out.println(stack.peek());
        System.out.println(stack.search("Skyrim"));
    }
}
