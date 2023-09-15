package net.microwonk.pubsub.pub;

import net.microwonk.pubsub.sub.Subscriber;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

///  @Examples : [
///  # Inheritance
///  public class Channel extends Publisher {
///       private String channelName;
///       private int channelID;
///
///       // Event
///       public void setChannel(String channel) {
///           this.channelName = channel;
///           this.channelID = channel.chars().sum(); // id for channelName
///           super.publish(channel);
///           super.publish(channelID);
///       }
///
///       // should only receive channel String
///       public static class ChannelSubscriber implements Subscriber<String> {
///          @Override
///          public void receiveMessage(String message) {
///              System.out.println(this.hashCode() + " " + message);
///          }
///       }
///
///       // should only receive channelID
///       public static class IDSubscriber implements Subscriber<Integer> {
///          @Override
///          public void receiveMessage(Integer message) {
///              System.out.println(this.hashCode() + " " + message);
///          }
///       }
///  } ,
///  # Composition
///  public class Channel {
///       private String channelName;
///       private int channelID;
///       private Publisher pub;
///
///       // Event
///       public void setChannel(String channel) {
///           this.channelName = channel;
///           this.channelID = channel.chars().sum(); // id for channelName
///           pub.publish(channel);
///           pub.publish(channelID);
///       }
///       ... same code as before
///
/// }
/**
 * @author Nicolas Frey
 * @version 1.0.2
 * @requires Class 'net.microwonk.pubsub.sub.Subscriber'
 * <br>
 * can either be managed with Inheritance or Composition.
 * @example @Examples 
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
            try {
                deliverMessage(subscriber, message);
            } catch (Exception ignored) {
                // ignore
            }
        });
    }

    private void deliverMessage(Subscriber<?> sub, Object message) throws IllegalAccessException, InvocationTargetException {
        Method toInvoke = Arrays
                .stream(sub.getClass().getDeclaredMethods())
                .filter(m -> m.getName().equals("receiveMessage"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Subscriber can not NOT have method 'receiveMessage'"));

        // only invokes 'receiveMessage' on Subscriber, if parameter and the message are of the same class
        if (Arrays.stream(toInvoke.getParameterTypes()).anyMatch(param -> param.equals(message.getClass()))) {
            toInvoke.setAccessible(true);
            toInvoke.invoke(sub, message);
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
