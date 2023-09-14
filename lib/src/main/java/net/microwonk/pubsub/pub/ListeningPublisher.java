package net.microwonk.pubsub.pub;

import net.microwonk.datatypes.tuples.Tuple;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// TODO documentation and removing Tuple
public class ListeningPublisher extends Publisher {

    private final ConcurrentHashMap<Object, Set<String>> listenTargets = new ConcurrentHashMap<>();
    private final Set<Tuple> activeListeners = new HashSet<>(); // HashMap would be a lot smarter, if not for the removeIf, which I love

    public synchronized void addTargets(Object targetObject, String... fieldName) {
        if (!listenTargets.containsKey(targetObject)) {
            listenTargets.put(targetObject, new HashSet<>());
        }
        Arrays.stream(fieldName).forEach(field -> listenTo(targetObject, field));
    }

    protected synchronized void listenTo(Object targetObject, String fieldName) {
        try {
            Field listenTo = targetObject.getClass().getDeclaredField(fieldName);
            listenTo.setAccessible(true);
            Thread listener = new Thread(() -> {
                try {
                    Object lastValue = listenTo.get(targetObject);
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            Object value = listenTo.get(targetObject);
                            if (!lastValue.equals(value)) {
                                publish(value);
                                lastValue = value;
                            }
                        } catch (IllegalAccessException e) {
                            System.err.println("Access to field is not allowed?");
                            break;
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Should not happen");
                }
            });
            // Add the listener to the list of active listeners
            activeListeners.add(new Tuple(fieldName, listener));
            // auto terminate when the main thread stops
            listener.setDaemon(true);
            // Start the listener Thread
            listener.start();

        } catch (NoSuchFieldException e) {
            System.err.println("The Field given does not exist in Object: " + targetObject.getClass().getName());
        }
    }

    /**
     * clear all traces of the daemon threads, interrupt (stop) them,
     * and clear all listenTargets
     */
    public synchronized void cleanUpListeners() {
        activeListeners.forEach(tuple -> tuple.<Thread>unsafe(1).interrupt());
        activeListeners.clear();
        listenTargets.clear();
    }

    public synchronized void removeListener(String field) {
        activeListeners.removeIf(tuple -> {
            boolean shouldTerminate = field.equals(tuple.unsafe(1));
            if (shouldTerminate) {
                tuple.<Thread>unsafe(1).interrupt();
            }
            return shouldTerminate;
        });
    }
}
