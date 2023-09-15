package net.microwonk.datatypes.tuple;

// TODO documentation
public interface TupleDataType {
    Object get(int element);
    <T> T unsafe(int element);
    Object[] toArray();
    boolean set(int element, Object newVal);
}
