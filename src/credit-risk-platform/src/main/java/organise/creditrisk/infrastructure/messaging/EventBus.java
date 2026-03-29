package organise.creditrisk.infrastructure.messaging;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class EventBus {

    private static final Map<String, List<Consumer<Event>>> listeners =
            new ConcurrentHashMap<>();

    public static void subscribe(String type, Consumer<Event> consumer) {

        listeners
                .computeIfAbsent(type, k -> new ArrayList<>())
                .add(consumer);
    }

    public static void publish(Event event) {

        List<Consumer<Event>> consumers = listeners.get(event.getType());

        if (consumers != null) {
            for (Consumer<Event> consumer : consumers) {
                consumer.accept(event);
            }
        }
    }
}