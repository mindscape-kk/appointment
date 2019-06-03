package co.mscp;

import java.util.function.Supplier;

public class Cache<T, R> {
    public R getOrMake(T clazz, Supplier<R> function) {
        //TODO:
        return  function.get();
    }
}
