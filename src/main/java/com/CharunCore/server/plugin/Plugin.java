package com.CharunCore.server.plugin;

import com.CharunCore.server.plugin.command.CommandExecutor;
import com.CharunCore.server.plugin.command.PluginCommand;
import com.CharunCore.server.plugin.event.EventManager;
import com.CharunCore.server.plugin.scheduler.ServerScheduler;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Plugin {

    private PluginDescription description;
    private File dataFolder;
    private File file;
    private Map<String, Object> config;
    private boolean enabled = false;

    public final void init(PluginDescription description, File file, File dataFolder) {
        this.description = description;
        this.file = file;
        this.dataFolder = dataFolder;
    }

    public abstract void onEnable();

    public void onDisable() {}

    public final PluginDescription getDescription() {
        return description;
    }

    public final String getName() {
        return description != null ? description.name() : getClass().getSimpleName();
    }

    public final File getDataFolder() {
        return dataFolder;
    }

    public final File getFile() {
        return file;
    }

    public final boolean isEnabled() {
        return enabled;
    }

    final void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public final PluginLogger getLogger() {
        return new PluginLogger(getName());
    }

    public final ServerScheduler getScheduler() {
        return ServerScheduler.INSTANCE;
    }

    public final Map<String, Object> getConfig() {
        if (config == null) reloadConfig();
        return config;
    }

    public final void reloadConfig() {
        File f = new File(dataFolder, "config.yml");
        if (f.exists()) {
            try {
                config = Yaml.parse(Files.readString(f.toPath(), StandardCharsets.UTF_8));
            } catch (IOException e) {
                getLogger().severe("读取 config.yml 失败: " + e.getMessage());
                config = new LinkedHashMap<>();
            }
        } else {
            config = new LinkedHashMap<>();
        }
    }

    public final void saveDefaultConfig() {
        dataFolder.mkdirs();
        File f = new File(dataFolder, "config.yml");
        if (f.exists()) return;
        try (var in = getClass().getResourceAsStream("/config.yml")) {
            if (in != null) Files.copy(in, f.toPath());
        } catch (IOException e) {
            getLogger().severe("释放默认 config.yml 失败: " + e.getMessage());
        }
    }

    public final void saveConfig() {
        dataFolder.mkdirs();
        try {
            Files.writeString(new File(dataFolder, "config.yml").toPath(), Yaml.dump(config == null ? new LinkedHashMap<>() : config),
                    StandardCharsets.UTF_8);
        } catch (IOException e) {
            getLogger().severe("写入 config.yml 失败: " + e.getMessage());
        }
    }

    public final void registerEvents(Object listener) {
        EventManager.INSTANCE.register(listener);
    }

    public final PluginCommand registerCommand(String name, String description,
                                               CommandExecutor executor, String... aliases) {
        return Server.get().getPluginManager().registerCommand(this, name, description, executor, aliases);
    }

    public static final class PluginLogger {
        private final String prefix;

        PluginLogger(String name) {
            this.prefix = "[" + name + "] ";
        }

        public void info(String message) {
            System.out.println(prefix + message);
        }

        public void warning(String message) {
            System.out.println(prefix + "[警告] " + message);
        }

        public void severe(String message) {
            System.err.println(prefix + "[错误] " + message);
        }
    }
}
