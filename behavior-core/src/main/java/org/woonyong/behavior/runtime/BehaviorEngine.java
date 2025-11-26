package org.woonyong.behavior.runtime;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.woonyong.behavior.annotations.Action;
import org.woonyong.behavior.annotations.Order;
import org.woonyong.behavior.core.Behavior;

public class BehaviorEngine {
    private static final Logger logger = LoggerFactory.getLogger(BehaviorEngine.class);
    public static final String FAILED_TO_INVOKE_ACTION = "Failed to invoke @Action method: {}";
    public static final String FAILED_TO_INSTANTIATE_BEHAVIOR = "Failed to instantiate behavior: {}";

    public <T> void update(BehaviorContext<T> context) {
        Behavior<T> behavior = context.getCurrentBehavior();
        invokeActionMethods(behavior, context);
        handleTransitionIfNeeded(context);
    }

    private <T> void invokeActionMethods(Behavior<T> behavior, BehaviorContext<T> context) {
        List<Method> actionMethods = Arrays.stream(behavior.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Action.class))
                .sorted(Comparator.comparingInt(BehaviorEngine::getOrderValue))
                .toList();

        for (Method method : actionMethods) {
            try {
                method.setAccessible(true);
                method.invoke(behavior, context);
                if (context.hasPendingTransition()) {
                    break;
                }
            } catch (Exception e) {
                logger.error(FAILED_TO_INVOKE_ACTION, method.getName(), e);
            }
        }
    }

    private <T> void handleTransitionIfNeeded(BehaviorContext<T> context) {
        if (!context.hasPendingTransition()) {
            return;
        }
        Class<? extends Behavior<T>> next = context.consumePendingTransition();
        try {
            Constructor<? extends Behavior<T>> constructor = next.getDeclaredConstructor();
            constructor.setAccessible(true);
            context.setCurrentBehavior(constructor.newInstance());
        } catch (Exception e) {
            logger.error(FAILED_TO_INSTANTIATE_BEHAVIOR, next.getName(), e);
        }
    }

    private static int getOrderValue(Method method) {
        Order order = method.getAnnotation(Order.class);
        if (order == null) {
            return Integer.MAX_VALUE;
        }
        return order.value();
    }
}
