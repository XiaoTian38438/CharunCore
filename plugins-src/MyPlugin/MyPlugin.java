package com.example;

import com.CharunCore.server.plugin.Plugin;
import com.CharunCore.server.plugin.event.*;
import com.CharunCore.server.plugin.event.events.*;

public class MyPlugin extends Plugin implements EventListener {

    @Override
    public void onEnable() {
        getLogger().info("Hello!");
        registerEvents(this);

        registerCommand("hello", "问好命令", (sender, label, args) -> {
            sender.sendMessage("§aHello, " + sender.getName() + "!");
            return true;
        }, "hi");

        getScheduler().runTaskTimer(
                com.CharunCore.server.plugin.scheduler.ServerScheduler.ref(this),
                () -> getLogger().info("每 100 tick 打一次"),
                100L, 100L);
    }

    @Override
    public void onDisable() {
        getLogger().info("Bye!");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage("§e" + event.getPlayer().username + " 来了!");
    }

    @EventHandler(ignoreCancelled = true)
    public void onChat(PlayerChatEvent event) {
        if (event.getMessage().contains("广告")) event.setCancelled(true);
    }
}