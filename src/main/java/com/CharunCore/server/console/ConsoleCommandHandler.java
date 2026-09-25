package com.CharunCore.server.console;

import com.CharunCore.server.command.BanList;
import com.CharunCore.server.plugin.Server;
import com.CharunCore.server.plugin.command.CommandSender;
import com.CharunCore.server.plugin.event.EventManager;
import com.CharunCore.server.plugin.event.events.PlayerKickEvent;
import com.CharunCore.server.Main;
import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.world.WorldManager;
import com.CharunCore.server.world.DimensionType;
import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.worldgen.structure2.StructureSet;
import com.CharunCore.server.world.entity.MobEntity;
import com.CharunCore.server.world.entity.EntityManager;
import com.CharunCore.server.command.EntitySelector;
import com.CharunCore.server.command.CommandTarget;
import com.CharunCore.server.command.OpList;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * 服务器终端(控制台)指令处理器。
 * 玩家能输入的所有 /指令 都可在此执行；玩家目标型指令必须显式指定 <玩家>。
 * 反馈输出到 System.out (即服务器终端)。
 */
public class ConsoleCommandHandler {

    private static void out(String msg) {
        System.out.println("[控制台] " + msg);
    }

    private static void err(String msg) {
        System.out.println("[控制台][错误] " + msg);
    }

    /** 按名字(不区分大小写)查找在线玩家 */
    public static NetworkHandler player(String name) {
        if (name == null) return null;
        for (NetworkHandler p : NetworkHandler.players.values()) {
            if (p.username != null && p.username.equalsIgnoreCase(name)) return p;
        }
        return null;
    }

    /** 经 @ 选择器解析目标玩家列表（P1-1）。 */
    private static List<NetworkHandler> resolvePlayers(String token) {
        return EntitySelector.resolvePlayers(token, null);
    }

    /** 解析目标返回首个匹配玩家（单目标指令用）。 */
    private static NetworkHandler firstTarget(String token) {
        List<NetworkHandler> list = resolvePlayers(token);
        return list.isEmpty() ? null : list.get(0);
    }

    /** 解析相对坐标：~ 表示基准值，~N 表示基准+N，否则绝对数值。 */
    private static double parseCoordRel(String s, double base) throws NumberFormatException {
        if (s.equals("~")) return base;
        if (s.startsWith("~")) return base + Double.parseDouble(s.substring(1));
        return Double.parseDouble(s);
    }

    /** 解析方块名与可选状态 [k=v,...]，返回方块状态 ID（未知方块返回 0）。 */
    private static int parseBlockState(String token) {
        String name = token.startsWith("minecraft:") ? token.substring(10) : token;
        Map<String, String> props = new LinkedHashMap<>();
        int bracket = name.indexOf('[');
        if (bracket >= 0) {
            String propsPart = name.substring(bracket + 1);
            name = name.substring(0, bracket);
            int end = propsPart.indexOf(']');
            if (end >= 0) propsPart = propsPart.substring(0, end);
            for (String kv : propsPart.split(",")) {
                int eq = kv.indexOf('=');
                if (eq > 0) props.put(kv.substring(0, eq).trim(), kv.substring(eq + 1).trim());
            }
        }
        int id = BlockStateHelper.getState(name, props);
        if (id == 0 && !name.equals("air")) id = BlockStateHelper.getDefault(name);
        return id;
    }

    private static DimensionType parseDim(String s) {
        return switch (s.toLowerCase()) {
            case "overworld", "0", "主世界" -> DimensionType.OVERWORLD;
            case "nether", "1", "下界"   -> DimensionType.THE_NETHER;
            case "end", "2", "末地"       -> DimensionType.THE_END;
            default -> null;
        };
    }

    /** 简易家点存储: key = 玩家名#家名(默认 home) -> 坐标 */
    private static final java.util.Map<String, double[]> HOMES = new java.util.concurrent.ConcurrentHashMap<>();

    private static final CommandSender CONSOLE_SENDER =
            new CommandSender() {
                @Override public String getName() { return "Console"; }
                @Override public void sendMessage(String message) { out(message); }
                @Override public boolean hasPermission(String permission) { return true; }
            };

