package com.CharunCore.server.plugin.scheduler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public final class ServerScheduler {

    public static final ServerScheduler INSTANCE = new ServerScheduler();

    public record Task(long id, long nextTick, long period, Runnable runnable, PluginRef plugin, boolean async) {}

    /** 弱插件引用: 插件卸载后任务自动跳过(避免类加载器泄漏)。 */
    public static final class PluginRef {
        public final Object plugin;
        public volatile boolean valid = true;

        public PluginRef(Object plugin) {
            this.plugin = plugin;
        }
    }

    private final AtomicLong ids = new AtomicLong();
    private final ConcurrentHashMap<Long, Task> tasks = new ConcurrentHashMap<>();
    private final ExecutorService asyncPool = Executors.newFixedThreadPool(2, r -> {
        Thread t = new Thread(r, "PluginAsync");
        t.setDaemon(true);
        return t;
    });
    private volatile long currentTick = 0;

    private ServerScheduler() {}

    public static PluginRef ref(Object plugin) {
        return new PluginRef(plugin);
    }

    public void tick() {
        currentTick++;
        for (Task t : tasks.values()) {
            if (t.nextTick() > currentTick) continue;
            if (!t.plugin().valid) {
                tasks.remove(t.id());
                continue;
            }
            if (t.period() <= 0) tasks.remove(t.id());
            else tasks.computeIfPresent(t.id(), (k, old) ->
                    new Task(old.id(), currentTick + old.period(), old.period(), old.runnable(), old.plugin(), old.async()));
            if (t.async()) {
                asyncPool.execute(() -> {
                    try {
                        t.runnable().run();
                    } catch (Exception e) {
                        System.err.println("[调度器] 异步任务异常: " + e);
                    }
                });
            } else {
                try {
                    t.runnable().run();
                } catch (Exception e) {
                    System.err.println("[调度器] 任务异常: " + e);
                }
            }
        }
    }

    public long runTask(PluginRef plugin, Runnable runnable) {
        return schedule(plugin, runnable, 0, -1, false);
    }

    public long runTaskLater(PluginRef plugin, Runnable runnable, long delayTicks) {
        return schedule(plugin, runnable, Math.max(0, delayTicks), -1, false);
    }

    public long runTaskTimer(PluginRef plugin, Runnable runnable, long delayTicks, long periodTicks) {
        return schedule(plugin, runnable, Math.max(0, delayTicks), Math.max(1, periodTicks), false);
    }

    public long runTaskAsync(PluginRef plugin, Runnable runnable) {
        return schedule(plugin, runnable, 0, -1, true);
    }

    public long runTaskLaterAsync(PluginRef plugin, Runnable runnable, long delayTicks) {
        return schedule(plugin, runnable, Math.max(0, delayTicks), -1, true);
    }

    public long runTaskTimerAsync(PluginRef plugin, Runnable runnable, long delayTicks, long periodTicks) {
        return schedule(plugin, runnable, Math.max(0, delayTicks), Math.max(1, periodTicks), true);
    }

    private long schedule(PluginRef plugin, Runnable runnable, long delay, long period, boolean async) {
        long id = ids.incrementAndGet();
        tasks.put(id, new Task(id, currentTick + delay, period, runnable, plugin, async));
        return id;
    }

    public void cancel(long taskId) {
        tasks.remove(taskId);
    }

    public void cancelTasks(PluginRef plugin) {
        plugin.valid = false;
        tasks.values().removeIf(t -> t.plugin() == plugin);
    }

    public int pendingCount() {
        return tasks.size();
    }
}
