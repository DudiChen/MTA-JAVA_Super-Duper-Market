package view;

import exception.OrderValidationException;

@FunctionalInterface
public interface TriConsumer<T, U, V> {
    void apply(T t, U u, V v) throws OrderValidationException;
}
