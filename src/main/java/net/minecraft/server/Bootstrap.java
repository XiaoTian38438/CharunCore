package net.minecraft.server;

/**
 * 本地桩实现，替代损坏的 com.mojang:minecraft-server 依赖中缺失的原版类。
 * 本服务器核心为自研重实现，拥有独立注册表与序列化体系，不依赖 Mojang NMS 的
 * 内置注册表引导(Bootstrap.bootStrap)，故此处提供 no-op 以满足 Main 的启动调用。
 */
public final class Bootstrap {
    private Bootstrap() {}

    /** 原版用于初始化内置注册表/数据修复器；本核心为自研实现，无需此步骤，no-op。 */
    public static void bootStrap() {
        // no-op
    }
}
