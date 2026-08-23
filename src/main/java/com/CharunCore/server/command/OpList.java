package com.CharunCore.server.command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * OP 权限等级与持久化（ops.json）。等级语义:
 *   Lv5 服务器开发+总负责人: 全部命令, 修改他人OP, 关服, 改配置, ban/unban
 *   Lv4 正式管理: ban/unban, 踢人, 队伍传送, 管理员菜单
 *   Lv3 见习管理: 禁言, tp, 查看玩家数据
 *   Lv2 值班协助: 自身飞行, tp, 把玩家传送到身边
 *   Lv1 玩家巡查员: 临时踢出, tp到玩家位置, 全服列表
 * ops.json 为空时所有玩家视作 Lv5(开发模式)。
 */
public final class OpList {

    private static final Path FILE = Path.of("ops.json");
    private static final Map<String, Integer> OPS = new ConcurrentHashMap<>();
    private static final Map<UUID, Integer> OPS_BY_UUID = new ConcurrentHashMap<>();
    private static volatile boolean loaded = false;

    private OpList() {}

    private static void ensureLoaded() {
        if (loaded) return;
        synchronized (OpList.class) {
            if (loaded) return;
            OPS.clear();
            OPS_BY_UUID.clear();
            if (Files.exists(FILE)) {
                try {
                    parse(Files.readString(FILE));
                } catch (IOException e) {
                    System.err.println("[OpList] 读取 ops.json 失败: " + e.getMessage());
                }
            }
            loaded = true;
        }
    }

    private static void parse(String json) {
        int start = json.indexOf('[');
        if (start < 0) return;
        String body = json.substring(start + 1);
        int end = body.lastIndexOf(']');
        if (end >= 0) body = body.substring(0, end);
        for (String obj : splitTopLevel(body)) {
            String name = field(obj, "name");
            String lvl = field(obj, "level");
            String uuid = field(obj, "uuid");
            if (name != null && !name.isEmpty()) {
                int level = 4;
                try { level = Integer.parseInt(lvl); } catch (NumberFormatException ignored) { }
                OPS.put(name.toLowerCase(), clamp(level));
                if (uuid != null && !uuid.isEmpty()) {
                    try {
                        OPS_BY_UUID.put(UUID.fromString(uuid), clamp(level));
                    } catch (IllegalArgumentException ignored) {}
                }
            }
        }
    }

    private static int clamp(int level) {
        return Math.max(0, Math.min(5, level));
    }

    private static java.util.List<String> splitTopLevel(String s) {
        java.util.List<String> out = new java.util.ArrayList<>();
        int depth = 0, start = -1;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '{') {
                if (depth == 0) start = i;
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0 && start >= 0) {
                    out.add(s.substring(start, i + 1));
                    start = -1;
                }
            }
        }
        return out;
    }

    private static String field(String obj, String key) {
        int i = obj.indexOf("\"" + key + "\"");
        if (i < 0) return null;
        int c = obj.indexOf(':', i);
        if (c < 0) return null;
        int q = obj.indexOf('"', c);
        if (q < 0) {
            int e = obj.indexOf(',', c);
            int m = obj.indexOf('}', c);
            int end = (e < 0) ? m : (m < 0 ? e : Math.min(e, m));
            if (end < 0) end = obj.length();
            return obj.substring(c + 1, end).trim();
        }
        int q2 = obj.indexOf('"', q + 1);
        if (q2 < 0) return null;
        return obj.substring(q + 1, q2);
    }

    private static void save() {
        StringBuilder sb = new StringBuilder("[\n");
        boolean first = true;
        for (Map.Entry<String, Integer> e : OPS.entrySet()) {
            if (!first) sb.append(",\n");
            first = false;
            UUID uuid = null;
            for (Map.Entry<UUID, Integer> u : OPS_BY_UUID.entrySet()) {
                if (u.getValue().equals(e.getValue()) && !takenByOther(sb.toString(), u.getKey(), e.getKey())) {
                    uuid = u.getKey();
                    break;
                }
            }
            sb.append("  {\"name\":\"").append(e.getKey())
              .append("\",\"level\":").append(e.getValue());
            if (uuid != null) sb.append(",\"uuid\":\"").append(uuid).append("\"");
            sb.append("}");
        }
        sb.append("\n]\n");
        try {
            Files.writeString(FILE, sb.toString());
        } catch (IOException ex) {
            System.err.println("[OpList] 写入 ops.json 失败: " + ex.getMessage());
        }
    }

    private static boolean takenByOther(String json, UUID uuid, String name) {
        return false;
    }

    public static void addOp(String name, int level) {
        addOp(null, name, level);
    }

    public static void addOp(UUID uuid, String name, int level) {
        ensureLoaded();
        OPS.put(name.toLowerCase(), clamp(level));
        if (uuid != null) OPS_BY_UUID.put(uuid, clamp(level));
        save();
    }

    public static void removeOp(String name) {
        ensureLoaded();
        Integer lvl = OPS.remove(name.toLowerCase());
        if (lvl != null) {
            OPS_BY_UUID.values().removeIf(v -> v.equals(lvl));
        }
        save();
    }

    public static boolean isOp(String name) {
        ensureLoaded();
        return OPS.containsKey(name.toLowerCase());
    }

    /** 玩家权限等级: 优先 UUID 匹配(防改名漂移), 回退用户名; 无任何 OP 记录时为 5(开发模式)。 */
    public static int level(UUID uuid, String name) {
        ensureLoaded();
        if (OPS.isEmpty() && OPS_BY_UUID.isEmpty()) return 5;
        if (uuid != null) {
            Integer byUuid = OPS_BY_UUID.get(uuid);
            if (byUuid != null) return byUuid;
        }
        Integer l = OPS.get(name == null ? "" : name.toLowerCase());
        return l == null ? 0 : l;
    }

    public static int level(String name) {
        return level(null, name);
    }

    public static Collection<String> names() {
        ensureLoaded();
        return OPS.keySet();
    }
}
