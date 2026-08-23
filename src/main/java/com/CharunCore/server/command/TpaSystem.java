package com.CharunCore.server.command;

import com.CharunCore.server.network.NetworkHandler;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class TpaSystem {

    private static final long EXPIRY_MS = 60_000L;

    private record Request(NetworkHandler from, boolean toSenderOfRequest, long time) {}

    private static final Map<UUID, Deque<Request>> pending = new ConcurrentHashMap<>();

    private TpaSystem() {}

    /** to=接受方。type: "tpa"=请求者想去对方那; "tpahere"=请求对方来自己这。 */
    public static void request(NetworkHandler from, NetworkHandler to, boolean here) {
        if (from == to) {
            from.sendFeedback("不能对自己发起传送请求", "red");
            return;
        }
        Request req = new Request(from, here, System.currentTimeMillis());
        pending.computeIfAbsent(to.uuid, k -> new ArrayDeque<>()).addLast(req);
        if (here) {
            to.sendFeedback("[传送请求] " + from.username + " 请求把你传送到 TA 身边, /tpaccept 接受, /tpdeny 拒绝 (60秒有效)", "yellow");
        } else {
            to.sendFeedback("[传送请求] " + from.username + " 请求传送到你身边, /tpaccept 接受, /tpdeny 拒绝 (60秒有效)", "yellow");
        }
        from.sendFeedback("已向 " + to.username + " 发送传送请求", "green");
    }

    public static boolean accept(NetworkHandler to) {
        Request req = takeValid(to);
        if (req == null) return false;
        NetworkHandler from = req.from();
        if (from == null || from.ctx == null || !from.ctx.channel().isActive()) {
            to.sendFeedback("请求发起者已离线", "red");
            return true;
        }
        if (req.toSenderOfRequest()) {
            teleport(from, to);
            from.sendFeedback(to.username + " 接受了你的请求, 正在传送对方", "green");
            to.sendFeedback("已接受, " + from.username + " 正在传送到你身边", "green");
        } else {
            teleport(from, to);
            from.sendFeedback(to.username + " 接受了你的请求, 正在传送", "green");
            to.sendFeedback("已接受 " + from.username + " 的传送请求", "green");
        }
        return true;
    }

    public static boolean deny(NetworkHandler to) {
        Request req = takeValid(to);
        if (req == null) return false;
        if (req.from() != null && req.from().ctx != null && req.from().ctx.channel().isActive()) {
            req.from().sendFeedback(to.username + " 拒绝了你的传送请求", "red");
        }
        to.sendFeedback("已拒绝传送请求", "green");
        return true;
    }

    private static void teleport(NetworkHandler from, NetworkHandler to) {
        NetworkHandler.teleportPlayer(from, to.x, to.y, to.z);
    }

    private static Request takeValid(NetworkHandler to) {
        Deque<Request> queue = pending.get(to.uuid);
        if (queue == null) return null;
        long now = System.currentTimeMillis();
        while (!queue.isEmpty()) {
            Request req = queue.pollLast();
            if (now - req.time() <= EXPIRY_MS) {
                if (queue.isEmpty()) pending.remove(to.uuid);
                return req;
            }
        }
        pending.remove(to.uuid);
        return null;
    }

    public static void onQuit(NetworkHandler player) {
        pending.remove(player.uuid);
        for (Deque<Request> queue : pending.values()) {
            synchronized (queue) {
                queue.removeIf(r -> r.from() == player);
            }
        }
    }

    public static int pendingCount(NetworkHandler player) {
        Deque<Request> queue = pending.get(player.uuid);
        return queue == null ? 0 : queue.size();
    }
}
