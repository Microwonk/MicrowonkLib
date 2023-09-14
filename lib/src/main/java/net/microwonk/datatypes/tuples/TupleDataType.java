package net.microwonk.datatypes.tuples;

// TODO documentation
public interface TupleDataType {
    Object get(int element);
    <T> T unsafe(int element);
    int size();
    Object[] toArray();
    boolean set(int element, Object newVal);
}
