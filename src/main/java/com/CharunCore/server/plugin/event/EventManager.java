package com.CharunCore.server.plugin.event;

import com.CharunCore.server.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class EventManager {

    public static final EventManager INSTANCE = new EventManager();

    private record Handler(Object listener, Method method, EventPriority priority, boolean ignoreCancelled) {}

    private final Map<Class<? extends Event>, List<Handler>> handlers = new ConcurrentHashMap<>();
    private final Map<Object, List<Class<? extends Event>>> byListener = new ConcurrentHashMap<>();

    private EventManager() {}

    public void register(Object listener) {
        if (listener == null) return;
        List<Class<? extends Event>> registered = new ArrayList<>();
        for (Method method : listener.getClass().getMethods()) {
            EventHandler annotation = method.getAnnotation(EventHandler.class);
            if (annotation == null) continue;
            if (method.getParameterCount() != 1 || !Event.class.isAssignableFrom(method.getParameterTypes()[0])) {
                System.err.println("[事件] " + listener.getClass().getName() + "." + method.getName()
                        + " 签名非法, 需要 单个Event参数");
                continue;
            }
            @SuppressWarnings("unchecked")
            Class<? extends Event> type = (Class<? extends Event>) method.getParameterTypes()[0];
            method.setAccessible(true);
            handlers.computeIfAbsent(type, t -> new CopyOnWriteArrayList<>())
                    .add(new Handler(listener, method, annotation.priority(), annotation.ignoreCancelled()));
            registered.add(type);
        }
        if (registered.isEmpty()) {
            System.err.println("[事件] 监听器 " + listener.getClass().getName() + " 没有任何 @EventHandler 方法");
            return;
        }
        byListener.put(listener, registered);
        handlers.values().forEach(l -> l.sort(Comparator.comparingInt(h -> h.priority().slot)));
    }

    public void unregister(Object listener) {
        List<Class<? extends Event>> types = byListener.remove(listener);
        if (types == null) return;
        for (List<Handler> list : handlers.values()) {
            list.removeIf(h -> h.listener() == listener);
        }
    }

    public void unregisterPlugin(Object plugin) {
        for (List<Handler> list : handlers.values()) {
            list.removeIf(h -> h.listener() instanceof Plugin p && p == plugin
                    || isInnerOf(h.listener(), plugin));
        }
    }

    private boolean isInnerOf(Object listener, Object plugin) {
        return listener != null && listener.getClass().isMemberClass()
                && outerInstance(listener) == plugin;
    }

    private Object outerInstance(Object inner) {
        for (java.lang.reflect.Field f : inner.getClass().getDeclaredFields()) {
            if (!java.lang.reflect.Modifier.isStatic(f.getModifiers()) && f.getType() == inner.getClass().getDeclaringClass()) {
                try {
                    f.setAccessible(true);
                    return f.get(inner);
                } catch (IllegalAccessException ignored) {}
            }
        }
        return null;
    }

    public <T extends Event> T fire(T event) {
        List<Handler> list = handlers.get(event.getClass());
        if (list == null || list.isEmpty()) return event;
        for (Handler h : list) {
            if (h.ignoreCancelled() && event.isCancelled()) continue;
            try {
                h.method().invoke(h.listener(), event);
            } catch (Exception e) {
                Throwable cause = e.getCause() != null ? e.getCause() : e;
                System.err.println("[事件] 处理器异常 " + h.listener().getClass().getName()
                        + "." + h.method().getName() + "(" + event.getEventName() + "): " + cause);
            }
        }
        return event;
    }

    public Map<Class<? extends Event>, List<Handler>> snapshot() {
        return Collections.unmodifiableMap(handlers);
    }
}
