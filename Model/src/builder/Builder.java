package builder;

public interface Builder<T, R> {
    public R build(T source);
}
