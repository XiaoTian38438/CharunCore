package com.CharunCore.server.plugin;

import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.plugin.api.PermissionManager;
import com.CharunCore.server.plugin.command.CommandExecutor;
import com.CharunCore.server.plugin.scheduler.ServerScheduler;
import com.CharunCore.server.plugin.command.CommandSender;
import com.CharunCore.server.plugin.command.PluginCommand;
import com.CharunCore.server.plugin.event.EventManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class PluginManager {

    private final Map<String, LoadedPlugin> plugins = new ConcurrentHashMap<>();
    private final Map<String, PluginCommand> commands = new ConcurrentHashMap<>();
    private final Map<String, String> aliasToCommand = new ConcurrentHashMap<>();

    public static final class LoadedPlugin {
        public final Plugin plugin;
        public final File file;
        public final URLClassLoader classLoader;

        LoadedPlugin(Plugin plugin, File file, URLClassLoader classLoader) {
            this.plugin = plugin;
            this.file = file;
            this.classLoader = classLoader;
        }
    }

    public List<Plugin> getPlugins() {
        List<Plugin> result = new ArrayList<>();
        plugins.values().forEach(p -> result.add(p.plugin));
        return result;
    }

    public Plugin getPlugin(String name) {
        LoadedPlugin lp = plugins.get(name.toLowerCase());
        return lp == null ? null : lp.plugin;
    }

    public PluginCommand getCommand(String nameOrAlias) {
        PluginCommand direct = commands.get(nameOrAlias.toLowerCase());
        if (direct != null) return direct;
        String target = aliasToCommand.get(nameOrAlias.toLowerCase());
        return target == null ? null : commands.get(target);
    }

    public Map<String, PluginCommand> getCommands() {
        return commands;
    }

    public PluginCommand registerCommand(Plugin owner, String name, String description,
                                         CommandExecutor executor,
                                         String... aliases) {
        PluginCommand cmd = new PluginCommand(name.toLowerCase(), description, null, owner, executor, aliases);
        commands.put(name.toLowerCase(), cmd);
        for (String alias : aliases) {
            aliasToCommand.put(alias.toLowerCase(), name.toLowerCase());
        }
        broadcastCommandsRefresh();
        return cmd;
    }

    public void unregisterCommandsOf(Plugin plugin) {
        commands.values().removeIf(c -> c.getOwner() == plugin);
        aliasToCommand.values().removeIf(v -> !commands.containsKey(v));
        broadcastCommandsRefresh();
    }

    public boolean dispatchCommand(CommandSender sender, String commandLine) {
        String trimmed = commandLine.startsWith("/") ? commandLine.substring(1) : commandLine;
        String[] parts = trimmed.split(" ");
        if (parts.length == 0 || parts[0].isEmpty()) return false;
        PluginCommand cmd = getCommand(parts[0]);
        if (cmd == null) return false;
        String[] args = new String[parts.length - 1];
        System.arraycopy(parts, 1, args, 0, args.length);
        return cmd.execute(sender, parts[0], args);
    }

    public void loadPlugins(File folder) {
        if (!folder.exists()) {
            folder.mkdirs();
            return;
        }
        File[] jars = folder.listFiles((d, n) -> n.endsWith(".jar"));
        if (jars == null) return;
        List<PluginDescription> descriptions = new ArrayList<>();
        Map<PluginDescription, File> files = new LinkedHashMap<>();
        for (File jar : jars) {
            try {
                PluginDescription desc = readDescription(jar);
                if (plugins.containsKey(desc.name().toLowerCase())) {
                    System.err.println("[插件] " + desc.name() + " 已加载, 跳过重复: " + jar.getName());
                    continue;
                }
                descriptions.add(desc);
                files.put(desc, jar);
            } catch (Exception e) {
                System.err.println("[插件] 解析 " + jar.getName() + " 失败: " + e.getMessage());
            }
        }
        List<PluginDescription> ordered = topologicalSort(descriptions);
        for (PluginDescription desc : ordered) {
            File jar = files.get(desc);
            try {
                enablePlugin(desc, jar);
            } catch (Throwable t) {
                System.err.println("[插件] 启用 " + desc.name() + " 失败: " + t);
                t.printStackTrace();
            }
        }
    }

    private void enablePlugin(PluginDescription desc, File jar) throws Exception {
        URLClassLoader loader = new URLClassLoader(new URL[]{jar.toURI().toURL()},
                getClass().getClassLoader());
        Class<?> mainClass = Class.forName(desc.main(), true, loader);
        if (!Plugin.class.isAssignableFrom(mainClass)) {
            throw new IllegalArgumentException(desc.main() + " 未继承 plugin.com.CharunCore.server.Plugin");
        }
        Plugin plugin = (Plugin) mainClass.getDeclaredConstructor().newInstance();
        File dataFolder = new File("plugins/" + desc.name());
        plugin.init(desc, jar, dataFolder);
        plugins.put(desc.name().toLowerCase(), new LoadedPlugin(plugin, jar, loader));
        for (Map.Entry<String, Object> e : desc.permissionsSection().entrySet()) {
            if (e.getValue() instanceof Map<?, ?> meta) {
                Object def = ((Map<?, ?>) meta).get("default");
                PermissionManager.register(
                        desc.name().toLowerCase() + "." + e.getKey(),
                        parseDefault(def));
            }
        }
        plugin.setEnabled(true);
        plugin.onEnable();
        System.out.println("[插件] 已启用 " + desc.name() + " v" + desc.version()
                + (desc.authors().isEmpty() ? "" : " by " + String.join(", ", desc.authors())));
        for (Map.Entry<String, Object> e : desc.commandsSection().entrySet()) {
            if (e.getValue() instanceof Map<?, ?> meta) {
                @SuppressWarnings("unchecked")
                Map<String, Object> cmdMeta = (Map<String, Object>) meta;
                String usage = String.valueOf(cmdMeta.getOrDefault("description", ""));
                System.out.println("[插件] " + desc.name() + " 声明命令 /" + e.getKey() + " " + usage
                        + " (需在 onEnable 中 registerCommand 生效)");
            }
        }
    }

    public void disablePlugin(String name) {
        LoadedPlugin lp = plugins.remove(name.toLowerCase());
        if (lp == null) return;
        disableSafely(lp);
    }

    private void disableSafely(LoadedPlugin lp) {
        try {
            ServerScheduler.INSTANCE.cancelTasks(
                    ServerScheduler.ref(lp.plugin));
            unregisterCommandsOf(lp.plugin);
            EventManager.INSTANCE.unregisterPlugin(lp.plugin);
            PermissionManager.unregisterPlugin(lp.plugin.getName());
            lp.plugin.setEnabled(false);
            lp.plugin.onDisable();
        } catch (Throwable t) {
            System.err.println("[插件] 禁用 " + lp.plugin.getName() + " 异常: " + t);
        }
        try {
            lp.classLoader.close();
        } catch (IOException ignored) {}
        System.out.println("[插件] 已禁用 " + lp.plugin.getName());
    }

    private static PermissionManager.Default parseDefault(Object def) {
        if (def == null) return PermissionManager.Default.OP;
        return switch (String.valueOf(def).toLowerCase()) {
            case "true", "everyone" -> PermissionManager.Default.EVERYONE;
            case "false", "nobody" -> PermissionManager.Default.NOBODY;
            default -> PermissionManager.Default.OP;
        };
    }

    public void disableAll() {
        for (String name : new ArrayList<>(plugins.keySet())) {
            disablePlugin(name);
        }
    }

    public void reloadPlugins() {
        disableAll();
        commands.clear();
        aliasToCommand.clear();
        loadPlugins(new File("plugins"));
    }

    private static PluginDescription readDescription(File jar) throws IOException {
        try (JarFile jf = new JarFile(jar)) {
            JarEntry entry = jf.getJarEntry("plugin.yml");
            if (entry == null) entry = jf.getJarEntry("plugin.yaml");
            if (entry == null) throw new IOException("缺少 plugin.yml");
            try (InputStream in = jf.getInputStream(entry)) {
                String text = new String(in.readAllBytes(), StandardCharsets.UTF_8);
                return PluginDescription.fromYaml(Yaml.parse(text));
            }
        }
    }

    private List<PluginDescription> topologicalSort(List<PluginDescription> list) {
        List<PluginDescription> result = new ArrayList<>();
        List<PluginDescription> pending = new ArrayList<>(list);
        while (!pending.isEmpty()) {
            boolean progressed = false;
            for (PluginDescription desc : new ArrayList<>(pending)) {
                boolean satisfied = true;
                for (String dep : desc.depends()) {
                    boolean loaded = plugins.containsKey(dep.toLowerCase());
                    boolean inResult = result.stream().anyMatch(d -> d.name().equalsIgnoreCase(dep));
                    boolean inPending = pending.stream().anyMatch(d -> d.name().equalsIgnoreCase(dep) && d != desc);
                    if (!loaded && !inResult && !inPending) {
                        System.err.println("[插件] " + desc.name() + " 缺少硬依赖 " + dep + ", 跳过");
                        pending.remove(desc);
                        satisfied = false;
                        break;
                    }
                    if (!loaded && !inResult) {
                        satisfied = false;
                        break;
                    }
                }
                if (satisfied && pending.contains(desc)) {
                    result.add(desc);
                    pending.remove(desc);
                    progressed = true;
                }
            }
            if (!progressed) {
                System.err.println("[插件] 依赖循环: " + pending.stream().map(PluginDescription::name).toList()
                        + ", 按声明顺序加载");
                result.addAll(pending);
                pending.clear();
            }
        }
        return result;
    }

    private void broadcastCommandsRefresh() {
        NetworkHandler.refreshCommandsForAll();
    }
}
