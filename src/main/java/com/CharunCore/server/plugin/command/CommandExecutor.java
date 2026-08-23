package com.CharunCore.server.plugin.command;

import java.util.List;

public interface CommandExecutor {

    boolean onCommand(CommandSender sender, String label, String[] args);

    default List<String> onTabComplete(CommandSender sender, String label, String[] args) {
        return List.of();
    }
}
