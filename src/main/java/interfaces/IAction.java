package interfaces;

/**
 * A functional interface to define a generic action.
 * @param <T> The datatype of an object on which action is to be performed.
 */
@FunctionalInterface
public interface IAction<T> {
    void execute(T object);
}
