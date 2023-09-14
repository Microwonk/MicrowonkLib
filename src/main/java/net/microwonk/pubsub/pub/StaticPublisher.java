package net.microwonk.pubsub.pub;

import net.microwonk.pubsub.sub.OnMessage;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class StaticPublisher {
    private final ConcurrentHashMap<String, ConcurrentHashMap<Integer, WeakReference<Object>>> subsOnChannels = new ConcurrentHashMap<>();

    public void subscribe(String channelName, Object subscriber) {
        Objects.requireNonNull(subscriber);
        if (!subsOnChannels.containsKey(channelName)) {
            subsOnChannels.put(channelName, new ConcurrentHashMap<>());
        }
        subsOnChannels.get(channelName).put(subscriber.hashCode(), new WeakReference<>(subscriber));
    }

    public void publish(String channelName, Object message) {
        subsOnChannels.get(channelName).forEach((key, value) -> {
            var subscriber = value.get();
            Objects.requireNonNull(subscriber);
            Method method = Arrays.stream(subscriber.getClass().getDeclaredMethods())
                    .filter(m -> m.getAnnotation(OnMessage.class) != null).findFirst()
                    .orElseThrow(IllegalStateException::new);
        });
    }

    private <T> boolean deliverMessage(T sub, Method method, Object message) {
        try {
            if (Arrays.stream(method.getParameterTypes()).anyMatch(param -> param.equals(message.getClass()))) {
                method.setAccessible(true);
                method.invoke(sub, message);
            }
            return true;
        } catch (Exception neverCase) {
            // this is never the case
        }
        return false;
    }
}
