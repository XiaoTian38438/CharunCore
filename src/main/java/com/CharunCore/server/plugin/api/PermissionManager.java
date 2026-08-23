package com.CharunCore.server.plugin.api;

import com.CharunCore.server.network.NetworkHandler;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 插件权限节点: 插件注册节点 + 默认值(everyone|op|nobody), 玩家可有附加授权/撤销。
 * hasPermission = 附件显式值 > 注册默认值(op 节点要求 OP Lv>=1)。
 */
public final class PermissionManager {

    public enum Default { EVERYONE, OP, NOBODY }

    private static final Map<String, Default> NODES = new ConcurrentHashMap<>();
    private static final Map<UUID, Map<String, Boolean>> ATTACHMENTS = new ConcurrentHashMap<>();

    private PermissionManager() {}

    public static void register(String node, Default def) {
        NODES.put(node.toLowerCase(), def == null ? Default.OP : def);
    }

    public static void unregisterPlugin(String prefix) {
        NODES.keySet().removeIf(n -> n.startsWith(prefix.toLowerCase()));
        ATTACHMENTS.values().forEach(m -> m.keySet().removeIf(n -> n.startsWith(prefix.toLowerCase())));
    }

    public static void setPermission(NetworkHandler player, String node, boolean value) {
        if (player == null || player.uuid == null) return;
        ATTACHMENTS.computeIfAbsent(player.uuid, k -> new ConcurrentHashMap<>())
                .put(node.toLowerCase(), value);
    }

    public static void clearAttachments(NetworkHandler player) {
        if (player != null && player.uuid != null) ATTACHMENTS.remove(player.uuid);
    }

    public static boolean hasPermission(NetworkHandler player, String node) {
        if (player == null) return false;
        if (player.opLevel() >= 5) return true;
        node = node.toLowerCase();
        Map<String, Boolean> attachments = ATTACHMENTS.get(player.uuid);
        if (attachments != null) {
            Boolean explicit = attachments.get(node);
            if (explicit != null) return explicit;
        }
        Default def = NODES.get(node);
        if (def == null) return player.opLevel() >= 1;
        return switch (def) {
            case EVERYONE -> true;
            case OP -> player.opLevel() >= 1;
            case NOBODY -> false;
        };
    }

    public static Set<String> registeredNodes() {
        return NODES.keySet();
    }
}
