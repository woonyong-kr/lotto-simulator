package org.woonyong.behavior.runtime;

import org.woonyong.behavior.core.Behavior;

public class BehaviorContext<T> {
    private final T actor;
    private Behavior<T> currentBehavior;
    private Class<? extends Behavior<T>> pendingTransition;

    public BehaviorContext(T actor, Behavior<T> initialBehavior) {
        this.actor = actor;
        this.currentBehavior = initialBehavior;
    }

    public T getActor() {
        return actor;
    }

    public Behavior<T> getCurrentBehavior() {
        return currentBehavior;
    }

    public void setCurrentBehavior(Behavior<T> behavior) {
        this.currentBehavior = behavior;
    }

    public void transitionTo(Class<? extends Behavior<T>> next) {
        this.pendingTransition = next;
    }

    public boolean hasPendingTransition() {
        return pendingTransition != null;
    }

    public Class<? extends Behavior<T>> consumePendingTransition() {
        Class<? extends Behavior<T>> next = pendingTransition;
        pendingTransition = null;
        return next;
    }
}
