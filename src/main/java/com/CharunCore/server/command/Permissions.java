package com.CharunCore.server.command;

import java.util.Map;

/** 命令 → 最低 OP 等级 (Lv1-5, 0=所有玩家)。控制台恒为 5。 */
public final class Permissions {

    private static final Map<String, Integer> CMD_LEVEL = Map.ofEntries(
            // Lv5: 总负责人
            Map.entry("op", 5), Map.entry("deop", 5),
            Map.entry("stop", 5), Map.entry("end", 5), Map.entry("shutdown", 5),
            Map.entry("reloadplugins", 5), Map.entry("rp", 5),
            Map.entry("difficulty", 5), Map.entry("gamerule", 5), Map.entry("config", 5),
            // Lv4: 正式管理
            Map.entry("ban", 4), Map.entry("unban", 4), Map.entry("pardon", 4),
            Map.entry("banlist", 4), Map.entry("kick", 4), Map.entry("adminmenu", 4), Map.entry("admin", 4),
            // Lv3: 见习管理
            Map.entry("mute", 3), Map.entry("unmute", 3),
            Map.entry("invsee", 3), Map.entry("playerinfo", 3), Map.entry("enderchest", 3),
            // Lv2: 值班协助 (tp 通用/坐标/带目标传人)
            Map.entry("fly", 2), Map.entry("tphere", 2), Map.entry("tp2", 2),
            Map.entry("gamemode", 2), Map.entry("gm", 2),
            Map.entry("give", 2), Map.entry("clear", 2), Map.entry("heal", 2), Map.entry("feed", 2),
            Map.entry("time", 2), Map.entry("weather", 2), Map.entry("summon", 2),
            Map.entry("effect", 2), Map.entry("xp", 2), Map.entry("enchant", 2),
            Map.entry("setblock", 2), Map.entry("fill", 2), Map.entry("clone", 2),
            Map.entry("world", 2), Map.entry("spawnpoint", 2), Map.entry("kill", 2),
            Map.entry("sethome", 1), Map.entry("delhome", 1), Map.entry("home", 1),
            Map.entry("back", 2), Map.entry("workbench", 1), Map.entry("craft", 1),
            Map.entry("god", 3), Map.entry("repair", 2), Map.entry("rename", 2),
            Map.entry("top", 2), Map.entry("bottom", 2), Map.entry("suicide", 0),
            Map.entry("hat", 1), Map.entry("ping", 0), Map.entry("motd", 0), Map.entry("rules", 0),
            Map.entry("afk", 0), Map.entry("list", 1), Map.entry("msg", 0), Map.entry("tell", 0),
            Map.entry("w", 0), Map.entry("say", 3), Map.entry("me", 0),
            Map.entry("help", 0), Map.entry("seed", 0), Map.entry("tppa", 0),
            Map.entry("tempkick", 1)
    );

    /** 未列出的管理类命令默认需要 Lv4; 普通玩家命令 0。 */
    public static int requiredLevel(String cmd) {
        Integer level = CMD_LEVEL.get(cmd);
        if (level != null) return level;
        return 4;
    }
}
