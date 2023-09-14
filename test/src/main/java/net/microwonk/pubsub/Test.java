package net.microwonk.pubsub;

import net.microwonk.pubsub.pub.ListeningPublisher;
import net.microwonk.pubsub.sub.Subscriber;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        var sub1 = new TestSubscriber();
        var sub2 = new TestInlineSubscriber();

        var pub = new TestPublisher();

        pub.subscribe(sub1);
        pub.subscribe(sub2.subToInt);
        pub.subscribe(sub2.subToStr);

        Scanner s = new Scanner(System.in);

        pub.listenToThis = s.nextInt();
        pub.listenToThis2 = s.nextInt();

        // pub.cleanUpListeners();
        s.close();
    }

    public static class TestSubscriber implements Subscriber<Integer> {
        @Override
        public void receiveMessage(Integer message) {
            System.out.println(this.hashCode() + " " + message);
        }
    }

    public static class TestInlineSubscriber {
        protected final Subscriber<Integer> subToInt = new Subscriber<>() {
            @Override
            public void receiveMessage(Integer message) {
                System.out.println(this.hashCode() + " " + message);
            }
        };

        protected final Subscriber<String> subToStr = new Subscriber<>() {
            @Override
            public void receiveMessage(String message) {
                System.out.println(this.hashCode() + " " + message);
            }
        };
    }

    public static class TestPublisher extends ListeningPublisher {
        public int listenToThis = 1;
        public int listenToThis2 = 2;
        TestPublisher() {
            addTargets(this, "listenToThis", "listenToThis2");
        }


    }
}
