package com.example.essentials;

import com.CharunCore.server.plugin.Plugin;
import com.CharunCore.server.plugin.api.Inventory;
import com.CharunCore.server.plugin.api.InventoryClickContext;
import com.CharunCore.server.plugin.api.ItemMeta;
import com.CharunCore.server.plugin.api.ItemStack;
import com.CharunCore.server.plugin.api.Player;
import com.CharunCore.server.plugin.api.PersistentDataContainer;
import com.CharunCore.server.plugin.api.World;
import com.CharunCore.server.plugin.command.CommandExecutor;
import com.CharunCore.server.plugin.command.CommandSender;
import com.CharunCore.server.plugin.event.EventListener;
import com.CharunCore.server.plugin.event.EventHandler;
import com.CharunCore.server.plugin.event.events.PlayerJoinEvent;

import java.util.List;

/**
 * 示例: 传送菜单 GUI + 个人主页 (PDC 持久化)。
 * 演示 Inventory / ItemMeta / PersistentDataContainer / 命令 / 事件。
 */
public class EssentialsPlugin extends Plugin implements EventListener {

    @Override
    public void onEnable() {
        registerEvents(this);

        registerCommand("menu", "打开传送菜单", (sender, label, args) -> {
            if (!sender.isPlayer()) return true;
            openTeleportMenu((Player) sender);
            return true;
        });

        registerCommand("sethome", "设置个人主页", (sender, label, args) -> {
            if (!sender.isPlayer()) return true;
            Player p = (Player) sender;
            PersistentDataContainer pdc = p.getPersistentDataContainer();
            pdc.setString("essentials.home.x", String.valueOf((int) p.getX()));
            pdc.setString("essentials.home.y", String.valueOf((int) p.getY()));
            pdc.setString("essentials.home.z", String.valueOf((int) p.getZ()));
            pdc.setString("essentials.home.dim", p.getWorld().key);
            p.sendMessage("§a主页已设置 (" + (int) p.getX() + ", " + (int) p.getY() + ", " + (int) p.getZ() + ")");
            return true;
        });

        registerCommand("home", "回到个人主页", (sender, label, args) -> {
            if (!sender.isPlayer()) return true;
            Player p = (Player) sender;
            PersistentDataContainer pdc = p.getPersistentDataContainer();
            if (!pdc.has("essentials.home.x")) {
                p.sendMessage("§c还没有设置主页, 先用 /sethome");
                return true;
            }
            int x = pdc.getInt("essentials.home.x", 0);
            int y = pdc.getInt("essentials.home.y", 64);
            int z = pdc.getInt("essentials.home.z", 0);
            World w = "minecraft:the_nether".equals(pdc.getString("essentials.home.dim", ""))
                    ? World.nether()
                    : "minecraft:the_end".equals(pdc.getString("essentials.home.dim", ""))
                    ? World.theEnd() : World.overworld();
            p.teleport(w, x + 0.5, y + 1, z + 0.5);
            p.sendMessage("§a已传送回主页");
            return true;
        });

        getLogger().info("EssentialsGUI 已启用");
    }

    /** 传送菜单: 27 格 GUI, 装饰玻璃 + 传送按钮。 */
    private void openTeleportMenu(Player p) {
        Inventory menu = Server().createInventory(27, "§8传送菜单");

        menu.fill(Inventory.decor("gray_stained_glass_pane", " "));

        menu.setItem(11, ItemStack.of("grass_block", 1)
                .withMeta(new ItemMeta()
                        .setDisplayName("§a主世界出生点")
                        .addLoreLine("§7点击传送到出生点")));
        menu.setItem(13, ItemStack.of("netherrack", 1)
                .withMeta(new ItemMeta()
                        .setDisplayName("§c下界")
                        .addLoreLine("§7点击传送 (8:1 坐标)")));
        menu.setItem(15, ItemStack.of("end_stone", 1)
                .withMeta(new ItemMeta()
                        .setDisplayName("§e末地")
                        .addLoreLine("§7点击传送到黑曜石平台")));

        menu.onClick((InventoryClickContext ctx) -> {
            Player pl = ctx.getPlayer();
            if (!ctx.inMenuArea()) return;          // 点在玩家背包区, 放行原生逻辑
            ctx.setCancelled(true);                 // 菜单格不可拿走
            int slot = ctx.getSlot();
            switch (slot) {
                case 11 -> {
                    pl.teleport(World.overworld(), 0.5, World.overworld().getHighestBlockYAt(0, 0) + 1.0, 0.5);
                    pl.sendMessage("§a已传送到主世界出生点");
                    pl.closeInventory();
                }
                case 13 -> {
                    double nx = Math.floor(pl.getX()) / 8.0;
                    double nz = Math.floor(pl.getZ()) / 8.0;
                    pl.teleport(World.nether(), nx, 80, nz);
                    pl.sendMessage("§c已传送到下界");
                    pl.closeInventory();
                }
                case 15 -> {
                    pl.teleport(World.theEnd(), 100, 49, 0.5);
                    pl.sendMessage("§e已传送到末地");
                    pl.closeInventory();
                }
                default -> {}
            }
        });

        menu.onClose(pl -> pl.sendActionBar("§7菜单已关闭"));
        p.openInventory(menu);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = Player.wrap(e.getPlayer());
        PersistentDataContainer pdc = p.getPersistentDataContainer();
        int visits = pdc.getInt("essentials.visits", 0) + 1;
        pdc.setInt("essentials.visits", visits);
        p.sendActionBar("§7欢迎回来, 第 " + visits + " 次进入服务器");
    }

    /** Server 门面引用。 */
    private com.CharunCore.server.plugin.Server Server() {
        return com.CharunCore.server.plugin.Server.get();
    }

    /** 提供给 CommandExecutor 的 List import 使用。 */
    @SuppressWarnings("unused")
    private static List<String> unused() { return List.of(); }
}
