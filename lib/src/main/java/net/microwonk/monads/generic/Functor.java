package net.microwonk.monads.generic;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Functor<T> {

    <R> Functor<R> map(Function<? super T, ? extends R> map);

    Functor<T> orElse(Consumer<Exception> handler);

    T orFail() throws Exception;

    T orGet(Supplier<T> supply);

    <E extends Exception> T orThrow(Function<Exception, E> mapException)
            throws E;

    default T orUse(T defaultValue) {
        return orGet(() -> defaultValue);
    }

    default Functor<T> then(Consumer<? super T> consumer) {
        return map(t -> {
            consumer.accept(t);

            return t;
        });
    }
}
