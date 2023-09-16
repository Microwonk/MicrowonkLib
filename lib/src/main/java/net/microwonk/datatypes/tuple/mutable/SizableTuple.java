package net.microwonk.datatypes.tuple.mutable;

import net.microwonk.datatypes.tuple.TupleDataType;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.RandomAccess;

/**
 * A SizableTuple class that represents a Tuple with the ability to add and insert values.
 */
public class SizableTuple extends Tuple implements TupleDataType, RandomAccess, Serializable {

    @Serial
    private static final long serialVersionUID = 21L;

    public SizableTuple(Object... values) {
        super(values);
    }

    /**
     * Adds a new value to the end of the SizableTuple.
     *
     * @param value The value to add.
     * @return true if the value was successfully added.
     */
    public boolean add(Object value) {
        if (value == null) {
            throw new NullPointerException("Null value cannot be added to SizableTuple");
        }
        ensureCapacity(size + 1);
        values[size++] = value;
        return true;
    }

    /**
     * Inserts a new value at the specified index in the SizableTuple.
     *
     * @param index The index at which to insert the value.
     * @param value The value to insert.
     * @throws IndexOutOfBoundsException if the specified index is out of bounds.
     */
    public void insert(int index, Object value) {
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (value == null) {
            throw new NullPointerException("Null value cannot be inserted into SizableTuple");
        }
        ensureCapacity(size + 1);

        // Move the elements from index to the right
        System.arraycopy(values, index, values, index + 1, size - index);

        // Insert the new value at the specified index
        values[index] = value;
        size++;
    }

    /**
     * Ensures that the SizableTuple has enough capacity to accommodate the specified size.
     *
     * @param minCapacity The minimum capacity required.
     */
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > values.length) {
            int newCapacity = Math.max(values.length * 2, minCapacity);
            values = Arrays.copyOf(values, newCapacity);
        }
    }
}
