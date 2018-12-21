package com.monster.godzilla.utlis;


import com.monster.godzilla.interfaces.IProvider;

/**
 * <p>
 * <h2>简述:</h2>
 * <ol>无</ol>
 * <h2>功能描述:</h2>
 * <ol>无</ol>
 * <h2>修改历史:</h2>
 * <ol>无</ol>
 * </p>
 *
 * @author 11925
 * @date 2018/11/20/020
 */
public final class DoubleCheck<T> implements IProvider<T> {
    private static final Object UNINITIALIZED = new Object();

    private volatile IProvider<T> provider;
    private volatile Object instance = UNINITIALIZED;

    private DoubleCheck(IProvider<T> provider) {
        assert provider != null;
        this.provider = provider;
    }


    public static <T> IProvider<T> provider(IProvider<T> delegate) {
        Preconditions.checkNotNull(delegate);
        if (delegate instanceof DoubleCheck) {
            /* This should be a rare case, but if we have a scoped @Binds that delegates to a scoped
             * binding, we shouldn't cache the value again. */
            return delegate;
        }
        return new DoubleCheck<T>(delegate);
    }
    @SuppressWarnings("unchecked")
    @Override
    public T call() {
        Object result = instance;
        if (result == UNINITIALIZED) {
            synchronized (this) {
                result = instance;
                if (result == UNINITIALIZED) {
                    result = provider.call();
                    /* Get the current instance and test to see if the call to provider.get() has resulted
                     * in a recursive call.  If it returns the same instance, we'll allow it, but if the
                     * instances differ, throw. */
                    Object currentInstance = instance;
                    if (currentInstance != UNINITIALIZED && currentInstance != result) {
                        throw new IllegalStateException("Scoped provider was invoked recursively returning "
                                + "different results: " + currentInstance + " & " + result + ". This is likely "
                                + "due to a circular dependency.");
                    }
                    instance = result;
                    /* Null out the reference to the provider. We are never going to need it again, so we
                     * can make it eligible for GC. */
                    provider = null;
                }
            }
        }
        return (T) result;
    }
}
