package net.microwonk.datatypes.tuple.immutable;

import net.microwonk.datatypes.tuple.TupleDataType;

public class Tuple2<E1,E2> extends ImmutableTuple implements TupleDataType {
    public Tuple2(E1 v1, E2 v2) {
        super(v1, v2);
    }

    public E1 e1() {
        return super.<E1>unsafe(0);
    }

    public E2 e2() {
        return super.<E2>unsafe(1);
    }
}
