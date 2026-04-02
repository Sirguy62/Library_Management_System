package Core_Java_Topics.CAP_Theorem;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

public class CpDataStore {

    private volatile String value = "initial";
    private volatile boolean partitioned = false;

    // The quorum size — majority of replicas must ack before a write commits
    private static final int QUORUM = 2;
    private final int totalReplicas = 3;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    // WRITE — requires quorum acknowledgment
    public void write(String newValue) throws PartitionException {
        int reachable = countReachableReplicas();

        if (reachable < QUORUM) {
            // CP choice: REFUSE the write rather than risk inconsistency
            throw new PartitionException(
                    "Cannot achieve quorum. Reachable: " + reachable +
                            ", required: " + QUORUM + ". Write refused."
            );
        }

        lock.writeLock().lock();
        try {
            value = newValue;
            // In a real system, you'd replicate to all reachable nodes here
            System.out.println("Write committed: " + newValue);
        } finally {
            lock.writeLock().unlock();
        }
    }

    // READ — also requires quorum (strong consistency)
    public String read() throws PartitionException {
        int reachable = countReachableReplicas();

        if (reachable < QUORUM) {
            // CP choice: REFUSE the read to avoid returning stale data
            throw new PartitionException(
                    "Cannot guarantee consistency. Read refused."
            );
        }

        lock.readLock().lock();
        try {
            return value;
        } finally {
            lock.readLock().unlock();
        }
    }

    private int countReachableReplicas() {
        return partitioned ? 1 : totalReplicas;
    }

    public void simulatePartition(boolean on) { this.partitioned = on; }

    public static class PartitionException extends Exception {
        public PartitionException(String msg) { super(msg); }
    }

    // --- Demo ---
    public static void main(String[] args) throws Exception {
        CpDataStore store = new CpDataStore();

        store.write("v1");
        System.out.println("Read: " + store.read()); // v1

        store.simulatePartition(true);
        System.out.println("Partition active...");

        try {
            store.write("v2");                        // throws
        } catch (PartitionException e) {
            System.out.println("CP rejected: " + e.getMessage());
        }

        try {
            store.read();                              // throws
        } catch (PartitionException e) {
            System.out.println("CP rejected read: " + e.getMessage());
        }
    }
}