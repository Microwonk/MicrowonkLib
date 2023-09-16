package net.microwonk.monads.generic;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public interface Monad<T, M extends Monad<?, M>> extends Functor<T> {

    <R, N extends Monad<R, M>> Monad<R, M> flatMap(
            Function<? super T, N> fMap);

    default <V, R, N extends Monad<V, M>> Monad<R, M> and(
            N											  rOther,
            BiFunction<? super T, ? super V, ? extends R> fJoin) {
        return flatMap(t -> rOther.map(v -> fJoin.apply(t, v)));
    }

    @Override
    <R> Monad<R, M> map(Function<? super T, ? extends R> map);

    @Override
    default Monad<T, M> then(Consumer<? super T> consumer) {
        return flatMap(t -> {
            consumer.accept(t);

            return this;
        });
    }
}
