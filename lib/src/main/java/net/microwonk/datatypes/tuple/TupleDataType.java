package net.microwonk.datatypes.tuple;

/**
 * An interface representing a Tuple data type, which is an ordered collection of elements.
 */
public interface TupleDataType {

    /**
     * Retrieves the element at the specified position in the Tuple.
     *
     * @param element The index of the element to retrieve.
     * @return The element at the specified index.
     */
    Object get(int element);

    /**
     * Retrieves the element at the specified position in the Tuple without type checking.
     * This method should be used with caution, as it may result in runtime errors if
     * the element's type does not match the expected type.
     *
     * @param element The index of the element to retrieve.
     * @param <T>     The expected type of the element.
     * @return The element at the specified index.
     */
    <T> T unsafe(int element);

    /**
     * Converts the Tuple to an array containing its elements.
     *
     * @return An array containing the elements of the Tuple.
     */
    Object[] toArray();

    /**
     * Retrieves the number of elements in the Tuple.
     *
     * @return The size of the Tuple.
     */
    int size();

    /**
     * Retrieves the class of the element at the specified position in the Tuple.
     *
     * @param element The index of the element.
     * @return The class of the element.
     */
    Class<?> getClass(int element);
}
