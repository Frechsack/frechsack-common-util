package frechsack.prod.util;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Simple builder to build interface based proxies.
 * @param <E> The result type.
 */
public class ProxyBuilder<E> {

    public static final String DEFAULT_EXECUTOR_METHOD = "DEFAULT_EXECUTOR_METHOD";

    private final Class<?>[] interfaces;
    private final ClassLoader classLoader;
    private final Map<String,Function<Arguments, ?>> executors = new HashMap<>();
    private Reference<Handler> handlerReference;


    @SuppressWarnings("unchecked")
    public ProxyBuilder(Class<? super E> type) {
        this(new Class[]{ type }, type.getClassLoader());
    }

    @SuppressWarnings("unchecked")
    public ProxyBuilder(Class<? super E> type, ClassLoader classLoader) {
        this(new Class[]{ type },classLoader);
    }

    @SafeVarargs
    public ProxyBuilder(Class<? super E>... interfaces) {
        this(interfaces,null);
    }

    public ProxyBuilder(Class<? super E>[] interfaces, ClassLoader classLoader) {
        // Null check
        for(Class<?> clazz : interfaces) Objects.requireNonNull(clazz);

        this.interfaces = interfaces;
        this.classLoader = classLoader;
    }


    /**
     * Adds a function implementation.
     * @param name The name of the function.
     * @param executor The function that will be executed.
     * @return Returns this ProxyBuilder.
     */
    public ProxyBuilder<E> add(String name, Function<Arguments, ?> executor){
        for(Class<?> inter : interfaces)
            for(Method method : inter.getMethods()) {
                if(!method.getName().equalsIgnoreCase(name)) continue;
                this.handlerReference = null;
                this.executors.put(Objects.requireNonNull(name),Objects.requireNonNull(executor));
                return this;
            }
        throw new IllegalArgumentException("Method " + name + " not found.");
    }

    /**
     * Adds a function implementation. The implementation will take benefit of the "synchronized" keyword.
     * @param name The name of the function.
     * @param executor The function that will be executed.
     * @return Returns this ProxyBuilder.
     */
    public ProxyBuilder<E> addSynchronized(String name, Function<Arguments,?> executor) {
        return this.add(name, new Function<>() {
            @Override
            public synchronized Object apply(Arguments arguments) {
                return executor.apply(arguments);
            }
        });
    }

    /**
     * Adds a function implementation. The function will be executed within a synchronized-block.
     * @param name The name of the function.
     * @param executor The function that will be executed.
     * @param lock A supplier that supplies the object that will be used as the lock in as synchronized-block.
     * @return Returns this ProxyBuilder.
     */
    public ProxyBuilder<E> addLocked(String name, Function<Arguments,?> executor, Supplier<?> lock){
        return this.add(name, arguments -> {
            synchronized (lock.get()){
                return executor.apply(arguments);
            }
        });
    }

    /**
     * Adds a function implementation. The function will be executed within a synchronized-block.
     * @param name The name of the function.
     * @param executor The function that will be executed.
     * @param lock An object that will be used as the lock in as synchronized-block.
     * @return Returns this ProxyBuilder.
     */
    public ProxyBuilder<E> addLocked(String name, Function<Arguments,?> executor, Object lock){
        return this.add(name, arguments -> {
            synchronized (lock){
                return executor.apply(arguments);
            }
        });
    }

    /**
     * Adds a default function in case no implementation is provided for a method.
     * @param executor The default implementation.
     * @return Returns this ProxyBuilder.
     */
    public ProxyBuilder<E> setDefault(Function<Arguments,?> executor){
        return this.add(DEFAULT_EXECUTOR_METHOD, executor);
    }

    /**
     * Removes any implementation from this builder.
     */
    public void clear(){
        this.handlerReference = null;
        this.executors.clear();
    }

    /**
     * Builds an object based on the supplied implementations.
     * @return Returns the object.
     */
    @SuppressWarnings("unchecked")
    public E build() {
        return (E) Proxy.newProxyInstance(this.classLoader, this.interfaces, this.buildHandler());
    }

    private synchronized InvocationHandler buildHandler() {
        Handler handler = this.handlerReference == null
                ? null
                : this.handlerReference.get();
        if (handler == null)
            this.handlerReference = new SoftReference<>(handler = new Handler(new HashMap<>(this.executors)));
        return handler;
    }

    /**
     * An object that represents the arguments and details about the called method.
     * @param arguments The arguments from the caller.
     * @param method The details about the method.
     */
    public record Arguments(Object[] arguments, Method method) {

        /**
         * Return the raw value of an argument. In case the index is out of range, an exception will be thrown.
         * @param index The index of the argument.
         * @return Returns the raw value.
         */
        public Object getRaw(int index) {
            return arguments[index];
        }

        /**
         * Returns a possible undefined argument. In case the argument is missing, an empty {@link Undefined} is returned.
         * @param index The index of the argument.
         * @return Returns the argument.
         */
        public Undefined<Object> get(int index) {
            return index < arguments.length
                    ? Undefined.of(arguments[index])
                    : Undefined.of();
        }

        public Class<?> methodClass(){
            return method.getDeclaringClass();
        }

        /**
         * Returns the amount of arguments.
         * @return The amount of arguments.
         */
        public int length() {
            return arguments.length;
        }
    }

    private record Handler(Map<String, Function<Arguments, ?>> executors) implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            Function<Arguments, ?> executor = executors.get(method.getName());
            if (executor == null)
                executor = executors.get(DEFAULT_EXECUTOR_METHOD);
            // Look for default executor
            if(executor == null)
                throw new IllegalStateException("Method " + method.getName()  + " is not implemented.");

            return executor.apply(new Arguments(args, method));
        }
    }
}
