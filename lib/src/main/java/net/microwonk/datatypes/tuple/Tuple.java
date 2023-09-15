package net.microwonk.datatypes.tuple;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.RandomAccess;

// TODO documentation
public class Tuple implements TupleDataType, Cloneable, RandomAccess, Serializable { // TODO implement stream

    @Serial
    private static final long serialVersionUID = 20L;

    private Object[] values;

    private final int size;

    public Tuple(Object ...values) {
        if (Arrays.stream(values).anyMatch(Objects::isNull)) throw new NullPointerException("Non-Sizeable Tuple cannot be created with null values");
        this.values = values;
        size = values.length;
    }

    public Object get(int element) {
        if (element > values.length || element < 0) {
            throw new ArrayIndexOutOfBoundsException("element invalid");
        }
        return values[element];
    }

    @Override
    public <T> T unsafe(int element) {
        return (T) get(element);
    }

    @Override
    public boolean set(int element, Object newVal) {
        if (element > values.length || element < 0) {
            throw new ArrayIndexOutOfBoundsException("element invalid");
        }
        values[element] = newVal;
        return true;
    }

    public int size() {
        return size;
    }

    @Override
    public Object[] toArray() {
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple tuple = (Tuple) o;
        return Arrays.equals(values, tuple.values);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    @Override
    public Tuple clone() {
        try {
            Tuple clone = (Tuple) super.clone();
            // Create new TupleWrapper instances for the cloned Tuple
            clone.values = new Object[values.length];
            System.arraycopy(values, 0, clone.values, 0, values.length);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @java.io.Serial
    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        // Write out element count, and any hidden stuff
        s.defaultWriteObject();

        // Write out size as capacity for behavioral compatibility with clone()
        s.writeInt(size);

        // Write out all elements in the proper order.
        for (int i=0; i<size; i++) {
            s.writeObject(values[i]);
        }
    }

    @java.io.Serial
    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        // Read in size, and any hidden stuff
        s.defaultReadObject();

        // Read in size, and any hidden stuff
        s.defaultReadObject();

        // Read in capacity
        int size = s.readInt(); // ignored

        Object[] elements = new Object[size];

        // Read in all elements in the proper order.
        for (int i = 0; i < size; i++) {
            elements[i] = s.readObject();
        }
        values = elements;
    }
}
