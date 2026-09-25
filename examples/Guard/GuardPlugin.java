package com.example.guard;

import java.util.List;

import com.CharunCore.server.plugin.Plugin;
import com.CharunCore.server.plugin.api.ItemStack;
import com.CharunCore.server.plugin.api.Player;
import com.CharunCore.server.plugin.command.CommandExecutor;
import com.CharunCore.server.plugin.command.CommandSender;
import com.CharunCore.server.plugin.event.EventHandler;
import com.CharunCore.server.plugin.event.EventListener;
import com.CharunCore.server.plugin.event.EventPriority;
import com.CharunCore.server.plugin.event.events.BlockBreakEvent;
import com.CharunCore.server.plugin.event.events.BlockBurnEvent;
import com.CharunCore.server.plugin.event.events.EntityExplodeEvent;
import com.CharunCore.server.plugin.event.events.PlayerJoinEvent;

/**
 * 示例: 服务器保护。
 * 演示事件取消(爆炸/火烧/破坏) + 出生点保护区 + 权限 + 物品元数据(保护魔杖)。
 */
public class GuardPlugin extends Plugin implements EventListener {

    /** 出生点保护半径 (方块)。 */
    private int spawnRadius = 32;
    /** 是否全服防爆。 */
    private boolean noExplosions = true;
    /** 是否全服防火烧蔓延。 */
    private boolean noFireSpread = true;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Object r = getConfig().get("spawn-radius");
        if (r instanceof Number n) spawnRadius = n.intValue();
        Object ne = getConfig().get("no-explosions");
        if (ne instanceof Boolean b) noExplosions = b;
        Object nf = getConfig().get("no-fire-spread");
        if (nf instanceof Boolean b) noFireSpread = b;

        registerEvents(this);

        registerCommand("guard", "Guard 配置", new CommandExecutor() {
            @Override public boolean onCommand(CommandSender s, String label, String[] args) {
                if (args.length >= 2 && args[0].equalsIgnoreCase("radius")) {
                    try {
                        spawnRadius = Integer.parseInt(args[1]);
                        s.sendMessage("§a出生点保护区半径: " + spawnRadius);
                        return true;
                    } catch (NumberFormatException ignored) {}
                }
                if (args.length >= 1 && args[0].equalsIgnoreCase("explosions")) {
                    noExplosions = !noExplosions;
                    s.sendMessage("§a防爆: " + (noExplosions ? "开" : "关"));
                    return true;
                }
                if (args.length >= 1 && args[0].equalsIgnoreCase("firespread")) {
                    noFireSpread = !noFireSpread;
                    s.sendMessage("§a防火蔓延: " + (noFireSpread ? "开" : "关"));
                    return true;
                }
                s.sendMessage("§e/guard radius <n> | explosions | firespread");
                return true;
            }
            @Override public List<String> onTabComplete(CommandSender s, String label, String[] args) {
                if (args.length == 1) return List.of("radius", "explosions", "firespread");
                return List.of();
            }
        }, "gd");

        getLogger().info("Guard 已启用 (radius=" + spawnRadius + ")");
    }

    private boolean inSpawnArea(int x, int z) {
        return Math.abs(x) <= spawnRadius && Math.abs(z) <= spawnRadius;
    }

    /** 取消出生点范围内破坏(玩家持金斧=保护魔杖则豁免)。 */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e) {
        if (inSpawnArea(e.getX(), e.getZ())) {
            Player p = Player.wrap(e.getPlayer());
            if (p != null && p.isOp()) {
                ItemStack held = p.getItemInMainHand();
                if (!held.isEmpty() && "golden_axe".equals(held.name())) return;
            }
            e.setCancelled(true);
            if (p != null) p.sendActionBar("§c出生点保护区, 无法破坏");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onExplode(EntityExplodeEvent e) {
        if (noExplosions) e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBurn(BlockBurnEvent e) {
        if (noFireSpread || inSpawnArea(e.getX(), e.getZ())) e.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = Player.wrap(e.getPlayer());
        if (p != null) {
            // 给 OP 发保护魔杖(带显示名与 lore 的物品元数据演示)
            if (p.isOp() && !p.getPersistentDataContainer().getBoolean("guard.wand", false)) {
                p.getPersistentDataContainer().setBoolean("guard.wand", true);
                p.giveItem(ItemStack.of("golden_axe", 1).withMeta(
                        new com.CharunCore.server.plugin.api.ItemMeta()
                                .setDisplayName("§a保护魔杖")
                                .addLoreLine("§7在出生点保护区手持可破坏方块")));
            }
        }
    }
}
