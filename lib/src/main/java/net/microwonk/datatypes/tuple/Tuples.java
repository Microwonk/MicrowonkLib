package net.microwonk.datatypes.tuple;

import net.microwonk.datatypes.tuple.immutable.ImmutableTuple;
import net.microwonk.datatypes.tuple.immutable.Tuple2;
import net.microwonk.datatypes.tuple.immutable.Tuple3;
import net.microwonk.datatypes.tuple.mutable.SizableTuple;
import net.microwonk.datatypes.tuple.mutable.Tuple;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tuples {

    public static Tuple toTuple(Stream<?> stream) {
        return new Tuple(stream.toArray());
    }

    public static ImmutableTuple toImmutableTuple(Stream<?> stream) {
        return new ImmutableTuple(stream.toArray());
    }

    public static SizableTuple toSizableTuple(Stream<?> stream) {
        return new SizableTuple(stream.toArray());
    }

    public static <T> Collector<T, ?, Tuple> toTuple() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> new Tuple(list.toArray())
        );
    }

    public static <T> Collector<T, ?, ImmutableTuple> toImmutableTuple() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> new ImmutableTuple(list.toArray())
        );
    }

    public static <T> Collector<T, ?, SizableTuple> toSizableTuple() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> new SizableTuple(list.toArray())
        );
    }

    @SuppressWarnings("unchecked")
    public static <T, E1, E2> Collector<T, ?, Tuple2<E1, E2>> toTuple2() {
        return Collector.of(
                () -> new Object[2],
                (acc, item) -> {
                    if (acc[0] == null) {
                        acc[0] = item;
                    } else if (acc[1] == null) {
                        acc[1] = item;
                    } else {
                        throw new IllegalStateException("Too many elements for Tuple2");
                    }
                },
                (acc1, acc2) -> {
                    if (acc1[0] != null && acc1[1] != null) {
                        throw new IllegalStateException("Too many elements for Tuple2");
                    }
                    if (acc2[0] != null) {
                        acc1[1] = acc2[0];
                    }
                    if (acc2[1] != null) {
                        acc1[1] = acc2[1];
                    }
                    return acc1;
                },
                acc -> new Tuple2<>((E1) acc[0], (E2) acc[1])
        );
    }

    @SuppressWarnings("unchecked")
    public static <T, E1, E2, E3> Collector<T, ?, Tuple3<E1, E2, E3>> toTuple3() {
        return Collector.of(
                () -> new Object[3],
                (acc, item) -> {
                    for (int i = 0; i < 3; i++) {
                        if (acc[i] == null) {
                            acc[i] = item;
                            break;
                        }
                    }
                },
                (acc1, acc2) -> {
                    for (int i = 0; i < 3; i++) {
                        if (acc1[i] == null) {
                            acc1[i] = acc2[i];
                        }
                    }
                    return acc1;
                },
                acc -> new Tuple3<>((E1) acc[0], (E2) acc[1], (E3) acc[2])
        );
    }

    public static <T extends AbstractTuple> T morphInto(AbstractTuple t1, Class<T> tupleClass) {
        try {
            return tupleClass.getConstructor(Object[].class).newInstance((Object) t1.cleanArr());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static Tuple toTuple(AbstractTuple t1) {
        return morphInto(t1, Tuple.class);
    }

    public static ImmutableTuple toImmutableTuple(AbstractTuple t1) {
        return morphInto(t1, ImmutableTuple.class);
    }

    public static SizableTuple toSizableTuple(AbstractTuple t1) {
        return morphInto(t1, SizableTuple.class);
    }

}
