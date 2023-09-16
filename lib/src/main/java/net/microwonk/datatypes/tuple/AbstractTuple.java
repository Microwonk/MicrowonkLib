package net.microwonk.datatypes.tuple;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * An abstract base class for Tuple implementations that provides common functionality.
 */
public abstract class AbstractTuple implements TupleDataType {

    protected Object[] values; // Array to store the tuple elements.

    protected int size; // Number of elements in the tuple.

    /**
     * Constructs an AbstractTuple with the specified values.
     *
     * @param values The values to be stored in the tuple.
     */
    protected AbstractTuple(Object... values) {
        if (Arrays.stream(values).anyMatch(Objects::isNull)) throw new NullPointerException("Tuple may not take Null-Values");
        this.values = values;
        size = values.length;
    }

    /**
     * Default constructor for AbstractTuple.
     */
    protected AbstractTuple() {}

    /**
     * Gets the element at the specified position in the tuple.
     *
     * @param element The index of the element to retrieve.
     * @return The element at the specified index.
     * @throws ArrayIndexOutOfBoundsException if the specified index is out of bounds.
     */
    @Override
    public Object get(int element) {
        if (element > this.size || element < 0) {
            throw new ArrayIndexOutOfBoundsException(element);
        }
        return values[element];
    }

    /**
     * Gets the element at the specified position in the tuple without type checking.
     * This method should be used with caution, as it may result in runtime errors if
     * the element's type does not match the expected type.
     *
     * @param element The index of the element to retrieve.
     * @param <T>     The expected type of the element.
     * @return The element at the specified index.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T unsafe(int element) {
        return (T) get(element);
    }

    /**
     * Gets the class of the element at the specified position in the tuple.
     *
     * @param element The index of the element.
     * @return The class of the element.
     * @throws ArrayIndexOutOfBoundsException if the specified index is out of bounds.
     */
    @Override
    public Class<?> getClass(int element) {
        if (element > size || element < 0) {
            throw new ArrayIndexOutOfBoundsException(element);
        }
        return values[element].getClass();
    }

    /**
     * Gets the number of elements in the tuple.
     *
     * @return The size of the tuple.
     */
    public int size() {
        return size;
    }

    /**
     * Returns a Stream<Object> for the elements of the tuple.
     *
     * @return A Stream<Object> containing the elements of the tuple.
     */
    public Stream<?> stream() {
        return IntStream.range(0, size)
                .mapToObj(this::get);
    }

    /**
     * Converts the tuple to an array.
     *
     * @return An array containing the elements of the tuple.
     */
    @Override
    public Object[] toArray() {
        return values;
    }

    /**
     * Converts the tuple to an array with non-null values
     *
     * @return The values Array, with only non-null values. (needed for SizableTuple)
     */
    public Object[] cleanArr() {
        return this.stream().filter(Objects::nonNull).collect(Tuples.toSizableTuple()).toArray();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append('[').append(values[i]).append(']');
        }
        return sb.toString();
    }
}
