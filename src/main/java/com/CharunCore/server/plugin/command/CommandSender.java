package com.CharunCore.server.plugin.command;

public interface CommandSender {

    String getName();

    void sendMessage(String message);

    boolean hasPermission(String permission);

    default boolean isPlayer() {
        return false;
    }
}
