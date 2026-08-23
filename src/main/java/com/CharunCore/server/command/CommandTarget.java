package com.CharunCore.server.command;

import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.world.entity.Entity;

/**
 * 统一目标抽象：玩家或世界实体，供 /tp 等指令以相同方式定位与传送。
 */
public sealed interface CommandTarget {

    double x();

    double y();

    double z();

    String name();

    void teleportTo(double tx, double ty, double tz);

    record Player(NetworkHandler h) implements CommandTarget {
        @Override public double x() { return h.x; }
        @Override public double y() { return h.y; }
        @Override public double z() { return h.z; }
        @Override public String name() { return h.username; }
        @Override public void teleportTo(double tx, double ty, double tz) {
            NetworkHandler.teleportPlayer(h, tx, ty, tz);
        }
    }

    record EntityT(Entity e) implements CommandTarget {
        @Override public double x() { return e.x; }
        @Override public double y() { return e.y; }
        @Override public double z() { return e.z; }
        @Override public String name() { return e.typeName; }
        @Override public void teleportTo(double tx, double ty, double tz) {
            e.x = tx;
            e.y = ty;
            e.z = tz;
        }
    }
}
