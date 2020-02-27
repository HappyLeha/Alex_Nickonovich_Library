package alexnickonovich.library.interfaces;

import lombok.NonNull;
@FunctionalInterface
public interface ICopier<T> {
    void copy (@NonNull T item);
}
