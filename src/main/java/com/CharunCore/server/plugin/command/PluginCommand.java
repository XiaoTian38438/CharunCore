package com.CharunCore.server.plugin.command;

import com.CharunCore.server.plugin.Plugin;

public final class PluginCommand {

    private final String name;
    private final String description;
    private final String permission;
    private final Plugin owner;
    private final CommandExecutor executor;
    private final String[] aliases;

    public PluginCommand(String name, String description, String permission, Plugin owner,
                         CommandExecutor executor, String[] aliases) {
        this.name = name;
        this.description = description == null ? "" : description;
        this.permission = permission;
        this.owner = owner;
        this.executor = executor;
        this.aliases = aliases == null ? new String[0] : aliases;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getPermission() { return permission; }
    public Plugin getOwner() { return owner; }
    public CommandExecutor getExecutor() { return executor; }
    public String[] getAliases() { return aliases; }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (permission != null && !permission.isEmpty() && !sender.hasPermission(permission)) {
            sender.sendMessage("§c你没有权限执行该指令");
            return true;
        }
        try {
            return executor.onCommand(sender, label, args);
        } catch (Exception e) {
            sender.sendMessage("§c指令执行异常: " + e.getMessage());
            owner.getLogger().severe("/" + name + " 执行异常: " + e);
            e.printStackTrace();
            return true;
        }
    }
}
