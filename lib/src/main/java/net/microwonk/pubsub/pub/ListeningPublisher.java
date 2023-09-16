package net.microwonk.pubsub.pub;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

// TODO documentation
public class ListeningPublisher extends Publisher {

    private final ConcurrentHashMap<Object, Set<String>> listenTargets = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Thread> activeListeners = new ConcurrentHashMap<>();

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
            activeListeners.put(fieldName, listener);
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
        activeListeners.forEach((k, v) -> v.interrupt());
        activeListeners.clear();
        listenTargets.clear();
    }

    public synchronized void removeListener(String field) {
        if (!activeListeners.containsKey(field)) {
            return;
        }
        activeListeners.get(field).interrupt();
        activeListeners.remove(field);
    }
}
