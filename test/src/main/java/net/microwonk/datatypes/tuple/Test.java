package net.microwonk.datatypes.tuple;

import net.microwonk.datatypes.tuple.immutable.ImmutableTuple;
import net.microwonk.datatypes.tuple.immutable.Tuple2;
import net.microwonk.datatypes.tuple.mutable.SizableTuple;
import net.microwonk.datatypes.tuple.mutable.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) {
        ImmutableTuple it = new ImmutableTuple("Nicolas", "Frey", 19);
        System.out.println(it);
        Object i = it.get(2);
        it.<Thread>unsafe(2);
        System.out.println(i);

        Tuple2<Integer, String> t2 = new Tuple2<>(2, "Tuple");

        int f = t2.e1();
        String str = t2.e2();
        str = t2.unsafe(1);

        SizableTuple st = new SizableTuple("HI");
        st.add("miau");
        st.insert(0, 1);
        st.insert(0, "m");
        st.insert(0, 1);
        st.insert(0, "m");
        st.insert(0, 1);
        st.insert(0, "m");
        st.insert(0, 1);
        System.out.println(st);

        System.out.println(st.stream().filter(v -> v.getClass().equals(String.class)).collect(Tuples.toTuple()));

        Tuple a = Tuples.morphInto(st, Tuple.class);
    }
}
