import java.util.ArrayList;
import java.util.LinkedList;

public class LinkedListDSA {
    public static void main(String[] args) {

        // LinkedList =  stores Nodes in 2 parts (data + address)
//               Nodes are in non-consecutive memory locations
//               Elements are linked using pointers
//                    Singly Linked List
//            Node            Node            Node
//        [data | address] -> [data | address] -> [data | address]
//                    Doubly Linked List
//            Node                           Node
//     [address | data | address] <-> [address | data | address]
// advantages?
// 1. Dynamic Data Structure (allocates needed memory while running)
// 2. Insertion and Deletion of Nodes is easy. O(1)
// 3. No/Low memory waste

        // advantages?
// 1. Dynamic Data Structure (allocates needed memory while running)
// 2. Insertion and Deletion of Nodes is easy. O(1)
// 3. No/Low memory waste
// disadvantages?
// 1. Greater memory usage (additional pointer)
// 2. No random access of elements (no index [i])
// 3. Accessing/searching elements is more time consuming. O(n)
// uses?
// 1. implement Stacks/Queues
// 2. GPS navigation
// 3. music playlist



        //LinkedList can be treated as a Stack or Queue, meaning methods of stack and queue
        //can be applied to linkedlist such as pop, push, offer, poll etc

        LinkedList<String> linkedList = new LinkedList<>();
        LinkedList<Integer> linkedLists = new LinkedList<>();
        ArrayList<Integer> arrayList = new ArrayList<>();

        long startTime;
        long endTime;
        long elapsedTime;


        //This is a Stack LinkedList, push is used to add items, pop to remove the last item added
        linkedList.push("A");
        linkedList.push("B");
        linkedList.push("C");
        linkedList.push("D");
        linkedList.push("F");
        linkedList.pop();


        //This is a queue LinkedList, offer is used to add items, poll to remove the last item added
        linkedList.offer("E");
        linkedList.offer("G");
        linkedList.offer("H");
        linkedList.offer("I");
        linkedList.offer("J");
        linkedList.poll();

        linkedList.add(4,"E");
        linkedList.remove("E");
        System.out.println(linkedList.indexOf("I"));
        System.out.println(linkedList.peekFirst());
        System.out.println(linkedList.peekLast());
        linkedList.addFirst("0");
        linkedList.addLast("8");

        System.out.println(linkedList);





    // calculating how fast a linkedlist or arraylist can be on different actions
        for (int i = 0; i < 100000; i++){
            linkedLists.add(i);
            arrayList.add(i);
        }
        startTime = System.nanoTime();
//        linkedLists.get(99999);
        linkedList.remove(0);
        endTime = System.nanoTime();
        elapsedTime =endTime - startTime;
        System.out.println("LinkedList: " + elapsedTime + " ns");


        startTime = System.nanoTime();
//        arrayList.get(99999);
        arrayList.remove(0);
        endTime = System.nanoTime();
        elapsedTime =endTime - startTime;

        System.out.println("ArrayList: " + elapsedTime + " ns");

    }
}
