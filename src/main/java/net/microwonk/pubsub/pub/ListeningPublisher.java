package net.microwonk.pubsub.pub;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ListeningPublisher extends Publisher {

    private final ConcurrentHashMap<Object, Set<String>> listenTargets = new ConcurrentHashMap<>();
    private final List<Thread> listenThreads = new ArrayList<>();

    public ListeningPublisher() { /* empty Constructor */ }

    public void addFields(Object targetObject, String ...fieldName) {
        if (!listenTargets.containsKey(targetObject)) {
            listenTargets.put(targetObject, new HashSet<>());
        }
        Arrays.stream(fieldName).forEach(field -> listenTo(targetObject, field));
    }

    protected void listenTo(Object targetObject, String fieldName) {
        try {
            Field listenTo = targetObject.getClass().getDeclaredField(fieldName);
            listenTo.setAccessible(true);
            Thread listener = new Thread(() -> {
                try {
                    Object lastValue = listenTo.get(targetObject);
                    while (true) {
                        try {
                            Object value = listenTo.get(targetObject);
                            if (!lastValue.equals(value)) {
                                publish(value);
                                lastValue = value;
                            }
                        } catch (IllegalAccessException e) {
                            System.err.println("Acces to field is not allowed?");
                            break;
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Should not happen");
                }
            });
            // start the listener Thread
            listenThreads.add(listener);
            listener.start();

            Runtime.getRuntime().addShutdownHook(new Thread(listener::interrupt));

        } catch (NoSuchFieldException e) {
            System.err.println("The Field given does not exist in Object: " + targetObject.getClass().getName());
        }
    }

    protected void cleanUpListeners() {
        listenThreads.forEach(Thread::interrupt);
    }
}
