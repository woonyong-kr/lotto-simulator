package org.woonyong.behavior.core;

import org.woonyong.behavior.runtime.BehaviorContext;

public interface Behavior<T> {
    void update(T actor, BehaviorContext<T> context);
}
