package net.microwonk.pubsub.sub;

@FunctionalInterface
public interface Subscriber<T> {
    /**
     *
     * @param message the Message to be received. Generic, so you can specify
     *                the type to be handled when it is sent by the publisher
     *                e.g. you only want to get Information when an Integer is
     *                updated. Also, can be of Type of your Custom Object, makes
     *                it easier to distinguish between field names and handle
     *                values more easily.
     *
     */
    void receiveMessage(T message);
}
