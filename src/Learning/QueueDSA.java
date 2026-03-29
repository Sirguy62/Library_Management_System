import java.util.LinkedList;
import java.util.Queue;

public class QueueDSA {
    public static void main(String[] args) {

        //Queue = FIFO data structure. First-In First-Out or First-Come First-Serve
        //       A collection for holding elements prior to processing Linear data structure

        //      add = enqueue, offer(), this is to add items
        //       remove = dequeue, poll(), to remove items

        // Where queues are used
        //1. Keyboard Buffer(letters should appear on the screen in the order they're pressed)
        //2. Printer queue (Print jobs should be completed in order)
        //3. Used in linkedLists, PriorityQueues, Breadth-first-search


        Queue<String> queue = new LinkedList<>();

        System.out.println(queue.isEmpty()); // this will return true because queue have not been filled yet

        queue.offer("Gospel");
        queue.offer("Edwin");
        queue.offer("Sirguy");
        queue.offer("Cool");

        System.out.println(queue.isEmpty()); // this will return false because queue have been filled with items
        System.out.println(queue.size());
        System.out.println(queue.contains("Gospel"));


        queue.poll();
        queue.peek();

        System.out.println(queue);
    }
}
