package net.microwonk.datatypes.tuple.immutable;

import net.microwonk.datatypes.tuple.AbstractTuple;

import java.util.Arrays;
import java.util.Objects;

/**
 * An immutable Tuple class that represents an ordered collection of elements.
 * Once created, the elements in this tuple cannot be modified.
 */
public class ImmutableTuple extends AbstractTuple {

    /**
     * Constructs an ImmutableTuple with the specified values.
     *
     * @param values The values to be stored in the ImmutableTuple. Must not contain null elements.
     * @throws NullPointerException if any of the provided values is null.
     */
    public ImmutableTuple(Object... values) {
        super(values);
    }
}
