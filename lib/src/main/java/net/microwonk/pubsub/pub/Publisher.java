package net.microwonk.pubsub.pub;

import net.microwonk.pubsub.sub.Subscriber;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Nicolas Frey
 * @version 1.0.2
 * @requires Class 'net.microwonk.pubsub.sub.Subscriber'
 */
public class Publisher {

    private final ConcurrentHashMap<Integer, WeakReference<Subscriber<?>>> subscribers = new ConcurrentHashMap<>();

    public void subscribe(Subscriber<?> subscriber) {
        Objects.requireNonNull(subscriber);
        subscribers.put(subscriber.hashCode(), new WeakReference<>(subscriber));
    }

    public synchronized void publish(Object message) {
        subscribers.forEach((key, value) -> {
            Subscriber<?> subscriber = value.get();
            Objects.requireNonNull(subscriber);
            deliverMessage(subscriber, message);
        });
    }

    private void deliverMessage(Subscriber<?> sub, Object message) {
        try {
            Method toInvoke = Arrays
                    .stream(sub.getClass().getDeclaredMethods())
                    .filter(m -> m.getName().equals("receiveMessage"))
                    .findFirst()
                    .orElseThrow(IllegalStateException::new);
            toInvoke.setAccessible(true);
            toInvoke.invoke(sub, message);
        } catch (Exception ignored) {
            // this is never the case
        }
    }

    public synchronized void removeSubscriber(Subscriber<?> toRemove) {
        subscribers.remove(toRemove.hashCode());
    }

    public synchronized void removeSubscribers() {
        subscribers.forEach((key, value) -> {
            var subscriber = value.get();
            Objects.requireNonNull(subscriber);
            removeSubscriber(subscriber);
        });
    }

    public synchronized int size() {
        return subscribers.size();
    }
}
