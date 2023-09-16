package net.microwonk.datatypes.tuple.immutable;

import net.microwonk.datatypes.tuple.TupleDataType;

public class Tuple3<E1,E2,E3> extends ImmutableTuple implements TupleDataType {
    public Tuple3(E1 v1, E2 v2, E3 v3) {
        super(v1, v2, v3);
    }

    public E1 e1() {
        return super.<E1>unsafe(0);
    }

    public E2 e2() {
        return super.<E2>unsafe(1);
    }

    public E3 e3() {
        return super.<E3>unsafe(2);
    }
}
