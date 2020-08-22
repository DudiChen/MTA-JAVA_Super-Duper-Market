package builder;

import javax.xml.bind.ValidationException;

public interface Builder<T, R> {
    public R build(T source) throws ValidationException;
}
