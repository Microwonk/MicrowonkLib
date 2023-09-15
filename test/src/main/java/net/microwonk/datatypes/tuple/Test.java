package net.microwonk.datatypes.tuple;

public class Test {
    public static void main(String[] args) {
        Tuple t = new Tuple("Hello", 2, "Bye");
        String h = t.unsafe(0);
        Object a = t.get(1);
        t.set(1, 5);
        System.out.println(t.get(1));
        t.set(1, "String");
        System.out.println(t.get(1));
    }
}
