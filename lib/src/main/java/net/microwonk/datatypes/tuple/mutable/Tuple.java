package net.microwonk.datatypes.tuple.mutable;

import net.microwonk.datatypes.tuple.AbstractTuple;
import net.microwonk.datatypes.tuple.TupleDataType;
import net.microwonk.exceptions.ObjectMismatchException;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.RandomAccess;

/**
 * A Tuple class that represents an ordered collection of elements.
 */
public class Tuple extends AbstractTuple implements TupleDataType, RandomAccess, Serializable {

    @Serial
    private static final long serialVersionUID = 20L;

    /**
     * Constructs a Tuple with the specified values.
     *
     * @param values The values to be stored in the Tuple. Must not contain null elements.
     * @throws NullPointerException if any of the provided values is null.
     */
    public Tuple(Object... values) {
        super(values);
    }

    /**
     * Sets the element at the specified position in the Tuple to a new value (cannot be of different Class).
     *
     * @param element The index of the element to set.
     * @param newVal  The new value to set.
     * @return true if the element was successfully set.
     * @throws ArrayIndexOutOfBoundsException if the specified index is out of bounds.
     * @throws ObjectMismatchException       if the new value has a different class than the existing element.
     */
    public boolean set(int element, Object newVal) {
        if (element > size || element < 0) {
            throw new ArrayIndexOutOfBoundsException(element);
        }
        if (!values[element].getClass().equals(newVal.getClass())) {
            throw new ObjectMismatchException(values[element], newVal);
        }
        values[element] = newVal;
        return true;
    }

    /**
     * Replaces the element at the specified position in the Tuple with a new value (can be of different Class).
     *
     * @param element The index of the element to replace.
     * @param newObj  The new value to set.
     * @return true if the element was successfully replaced.
     * @throws ArrayIndexOutOfBoundsException if the specified index is out of bounds.
     */
    public boolean replace(int element, Object newObj) {
        if (element > size || element < 0) {
            throw new ArrayIndexOutOfBoundsException(element);
        }
        values[element] = newObj;
        return true;
    }

    /**
     * Checks if this Tuple is equal to another object.
     *
     * @param o The object to compare to.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple tuple = (Tuple) o;
        return Arrays.equals(values, tuple.values);
    }

    /**
     * Calculates the hash code for this Tuple.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    @java.io.Serial
    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        // Write out element count, and any hidden stuff
        s.defaultWriteObject();

        // Write out size as capacity for behavioral compatibility with clone()
        s.writeInt(size);

        // Write out all elements in the proper order.
        for (int i = 0; i < size; i++) {
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