    public static void execute(String raw) {
        String cmd = raw.startsWith("/") ? raw.substring(1) : raw;
        String[] parts = cmd.trim().split("\\s+");
        if (parts.length == 0 || parts[0].isEmpty()) return;
        String label = parts[0].toLowerCase();

        if (label.equals("plugins") || label.equals("pl")) {
            var plugins = Server.get().getPluginManager().getPlugins();
            if (plugins.isEmpty()) { out("没有已加载的插件 (放入 plugins/*.jar)"); return; }
            StringBuilder sb = new StringBuilder("插件 (" + plugins.size() + "): ");
            for (var p : plugins) sb.append(p.getName()).append(" v").append(p.getDescription().version()).append(", ");
            out(sb.substring(0, sb.length() - 2));
            return;
        }
        if (label.equals("reloadplugins") || label.equals("rp")) {
            out("正在重载插件...");
            Server.get().getPluginManager().reloadPlugins();
            out("插件重载完成, 当前 " + Server.get().getPluginManager().getPlugins().size() + " 个");
            return;
        }
        if (Server.get().getPluginManager().dispatchCommand(CONSOLE_SENDER, cmd)) {
            return;
        }

        try {
            switch (label) {

                // -------- 停止服务器 --------
                case "stop", "end", "shutdown" -> {
                    out("正在关闭服务器...");
                    Main.stopServer();
                }

                // -------- time --------
                case "time" -> {
                    if (parts.length < 2) { err("用法: time set|add|query <值>"); return; }
                    switch (parts[1].toLowerCase()) {
                        case "set" -> {
                            if (parts.length < 3) { err("用法: time set <值>"); return; }
                            long val = NetworkHandler.parseTimeValue(parts[2]);
                            if (val < 0) { err("未知时间值: " + parts[2]); return; }
                            Main.dayTime = val % 24000;
                            NetworkHandler.broadcastTime();
                            out("时间已设置为 " + Main.dayTime);
                        }
                        case "add" -> {
                            if (parts.length < 3) { err("用法: time add <ticks>"); return; }
                            try {
                                Main.dayTime = (Main.dayTime + Long.parseLong(parts[2])) % 24000;
                                NetworkHandler.broadcastTime();
                                out("时间已增加 " + parts[2] + " ticks");
                            } catch (NumberFormatException e) { err("无效数字: " + parts[2]); }
                        }
                        case "query" -> {
                            if (parts.length < 3) { err("用法: time query daytime|gametime|day"); return; }
                            switch (parts[2].toLowerCase()) {
                                case "daytime"  -> out("当前游戏时间: " + Main.dayTime);
                                case "gametime" -> out("总世界龄: " + Main.worldAge);
                                case "day"      -> out("当前天数: " + (Main.worldAge / 24000));
                                default         -> err("未知 query 类型: " + parts[2]);
                            }
                        }
                        default -> err("用法: time set|add|query <值>");
                    }
                }

                // -------- tp <目标> <x y z | 实体> [facing ...] --------
                case "tp" -> {
                    if (parts.length < 3) { err("用法: /tp <目标> <x y z | 实体> [facing ...]"); return; }
                    List<NetworkHandler> tgs = resolvePlayers(parts[1]);
                    if (tgs.isEmpty()) { err("无匹配目标: " + parts[1]); return; }
                    if (parts.length >= 5) {
                        try {
                            for (NetworkHandler t : tgs) {
                                double tx = parseCoordRel(parts[2], t.x);
                                double ty = parseCoordRel(parts[3], t.y);
                                double tz = parseCoordRel(parts[4], t.z);
                                NetworkHandler.teleportPlayer(t, tx, ty, tz);
                            }
                            String extra = parts.length > 5 ? " (facing/旋转参数已忽略)" : "";
                            out("已传送 " + tgs.size() + " 名目标到相对坐标 " + parts[2] + " " + parts[3] + " " + parts[4] + extra);
                        } catch (NumberFormatException e) {
                            err("无效坐标: " + parts[2] + " " + parts[3] + " " + parts[4]);
                        }
                    } else {
                        List<CommandTarget> dests = EntitySelector.resolveTargets(parts[2], null);
                        if (dests.isEmpty()) { err("无效目标/坐标: " + parts[2]); return; }
                        CommandTarget dest = dests.get(0);
                        for (NetworkHandler t : tgs) NetworkHandler.teleportPlayer(t, dest.x(), dest.y(), dest.z());
                        out("已传送 " + tgs.size() + " 名目标到 " + dest.name());
                    }
                }

                // -------- gamemode <模式> [目标] --------
                case "gamemode" -> {
                    if (parts.length < 3) { err("用法: /gamemode <模式> [目标]"); return; }
                    int mode = NetworkHandler.parseGameMode(parts[1]);
                    if (mode < 0) { err("未知游戏模式: " + parts[1]); return; }
                    List<NetworkHandler> tgs = resolvePlayers(parts[2]);
                    if (tgs.isEmpty()) { err("无匹配目标: " + parts[2]); return; }
                    String[] names = {"survival", "creative", "adventure", "spectator"};
                    for (NetworkHandler t : tgs) {
                        if (t.ctx != null) {
                            t.sendPacket(t.ctx, 0x26, pb -> { pb.writeByte(3); pb.writeFloat(mode); });
                        }
                        for (NetworkHandler h : NetworkHandler.players.values()) {
                            if (h.ctx == null) continue;
                            h.sendPacket(h.ctx, 0x44, pb -> {
                                pb.writeByte(0x04);
                                pb.writeVarInt(1);
                                pb.writeUUID(t.uuid);
                                pb.writeVarInt(mode);
                            });
                        }
                        t.gameMode = mode;
                        boolean allow = (mode == 1 || mode == 3);
                        if (t.allowFlight != allow) t.allowFlight = allow;
                        t.sendAbilitiesUpdate();
                        NetworkHandler.broadcastSystemMessage("[服务器] " + t.username + " 的游戏模式已改为 " + names[mode], "gray");
                    }
                }

                // -------- locate <结构> [玩家] --------
                case "locate" -> {
                    if (parts.length < 2) {
                        err("用法: /locate <结构名> [玩家]");
                        out("可用结构集: " + String.join(", ", StructureSet.loadAll().keySet()));
                        return;
                    }
                    String structureName = parts[1].toLowerCase();
                    NetworkHandler ctxPlayer = parts.length >= 3
                            ? firstTarget(parts[2])
                            : NetworkHandler.players.values().stream().findFirst().orElse(null);
                    if (ctxPlayer == null) { err("需要指定一名在线玩家用于定位维度"); return; }
                    int playerChunkX = ((int) ctxPlayer.x) >> 4;
                    int playerChunkZ = ((int) ctxPlayer.z) >> 4;
                    long seed = WorldManager.getSeed();
                    int[] result = ctxPlayer.locateStructure(structureName, playerChunkX, playerChunkZ, seed);
                    if (result == null) {
                        err("未找到结构: " + structureName);
                    } else {
                        int bx = result[0] * 16 + 8;
                        int bz = result[1] * 16 + 8;
                        out("找到 " + structureName + " 在 [" + bx + ", ~, " + bz
                                + "] (相对 " + (result[0] - playerChunkX) + ", " + (result[1] - playerChunkZ) + " 区块)");
                    }
                }

                // -------- give <目标> <物品> [数量] [components] --------
                case "give" -> {
                    if (parts.length < 3) { err("用法: /give <目标> <物品名> [数量] [components]"); return; }
                    List<NetworkHandler> tgs = resolvePlayers(parts[1]);
                    if (tgs.isEmpty()) { err("无匹配目标: " + parts[1]); return; }
                    String itemName = parts[2].startsWith("minecraft:") ? parts[2].substring(10) : parts[2];
                    int amount = 1;
                    if (parts.length >= 4) {
                        try { amount = Integer.parseInt(parts[3]); }
                        catch (NumberFormatException e) { err("无效数量: " + parts[3]); return; }
                    }
                    if (amount < 1) { err("数量必须 >= 1"); return; }
                    if (amount > 64) {
                        amount = 64;
                        out("数量已限制为单堆上限 64 (实际堆叠上限由 giveItem 内部再校验)");
                    }
                    int itemId = BlockManager.getItemIdByName(itemName);
                    if (itemId <= 0) { err("未知物品: " + itemName); return; }
                    if (parts.length >= 5) out("已忽略 components 参数 (best-effort 未实现)");
                    for (NetworkHandler t : tgs) {
                        t.giveItem(itemId, amount);
                        out("已给予 " + t.username + " " + amount + "x " + itemName);
                    }
                }

                // -------- clear <玩家> [物品] --------
                case "clear" -> {
                    if (parts.length < 2) { err("用法: /clear <玩家> [物品名]"); return; }
                    NetworkHandler t = firstTarget(parts[1]);
                    if (t == null) { err("玩家不在线: " + parts[1]); return; }
                    if (parts.length < 3) {
                        for (int i = 0; i < 46; i++) {
                            t.data.inventoryIds[i] = 0;
                            t.data.inventoryCounts[i] = 0;
                        }
                        t.sendInventoryUpdate();
                        out("已清空 " + t.username + " 的物品栏");
                    } else {
                        String itemName = parts[2].startsWith("minecraft:") ? parts[2].substring(10) : parts[2];
                        int itemId = BlockManager.getItemIdByName(itemName);
                        if (itemId <= 0) { err("未知物品: " + itemName); return; }
                        int cleared = 0;
                        for (int i = 0; i < 46; i++) {
                            if (t.data.inventoryIds[i] == itemId) {
                                cleared += t.data.inventoryCounts[i];
                                t.data.inventoryIds[i] = 0;
                                t.data.inventoryCounts[i] = 0;
                            }
                        }
                        t.sendInventoryUpdate();
                        out("已清除 " + t.username + " " + cleared + "x " + itemName);
                    }
                }

                // -------- kill <玩家> --------
                case "kill" -> {
                    if (parts.length < 2) { err("用法: /kill <玩家>"); return; }
                    NetworkHandler t = firstTarget(parts[1]);
                    if (t == null) { err("玩家不在线: " + parts[1]); return; }
                    t.health = 0;
                    t.isDead = true;
                    t.sendHealthUpdate();
                    t.sendDeathScreen(t.username + " 被击杀");
                    NetworkHandler.broadcastSystemMessage("[服务器] " + t.username + " 被击杀", "gray");
                }

                // -------- heal <玩家> --------
                case "heal" -> {
                    if (parts.length < 2) { err("用法: /heal <玩家>"); return; }
                    NetworkHandler t = firstTarget(parts[1]);
                    if (t == null) { err("玩家不在线: " + parts[1]); return; }
                    t.health = 20.0f;
                    t.data.food = 20;
                    t.sendHealthUpdate();
                    out("已治疗 " + t.username);
                }

                // -------- feed <玩家> --------
                case "feed" -> {
                    if (parts.length < 2) { err("用法: /feed <玩家>"); return; }
                    NetworkHandler t = firstTarget(parts[1]);
                    if (t == null) { err("玩家不在线: " + parts[1]); return; }
                    t.data.food = 20;
                    t.sendHealthUpdate();
                    out("已喂饱 " + t.username);
                }

                // -------- weather clear|rain|thunder [时长] --------
                case "weather" -> {
                    if (parts.length < 2) { err("用法: /weather clear|rain|thunder [时长]"); return; }
                    switch (parts[1].toLowerCase()) {
                        case "clear" -> {
                            Main.isRaining = false;
                            Main.isThundering = false;
                            Main.rainTarget = 0.0;
                            for (NetworkHandler p : NetworkHandler.players.values()) {
                                if (p.ctx == null) continue;
                                p.sendPacket(p.ctx, 0x26, pb -> { pb.writeByte(1); pb.writeFloat(0); });
                            }
                            out("天气正在转晴");
                        }
                        case "rain" -> {
                            Main.isRaining = true;
                            Main.isThundering = false;
                            Main.rainTarget = 1.0;
                            for (NetworkHandler p : NetworkHandler.players.values()) {
                                if (p.ctx == null) continue;
                                p.sendPacket(p.ctx, 0x26, pb -> { pb.writeByte(2); pb.writeFloat(1); });
                            }
                            out("天气正在转雨");
                        }
                        case "thunder" -> {
                            Main.isRaining = true;
                            Main.isThundering = true;
                            Main.rainTarget = 1.0;
                            for (NetworkHandler p : NetworkHandler.players.values()) {
                                if (p.ctx == null) continue;
                                p.sendPacket(p.ctx, 0x26, pb -> { pb.writeByte(7); pb.writeFloat(1); });
                                p.sendPacket(p.ctx, 0x26, pb -> { pb.writeByte(2); pb.writeFloat(1); });
                            }
                            out("天气正在转雷暴");
                        }
                        default -> err("未知天气: " + parts[1]);
                    }
                }

                // -------- summon <实体> <x y z | 玩家> --------
                case "summon" -> {
                    if (parts.length < 2) { err("用法: /summon <实体类型> <x y z | 玩家名>"); return; }
                    String entityType = parts[1].startsWith("minecraft:") ? parts[1].substring(10) : parts[1];
                    double sx = 0, sy = 64, sz = 0;
                    if (parts.length >= 5) {
                        try {
                            sx = Double.parseDouble(parts[2]);
                            sy = Double.parseDouble(parts[3]);
                            sz = Double.parseDouble(parts[4]);
                        } catch (NumberFormatException e) { err("无效坐标"); return; }
                    } else if (parts.length == 3) {
                        NetworkHandler t = firstTarget(parts[2]);
                        if (t == null) { err("玩家不在线或无效: " + parts[2]); return; }
                        sx = t.x; sy = t.y; sz = t.z;
                    } else {
                        err("用法: /summon <实体类型> <x y z | 玩家名>");
                        return;
                    }
                    if (entityType.equals("lightning_bolt")) {
                        out("闪电召唤功能待实现");
                    } else {
                        MobEntity mob = new MobEntity(EntityManager.allocateId(), entityType, sx, sy, sz);
                        EntityManager.addEntity(mob);
                        out("已召唤 " + entityType + " 于 " + sx + " " + sy + " " + sz);
                    }
                }

                // -------- xp <数量> <玩家> | experience add <数量> <玩家> --------
                case "xp", "experience" -> {
                    int amtIdx0 = 1;
                    if (label.equals("experience") && parts.length >= 2 && parts[1].equalsIgnoreCase("add")) {
                        amtIdx0 = 2;
                    }
                    final int amtIdx = amtIdx0;
                    if (parts.length <= amtIdx + 1) { err("用法: /xp <数量> <玩家>"); return; }
                    try {
                        int amount = Integer.parseInt(parts[amtIdx]);
                        NetworkHandler t = firstTarget(parts[amtIdx + 1]);
                        if (t == null) { err("玩家不在线: " + parts[amtIdx + 1]); return; }
                        t.addExperience(amount);
                        out("已给予 " + t.username + " " + amount + " 经验值");
                    } catch (NumberFormatException e) { err("无效数字"); }
                }

                // -------- enchant <玩家> <类型> [等级] --------
                case "enchant" -> {
                    if (parts.length < 3) { err("用法: /enchant <玩家> <附魔类型> [等级]"); return; }
                    NetworkHandler t = firstTarget(parts[1]);
                    if (t == null) { err("玩家不在线: " + parts[1]); return; }
                    out("附魔功能待实现: " + parts[2]);
                }

                // -------- effect give|clear <玩家> <效果> [秒] [放大器] --------
                case "effect" -> {
                    if (parts.length < 2) { err("用法: /effect give|clear <玩家> <效果> [秒] [放大器]"); return; }
                    if (parts[1].equalsIgnoreCase("clear")) {
                        if (parts.length < 3) { err("用法: /effect clear <玩家>"); return; }
                        NetworkHandler t = firstTarget(parts[2]);
                        if (t == null) { err("玩家不在线: " + parts[2]); return; }
                        t.clearEffects();
                        out("已清除 " + t.username + " 的所有药水效果");
                    } else if (parts[1].equalsIgnoreCase("give")) {
                        if (parts.length < 4) { err("用法: /effect give <玩家> <效果> [秒] [放大器]"); return; }
                        NetworkHandler t = firstTarget(parts[2]);
                        if (t == null) { err("玩家不在线: " + parts[2]); return; }
                        String effectName = parts[3];
                        int duration = parts.length >= 5 ? Integer.parseInt(parts[4]) * 20 : 600;
                        int amplifier = parts.length >= 6 ? Integer.parseInt(parts[5]) : 0;
                        t.addEffect(effectName, amplifier, duration);
                        out("已给予 " + t.username + " 效果 " + effectName + " " + (duration / 20) + "秒 等级" + amplifier);
                    } else {
                        err("用法: /effect give|clear <玩家> <效果> [秒] [放大器]");
                    }
                }

                // -------- seed --------
                case "seed" -> out("种子: " + WorldManager.getSeed());

                // -------- difficulty [<难度>] --------
                case "difficulty" -> {
                    if (parts.length < 2) { out("当前难度: " + NetworkHandler.difficultyName(Main.difficulty)); return; }
                    int diff = switch (parts[1].toLowerCase()) {
                        case "peaceful", "0" -> 0;
                        case "easy", "1"     -> 1;
                        case "normal", "2"   -> 2;
                        case "hard", "3"     -> 3;
                        default              -> -1;
                    };
                    if (diff < 0) { err("未知难度: " + parts[1]); return; }
                    Main.difficulty = diff;
                    for (NetworkHandler p : NetworkHandler.players.values()) {
                        if (p.ctx == null) continue;
                        p.sendPacket(p.ctx, 0x03, pb -> {
                            pb.writeByte(diff);
                            pb.writeByte(0);
                        });
                    }
                    out("难度已设为 " + NetworkHandler.difficultyName(diff));
                }

                // -------- setblock <x> <y> <z> <方块[状态]> [replace|destroy|keep] [维度] --------
                case "setblock" -> {
                    if (parts.length < 5) { err("用法: /setblock <x> <y> <z> <方块[状态]> [replace|destroy|keep] [维度]"); return; }
                    try {
                        int bx = Integer.parseInt(parts[1]);
                        int by = Integer.parseInt(parts[2]);
                        int bz = Integer.parseInt(parts[3]);
                        String blockToken = parts[4];
                        int blockId = parseBlockState(blockToken);
                        String base = blockToken.startsWith("minecraft:") ? blockToken.substring(10) : blockToken;
                        int bi = base.indexOf('[');
                        if (bi >= 0) base = base.substring(0, bi);
                        if (blockId == 0 && !base.equals("air")) { err("未知方块: " + blockToken); return; }
                        String mode = "replace";
                        DimensionType dim = DimensionType.OVERWORLD;
                        for (int i = 5; i < parts.length; i++) {
                            DimensionType d = parseDim(parts[i]);
                            if (d != null) dim = d;
                            else if (parts[i].equalsIgnoreCase("destroy")) mode = "destroy";
                            else if (parts[i].equalsIgnoreCase("keep")) mode = "keep";
                            else if (parts[i].equalsIgnoreCase("replace")) mode = "replace";
                        }
                        if (mode.equals("keep") && WorldManager.getBlockState(dim, bx, by, bz) != 0) {
                            out("keep 模式：目标位置非空气，跳过 (" + bx + " " + by + " " + bz + ")");
                            return;
                        }
                        WorldManager.setBlock(dim, bx, by, bz, blockId);
                        NetworkHandler.broadcastBlockChange(dim, bx, by, bz, blockId);
                        out("已设置方块 " + blockToken + " 在 " + bx + " " + by + " " + bz + " (" + dim + ", " + mode + ")");
                    } catch (NumberFormatException e) { err("无效坐标"); }
                }

                // -------- fill <x1> <y1> <z1> <x2> <y2> <z2> <方块[状态]> [replace <filter>|destroy|keep] [维度] --------
                case "fill" -> {
                    if (parts.length < 8) { err("用法: /fill <x1> <y1> <z1> <x2> <y2> <z2> <方块[状态]> [replace <filter>|destroy|keep] [维度]"); return; }
                    try {
                        int x1 = Integer.parseInt(parts[1]), y1 = Integer.parseInt(parts[2]), z1 = Integer.parseInt(parts[3]);
                        int x2 = Integer.parseInt(parts[4]), y2 = Integer.parseInt(parts[5]), z2 = Integer.parseInt(parts[6]);
                        String blockToken = parts[7];
                        int blockId = parseBlockState(blockToken);
                        String base = blockToken.startsWith("minecraft:") ? blockToken.substring(10) : blockToken;
                        int bi = base.indexOf('[');
                        if (bi >= 0) base = base.substring(0, bi);
                        if (blockId == 0 && !base.equals("air")) { err("未知方块: " + blockToken); return; }
                        String mode = "replace";
                        String filterName = null;
                        DimensionType dim = DimensionType.OVERWORLD;
                        for (int i = 8; i < parts.length; i++) {
                            DimensionType d = parseDim(parts[i]);
                            if (d != null) { dim = d; continue; }
                            String tok = parts[i].toLowerCase();
                            if (tok.equals("destroy")) mode = "destroy";
                            else if (tok.equals("keep")) mode = "keep";
                            else if (tok.equals("replace")) mode = "replace";
                            else if (mode.equals("replace") && filterName == null) filterName = parts[i];
                        }
                        int filterId = 0;
                        if (filterName != null) {
                            filterId = parseBlockState(filterName);
                            if (filterId == 0) { err("未知过滤方块: " + filterName); return; }
                        }
                        int minX = Math.min(x1, x2), maxX = Math.max(x1, x2);
                        int minY = Math.min(y1, y2), maxY = Math.max(y1, y2);
                        int minZ = Math.min(z1, z2), maxZ = Math.max(z1, z2);
                        int count = 0;
                        for (int bx = minX; bx <= maxX; bx++)
                            for (int by = minY; by <= maxY; by++)
                                for (int bz = minZ; bz <= maxZ; bz++) {
                                    int cur = WorldManager.getBlockState(dim, bx, by, bz);
                                    if (mode.equals("keep") && cur != 0) continue;
                                    if (filterName != null && cur != filterId) continue;
                                    WorldManager.setBlock(dim, bx, by, bz, blockId);
                                    NetworkHandler.broadcastBlockChange(dim, bx, by, bz, blockId);
                                    count++;
                                }
                        out("已填充 " + count + " 个方块 (" + dim + ", " + mode + (filterName != null ? " filter=" + filterName : "") + ")");
                    } catch (NumberFormatException e) { err("无效坐标"); }
                }

                // -------- clone <x1> <y1> <z1> <x2> <y2> <z2> <x> <y> <z> [维度] --------
                case "clone" -> {
                    if (parts.length < 10) { err("用法: /clone <x1> <y1> <z1> <x2> <y2> <z2> <x> <y> <z> [维度]"); return; }
                    try {
                        int x1 = Integer.parseInt(parts[1]), y1 = Integer.parseInt(parts[2]), z1 = Integer.parseInt(parts[3]);
                        int x2 = Integer.parseInt(parts[4]), y2 = Integer.parseInt(parts[5]), z2 = Integer.parseInt(parts[6]);
                        int dx = Integer.parseInt(parts[7]), dy = Integer.parseInt(parts[8]), dz = Integer.parseInt(parts[9]);
                        DimensionType dim = DimensionType.OVERWORLD;
                        if (parts.length >= 11) {
                            DimensionType d = parseDim(parts[10]);
                            if (d == null) { err("未知维度: " + parts[10]); return; }
                            dim = d;
                        }
                        int minX = Math.min(x1, x2), maxX = Math.max(x1, x2);
                        int minY = Math.min(y1, y2), maxY = Math.max(y1, y2);
                        int minZ = Math.min(z1, z2), maxZ = Math.max(z1, z2);
                        int offX = dx - minX, offY = dy - minY, offZ = dz - minZ;
                        for (int bx = minX; bx <= maxX; bx++)
                            for (int by = minY; by <= maxY; by++)
                                for (int bz = minZ; bz <= maxZ; bz++) {
                                    int state = WorldManager.getBlockState(dim, bx, by, bz);
                                    WorldManager.setBlock(dim, bx + offX, by + offY, bz + offZ, state);
                                    NetworkHandler.broadcastBlockChange(dim, bx + offX, by + offY, bz + offZ, state);
                                }
                        out("已克隆区域 (" + dim + ")");
                    } catch (NumberFormatException e) { err("无效坐标"); }
                }

                // -------- spawnpoint <玩家> [x y z] --------
                case "spawnpoint" -> {
                    if (parts.length < 2) { err("用法: /spawnpoint <玩家> [x y z]"); return; }
                    double spx, spy, spz;
                    if (parts.length >= 5) {
                        try {
                            spx = Integer.parseInt(parts[2]);
                            spy = Integer.parseInt(parts[3]);
                            spz = Integer.parseInt(parts[4]);
                        } catch (NumberFormatException e) { err("无效坐标"); return; }
                    } else {
                        NetworkHandler t = firstTarget(parts[1]);
                        if (t == null) { err("玩家不在线: " + parts[1]); return; }
                        spx = t.x; spy = t.y; spz = t.z;
                    }
                    WorldManager.setSpawnPoint((int) spx, (int) spy, (int) spz);
                    out("出生点已设为 " + (int) spx + " " + (int) spy + " " + (int) spz);
                }

                // -------- home <玩家> [家名] --------
                case "home" -> {
                    if (parts.length < 2) { err("用法: /home <玩家> [家名]"); return; }
                    NetworkHandler t = firstTarget(parts[1]);
                    if (t == null) { err("玩家不在线: " + parts[1]); return; }
                    String homeName = parts.length >= 3 ? parts[2] : "home";
                    double[] h = HOMES.get(t.username.toLowerCase() + "#" + homeName);
                    if (h != null) {
                        NetworkHandler.teleportPlayer(t, h[0], h[1], h[2]);
                        out("已传送 " + t.username + " 到家点 " + homeName);
                    } else {
                        double[] home = WorldManager.getSpawnPoint();
                        NetworkHandler.teleportPlayer(t, home[0] + 0.5, home[1], home[2] + 0.5);
                        out("已传送 " + t.username + " 到出生点");
                    }
                }

                // -------- sethome <玩家> [家名] --------
                case "sethome" -> {
                    if (parts.length < 2) { err("用法: /sethome <玩家> [家名]"); return; }
                    NetworkHandler t = firstTarget(parts[1]);
                    if (t == null) { err("玩家不在线: " + parts[1]); return; }
                    String homeName = parts.length >= 3 ? parts[2] : "home";
                    HOMES.put(t.username.toLowerCase() + "#" + homeName, new double[]{t.x, t.y, t.z});
                    out("已为 " + t.username + " 设置家点 " + homeName + " (" + (int) t.x + " " + (int) t.y + " " + (int) t.z + ")");
                }

                // -------- delhome <玩家> <家名> --------
                case "delhome" -> {
                    if (parts.length < 3) { err("用法: /delhome <玩家> <家名>"); return; }
                    NetworkHandler t = firstTarget(parts[1]);
                    if (t == null) { err("玩家不在线: " + parts[1]); return; }
                    String key = t.username.toLowerCase() + "#" + parts[2];
                    if (HOMES.remove(key) != null) out("已删除 " + t.username + " 的家点 " + parts[2]);
                    else err("未找到家点: " + parts[2]);
                }

                // -------- rename <玩家> <新名> --------
                case "rename" -> {
                    if (parts.length < 3) { err("用法: /rename <玩家> <新名字>"); return; }
                    NetworkHandler t = firstTarget(parts[1]);
                    if (t == null) { err("玩家不在线: " + parts[1]); return; }
                    String old = t.username;
                    t.username = parts[2];
                    NetworkHandler.broadcastSystemMessage("[服务器] " + old + " 已被重命名为 " + parts[2], "gray");
                }

                // -------- tphere <玩家A> <玩家B> : 把 A 传送到 B --------
                case "tphere" -> {
                    if (parts.length < 3) { err("用法: /tphere <玩家A> <玩家B>"); return; }
                    NetworkHandler a = firstTarget(parts[1]);
                    NetworkHandler b = firstTarget(parts[2]);
                    if (a == null) { err("玩家不在线: " + parts[1]); return; }
                    if (b == null) { err("玩家不在线: " + parts[2]); return; }
                    NetworkHandler.teleportPlayer(a, b.x, b.y, b.z);
                    out("已传送 " + a.username + " 到 " + b.username);
                }

                // -------- tpa / tpaccept / tpdeny --------
                case "tpa" -> {
                    if (parts.length < 2) { err("用法: tpa <玩家>"); return; }
                    NetworkHandler t = firstTarget(parts[1]);
                    if (t == null) { err("玩家不在线: " + parts[1]); return; }
                    t.sendFeedback("[传送请求] 控制台 请求把你传送到控制台指定位置身边 (控制台无法作为传送起点, 此请求仅作通知)", "yellow");
                    out("控制台无法发起真实传送请求, 请让玩家在游戏内使用 /tpa");
                }
                case "tpaccept" -> {
                    out("传送请求需由玩家在游戏内接受 (/tpaccept)");
                }
                case "tpdeny" -> {
                    out("传送请求需由玩家在游戏内拒绝 (/tpdeny)");
                }

                // -------- back <玩家> --------
                case "back" -> {
                    if (parts.length < 2) { err("用法: /back <玩家>"); return; }
                    NetworkHandler t = firstTarget(parts[1]);
                    if (t == null) { err("玩家不在线: " + parts[1]); return; }
                    if (t.lastDeathX != 0 || t.lastDeathY != 0 || t.lastDeathZ != 0) {
                        NetworkHandler.teleportPlayer(t, t.lastDeathX, t.lastDeathY, t.lastDeathZ);
                        out("已传送 " + t.username + " 到上次死亡位置");
                    } else {
                        err(t.username + " 没有可返回的死亡位置");
                    }
                }

                // -------- gamerule <规则> [值] --------
                case "gamerule" -> {
                    if (parts.length < 2) { err("用法: /gamerule <规则> [值]"); return; }
                    if (parts.length < 3) {
                        String val = WorldManager.getGameRule(parts[1]);
                        out(parts[1] + " = " + (val != null ? val : "(未设置)"));
                        return;
                    }
                    WorldManager.setGameRule(parts[1], parts[2]);
                    out("游戏规则 " + parts[1] + " 已设为 " + parts[2]);
                }

                // -------- kick <玩家> [原因] --------
                case "kick" -> {
                    if (parts.length < 2) { err("用法: /kick <玩家> [原因]"); return; }
                    NetworkHandler t = firstTarget(parts[1]);
                    if (t == null) { err("玩家不在线: " + parts[1]); return; }
                    String reason = parts.length >= 3 ? String.join(" ", Arrays.copyOfRange(parts, 2, parts.length)) : "已被踢出";
                    var kickEvent = EventManager.INSTANCE.fire(
                            new PlayerKickEvent(t, reason));
                    if (kickEvent.isCancelled()) { out("踢出被插件取消"); return; }
                    reason = kickEvent.getReason();
                    t.sendFeedback("§c你已被踢出: " + reason, "red");
                    if (t.ctx != null) t.ctx.channel().close();
                    NetworkHandler.broadcastSystemMessage("[服务器] " + t.username + " 被踢出: " + reason, "gray");
                }

                // -------- op <玩家> / deop <玩家> --------
                case "op" -> {
                    if (parts.length < 2) { err("用法: /op <玩家> [等级1-5, 默认4]"); return; }
                    int level = 4;
                    if (parts.length >= 3) {
                        try { level = Integer.parseInt(parts[2]); } catch (NumberFormatException e) { level = -1; }
                        if (level < 1 || level > 5) { err("等级范围 1-5"); return; }
                    }
                    List<NetworkHandler> tgs = resolvePlayers(parts[1]);
                    if (tgs.isEmpty()) {
                        OpList.addOp(parts[1], level);
                        out(parts[1] + " 已被授予 OP Lv" + level + " (离线, 按用户名)");
                        return;
                    }
                    for (NetworkHandler t : tgs) {
                        OpList.addOp(t.uuid, t.username, level);
                        t.sendFeedback("§6你已被授予 OP Lv" + level, "yellow");
                        NetworkHandler.broadcastSystemMessage("[服务器] " + t.username + " 已被授予 OP Lv" + level, "gray");
                    }
                }
                case "ops" -> {
                    if (OpList.names().isEmpty()) { out("ops.json 为空 (开发模式: 全员 Lv5)"); return; }
                    for (String n : OpList.names()) {
                        out("  " + n + " - Lv" + OpList.level(n));
                    }
                }
                case "ban" -> {
                    if (parts.length < 2) { err("用法: /ban <玩家> [原因]"); return; }
                    String reason = parts.length >= 3 ? String.join(" ", java.util.Arrays.copyOfRange(parts, 2, parts.length)) : "违反服务器规则";
                    List<NetworkHandler> tgs = resolvePlayers(parts[1]);
                    if (tgs.isEmpty()) {
                        BanList.ban(null, parts[1], reason, -1);
                        out(parts[1] + " 已被封禁 (离线)");
                        return;
                    }
                    for (NetworkHandler t : tgs) {
                        BanList.ban(t.uuid, t.username, reason, -1);
                        t.sendFeedback("§c你已被封禁: " + reason, "red");
                        if (t.ctx != null) t.ctx.close();
                        NetworkHandler.broadcastSystemMessage("[服务器] " + t.username + " 已被封禁: " + reason, "red");
                    }
                }
                case "unban", "pardon" -> {
                    if (parts.length < 2) { err("用法: /unban <玩家>"); return; }
                    if (BanList.unban(parts[1])) out("已解封 " + parts[1]);
                    else err("未找到封禁记录: " + parts[1]);
                }
                case "banlist" -> {
                    var entries = BanList.entries();
                    if (entries.isEmpty()) { out("没有封禁记录"); return; }
                    out("封禁列表 (" + entries.size() + "):");
                    for (var e : entries) {
                        out("  " + e.name() + (e.uuid() != null ? " (" + e.uuid() + ")" : "")
                                + " - " + e.reason()
                                + (e.expiresAtMs() > 0 ? " [限时至 " + new java.util.Date(e.expiresAtMs()) + "]" : " [永久]"));
                    }
                }
                case "deop" -> {
                    if (parts.length < 2) { err("用法: /deop <玩家>"); return; }
                    List<NetworkHandler> tgs = resolvePlayers(parts[1]);
                    if (tgs.isEmpty()) { err("无匹配目标: " + parts[1]); return; }
                    for (NetworkHandler t : tgs) {
                        OpList.removeOp(t.username);
                        NetworkHandler.broadcastSystemMessage("[服务器] " + t.username + " 已被移除OP权限", "gray");
                    }
                }

                // -------- list --------
                case "list" -> {
                    StringBuilder sb = new StringBuilder("在线玩家 (" + NetworkHandler.players.size() + "): ");
                    int i = 0;
                    for (NetworkHandler p : NetworkHandler.players.values()) {
                        if (i++ > 0) sb.append(", ");
                        sb.append(p.username);
                    }
                    out(sb.toString());
                }

                // -------- msg <玩家> <消息> (tell / w) --------
                case "msg", "tell", "w" -> {
                    if (parts.length < 3) { err("用法: /msg <玩家> <消息>"); return; }
                    NetworkHandler t = firstTarget(parts[1]);
                    if (t == null) { err("玩家不在线: " + parts[1]); return; }
                    String msg = String.join(" ", Arrays.copyOfRange(parts, 2, parts.length));
                    t.sendFeedback("[私聊] 控制台: " + msg, "gray");
                    out("[私聊] -> " + t.username + ": " + msg);
                }

                // -------- say <消息> | broadcast <消息> --------
                case "say", "broadcast" -> {
                    if (parts.length < 2) { err("用法: /say <消息>"); return; }
                    String msg = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
                    NetworkHandler.broadcastSystemMessage("[控制台] " + msg, "gray");
                }

                // -------- me <动作> --------
                case "me" -> {
                    if (parts.length < 2) { err("用法: /me <动作>"); return; }
                    String msg = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
                    NetworkHandler.broadcastSystemMessage("* 控制台 " + msg, "gray");
                }

                // -------- help [页码] --------
                case "help" -> {
                    int page = parts.length >= 2 ? Integer.parseInt(parts[1]) : 1;
                    String[] cmds = {
                        "/give <目标> <物品> [数量]", "/clear <目标> [物品]", "/tp <目标> <x y z|实体> [facing]", "/gamemode <模式> [目标]",
                        "/time set|add|query <值>", "/weather clear|rain|thunder", "/kill <玩家>",
                        "/heal <玩家>", "/feed <玩家>", "/summon <实体> <x y z|玩家>",
                        "/xp <数量> <玩家>", "/enchant <玩家> <类型> [等级]", "/effect give|clear <玩家> <效果> [秒] [放大器]",
                        "/seed", "/difficulty <难度>", "/setblock <x y z> <方块[状态]> [replace|destroy|keep] [维度]",
                        "/fill <x1 y1 z1 x2 y2 z2> <方块[状态]> [replace <filter>|destroy|keep] [维度]", "/clone <x1 y1 z1 x2 y2 z2> <x y z> [维度]",
                        "/spawnpoint <玩家> [x y z]", "/home <玩家>", "/back <玩家>", "/gamerule <规则> [值]",
                        "/kick <玩家>", "/op <玩家>", "/deop <玩家>", "/list",
                        "/msg <玩家> <消息>", "/say <消息>", "/me <动作>",
                        "/world <玩家> <overworld|nether|end>", "/fly <玩家> [on|off]", "/speed <玩家> <值>",
                        "/god <玩家> [on|off]", "/repair <玩家>", "/rename <玩家> <名称>",
                        "/top <玩家>", "/bottom <玩家>", "/sethome <玩家> [名称]", "/delhome <玩家> <名称>",
                        "/tphere <玩家>", "/tpa <玩家>", "/tpaccept", "/tpdeny",
                        "/invsee <玩家>", "/enderchest <玩家>", "/workbench <玩家>",
                        "/hat <玩家>", "/ping <玩家>", "/rules", "/motd", "/afk <玩家>", "/suicide <玩家>", "/stop"
                    };
                    int perPage = 10;
                    int totalPages = (cmds.length + perPage - 1) / perPage;
                    if (page < 1) page = 1;
                    if (page > totalPages) page = totalPages;
                    out("--- 控制台帮助 第 " + page + "/" + totalPages + " 页 ---");
                    int start = (page - 1) * perPage;
                    int end = Math.min(start + perPage, cmds.length);
                    for (int i = start; i < end; i++) out("  " + cmds[i]);
                }

                // -------- world <玩家> <overworld|nether|end> --------
                case "world" -> {
                    if (parts.length < 3) { err("用法: /world <玩家> <overworld|nether|end>"); return; }
                    NetworkHandler t = firstTarget(parts[1]);
                    if (t == null) { err("玩家不在线: " + parts[1]); return; }
                    DimensionType d = parseDim(parts[2]);
                    if (d == null) { err("未知维度: " + parts[2]); return; }
                    if (t.ctx == null) { err("该玩家通道不可用"); return; }
                    t.teleportToDimension(t.ctx, d);
                    out("已传送 " + t.username + " 到 " + d);
                }

                // -------- fly <玩家> [on|off] --------
                case "fly" -> {
                    if (parts.length < 2) { err("用法: /fly <玩家> [on|off]"); return; }
                    NetworkHandler t = firstTarget(parts[1]);
                    if (t == null) { err("玩家不在线: " + parts[1]); return; }
                    boolean fly = parts.length < 3 ? !t.allowFlight : parts[2].equalsIgnoreCase("on");
                    t.allowFlight = fly;
                    t.sendAbilitiesUpdate();
                    out("已" + (fly ? "启用" : "禁用") + " " + t.username + " 的飞行");
                }

                // -------- speed <玩家> <值> --------
                case "speed" -> {
                    if (parts.length < 3) { err("用法: /speed <玩家> <值>"); return; }
                    NetworkHandler t = firstTarget(parts[1]);
                    if (t == null) { err("玩家不在线: " + parts[1]); return; }
                    try {
                        float s = Float.parseFloat(parts[2]);
                        t.flySpeed = s;
                        t.walkSpeed = s;
                        t.sendAbilitiesUpdate();
                        out("已将 " + t.username + " 速度设为 " + s);
                    } catch (NumberFormatException e) { err("无效数字: " + parts[2]); }
                }

                // -------- god <玩家> [on|off] --------
                case "god" -> {
                    if (parts.length < 2) { err("用法: /god <玩家> [on|off]"); return; }
                    NetworkHandler t = firstTarget(parts[1]);
                    if (t == null) { err("玩家不在线: " + parts[1]); return; }
                    boolean g = parts.length < 3 ? !t.godMode : parts[2].equalsIgnoreCase("on");
                    t.godMode = g;
                    out("已" + (g ? "启用" : "禁用") + " " + t.username + " 的无敌模式");
                }

                // -------- top <玩家> --------
                case "top" -> {
                    if (parts.length < 2) { err("用法: /top <玩家>"); return; }
                    NetworkHandler t = firstTarget(parts[1]);
                    if (t == null) { err("玩家不在线: " + parts[1]); return; }
                    int tx = (int) t.x, tz = (int) t.z;
                    boolean found = false;
                    for (int ty = 319; ty >= -64; ty--) {
                        if (WorldManager.getBlockState(t.currentDim, tx, ty, tz) != 0) {
                            NetworkHandler.teleportPlayer(t, t.x, ty + 1, t.z);
                            out("已传送 " + t.username + " 到地表");
                            found = true;
                            break;
                        }
                    }
                    if (!found) err("未找到固体方块");
                }

                // -------- bottom <玩家> --------
                case "bottom" -> {
                    if (parts.length < 2) { err("用法: /bottom <玩家>"); return; }
                    NetworkHandler t = firstTarget(parts[1]);
                    if (t == null) { err("玩家不在线: " + parts[1]); return; }
                    NetworkHandler.teleportPlayer(t, t.x, -64, t.z);
                    out("已传送 " + t.username + " 到底部");
                }

                // -------- suicide <玩家> --------
                case "suicide" -> {
                    if (parts.length < 2) { err("用法: /suicide <玩家>"); return; }
                    NetworkHandler t = firstTarget(parts[1]);
                    if (t == null) { err("玩家不在线: " + parts[1]); return; }
                    t.health = 0;
                    t.isDead = true;
                    t.sendHealthUpdate();
                    t.sendDeathScreen(t.username + " 自杀了");
                    NetworkHandler.broadcastSystemMessage("[服务器] " + t.username + " 自杀了", "gray");
                }

                // -------- ping <玩家> --------
                case "ping" -> {
                    if (parts.length < 2) { err("用法: /ping <玩家>"); return; }
                    NetworkHandler t = firstTarget(parts[1]);
                    if (t == null) { err("玩家不在线: " + parts[1]); return; }
                    out(t.username + " 延迟: " + (System.currentTimeMillis() - t.lastPingTime) + "ms");
                }

                // -------- motd --------
                case "motd" -> {
                    out("=== " + Main.SERVER_NAME + " ===");
                    out("欢迎来到服务器!");
                    out("输入 /help 查看可用命令");
                }

                // -------- rules --------
                case "rules" -> {
                    out("=== 服务器规则 ===");
                    out("1. 禁止恶意破坏");
                    out("2. 禁止作弊/外挂");
                    out("3. 尊重其他玩家");
                    out("4. 禁止刷屏/广告");
                }

                // -------- afk <玩家> --------
                case "afk" -> {
                    if (parts.length < 2) { err("用法: /afk <玩家>"); return; }
                    NetworkHandler t = firstTarget(parts[1]);
                    if (t == null) { err("玩家不在线: " + parts[1]); return; }
                    t.isAfk = !t.isAfk;
                    NetworkHandler.broadcastSystemMessage("[服务器] " + t.username + (t.isAfk ? " 现在挂机" : " 回来了"), "gray");
                }

                // -------- repair <玩家> --------
                case "repair" -> {
                    if (parts.length < 2) { err("用法: /repair <玩家>"); return; }
                    NetworkHandler t = firstTarget(parts[1]);
                    if (t == null) { err("玩家不在线: " + parts[1]); return; }
                    int slot = 36 + t.heldItemSlot;
                    if (t.data.inventoryIds[slot] == 0) { err(t.username + " 手中没有物品"); return; }
                    out("已修复 " + t.username + " 手中物品(待实现)");
                }

                // -------- hat <玩家> --------
                case "hat" -> {
                    if (parts.length < 2) { err("用法: /hat <玩家>"); return; }
                    NetworkHandler t = firstTarget(parts[1]);
                    if (t == null) { err("玩家不在线: " + parts[1]); return; }
                    int slot = 36 + t.heldItemSlot;
                    if (t.data.inventoryIds[slot] == 0) { err(t.username + " 手中没有物品"); return; }
                    int headSlot = 5;
                    int tmpId = t.data.inventoryIds[headSlot];
                    int tmpCnt = t.data.inventoryCounts[headSlot];
                    t.data.inventoryIds[headSlot] = t.data.inventoryIds[slot];
                    t.data.inventoryCounts[headSlot] = t.data.inventoryCounts[slot];
                    t.data.inventoryIds[slot] = tmpId;
                    t.data.inventoryCounts[slot] = tmpCnt;
                    t.sendInventoryUpdate();
                    out("已为 " + t.username + " 戴上方块");
                }

                // -------- enderchest <玩家> | ec <玩家> --------
                case "enderchest", "ec" -> {
                    if (parts.length < 2) { err("用法: /enderchest <玩家>"); return; }
                    NetworkHandler t = firstTarget(parts[1]);
                    if (t == null) { err("玩家不在线: " + parts[1]); return; }
                    out("末影箱功能待实现");
                }

                // -------- workbench <玩家> | craft <玩家> --------
                case "workbench", "craft" -> {
                    if (parts.length < 2) { err("用法: /workbench <玩家>"); return; }
                    NetworkHandler t = firstTarget(parts[1]);
                    if (t == null) { err("玩家不在线: " + parts[1]); return; }
                    if (t.ctx == null) { err("该玩家通道不可用"); return; }
                    t.openCraftingTable();
                    out("已为 " + t.username + " 打开工作台");
                }

                // -------- invsee <玩家> --------
                case "invsee" -> {
                    if (parts.length < 2) { err("用法: /invsee <玩家>"); return; }
                    NetworkHandler t = firstTarget(parts[1]);
                    if (t == null) { err("玩家不在线: " + parts[1]); return; }
                    out(t.username + " 的物品栏:");
                    for (int i = 9; i < 45; i++) {
                        if (t.data.inventoryIds[i] > 0) {
                            String name = BlockManager.itemIdToName(t.data.inventoryIds[i]);
                            out("  [" + i + "] " + name + " x" + t.data.inventoryCounts[i]);
                        }
                    }
                }

                default -> err("未知命令: /" + label + " (输入 /help 查看可用命令)");
            }
        } catch (Exception e) {
            err("执行命令出错: " + e);
            e.printStackTrace();
        }
    }
}
