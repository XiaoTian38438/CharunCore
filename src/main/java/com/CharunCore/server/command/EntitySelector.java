package com.CharunCore.server.command;

import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.world.entity.Entity;
import com.CharunCore.server.world.entity.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @a / @p / @r / @s / @e 目标选择器解析层。
 * 与原版 CommandSourceStack 选择器语义对齐（best-effort）：
 *   @a 全部在线玩家
 *   @p 最近玩家（控制台无执行源位置，退化为全部在线玩家）
 *   @r 随机一名在线玩家（无玩家时退化为随机实体）
 *   @s 执行源自身（控制台无自身，返回空）
 *   @e 全部世界实体
 * 非 @ 开头按精确用户名匹配在线玩家。
 */
public final class EntitySelector {

    private static final Random RNG = new Random();

    private EntitySelector() {}

    /** 解析为在线玩家列表（@e 不返回，留给实体型指令处理）。 */
    public static List<NetworkHandler> resolvePlayers(String token, NetworkHandler source) {
        List<NetworkHandler> result = new ArrayList<>();
        if (token == null) return result;
        String t = token.trim();
        if (t.isEmpty()) return result;
        if (t.charAt(0) == '@' && t.length() > 1) {
            switch (t.charAt(1)) {
                case 'a' -> result.addAll(NetworkHandler.players.values());
                case 'p' -> result.addAll(NetworkHandler.players.values());
                case 'r' -> {
                    List<NetworkHandler> all = new ArrayList<>(NetworkHandler.players.values());
                    if (!all.isEmpty()) result.add(all.get(RNG.nextInt(all.size())));
                }
                case 's' -> { if (source != null) result.add(source); }
                default -> { }
            }
        } else {
            NetworkHandler p = NetworkHandler.player(t);
            if (p != null) result.add(p);
        }
        return result;
    }

    /** 解析为玩家或实体目标（含 @e）。 */
    public static List<CommandTarget> resolveTargets(String token, NetworkHandler source) {
        List<CommandTarget> result = new ArrayList<>();
        if (token == null) return result;
        String t = token.trim();
        if (t.isEmpty()) return result;
        if (t.charAt(0) == '@' && t.length() > 1) {
            switch (t.charAt(1)) {
                case 'a' -> {
                    for (NetworkHandler p : NetworkHandler.players.values()) result.add(new CommandTarget.Player(p));
                }
                case 'p' -> {
                    for (NetworkHandler p : NetworkHandler.players.values()) result.add(new CommandTarget.Player(p));
                }
                case 'r' -> {
                    List<NetworkHandler> all = new ArrayList<>(NetworkHandler.players.values());
                    if (!all.isEmpty()) {
                        result.add(new CommandTarget.Player(all.get(RNG.nextInt(all.size()))));
                    } else {
                        List<Entity> ents = new ArrayList<>(EntityManager.getAllEntities());
                        if (!ents.isEmpty()) result.add(new CommandTarget.EntityT(ents.get(RNG.nextInt(ents.size()))));
                    }
                }
                case 's' -> { if (source != null) result.add(new CommandTarget.Player(source)); }
                case 'e' -> {
                    for (Entity e : EntityManager.getAllEntities()) result.add(new CommandTarget.EntityT(e));
                }
                default -> { }
            }
        } else {
            NetworkHandler p = NetworkHandler.player(t);
            if (p != null) result.add(new CommandTarget.Player(p));
        }
        return result;
    }
}
