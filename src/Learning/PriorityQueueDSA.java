import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class PriorityQueueDSA {
    public static void main() {

        // Priority Queue = A FIFO data structure that serves elements with the highest priority
        //                  first before elements with lower priority

//        Queue<Double> queue = new LinkedList<>();
        Queue<Double> queue = new PriorityQueue<>(Collections.reverseOrder());// to arrange them in order based on the higher number or gpa

        queue.offer(3.0);
        queue.offer(2.3);
        queue.offer(4.1);
        queue.offer(1.7);
        queue.offer(3.7);

        System.out.println(queue);

        while (!queue.isEmpty()) {
            System.out.println(queue.poll());
        }

    }
}
