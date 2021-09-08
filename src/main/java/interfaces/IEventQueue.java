package interfaces;

public interface IEventQueue<T> {
    /**
     * Adds an event to the event queue.
     * @param event
     */
    void addEvent(T event);

    /**
     * Performs required action on a new event.
     */
    void onEvent();
}
