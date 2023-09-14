package net.microwonk.datatypes.tuples;

// TODO
public class SizeableTuple implements TupleDataType {
    @Override
    public Object get(int element) {
        return null;
    }

    @Override
    public <T> T unsafe(int element) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public boolean set(int element, Object newVal) {
        return false;
    }
}
