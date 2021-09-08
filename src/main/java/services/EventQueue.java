package services;

import interfaces.IAction;
import interfaces.IEventQueue;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * An event queue to hold and perform actions on events.
 * @param <T>
 */
public class EventQueue<T> implements IEventQueue<T> {
    private final BlockingDeque<T> events;
    private final ExecutorService executorService;
    private final IAction<T> action;
    private final String name;

    /**
     * Constructs an event queue.
     * @param name the event queue name.
     * @param action the action performed when a new event is added to the queue.
     */
    public EventQueue(String name, IAction<T> action) {
        this.action = action;
        this.name = name;
        this.events = new LinkedBlockingDeque<>();
        executorService = Executors.newFixedThreadPool(10);
    }

    /**
     * Adds an event to the event queue.
     * @param event
     */
    @Override
    public void addEvent(T event) {
        events.add(event);
        onEvent();
    }

    /**
     * Performs required action on a new event.
     */
    @Override
    public void onEvent() {
        T event = events.poll();
        if (event == null) {
            return;
        }

        // action.execute(event);
        executorService.submit(() -> action.execute(event));
    }
}
