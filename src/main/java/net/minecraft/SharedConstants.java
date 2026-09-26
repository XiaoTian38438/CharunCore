package net.minecraft;

/**
 * 本地桩实现，替代损坏的 com.mojang:minecraft-server 依赖中缺失的原版类。
 * 本服务器核心为自研重实现，拥有独立注册表(BlockManager/BlockStateHelper 等)，
 * 不依赖 Mojang NMS 的注册表引导，故此处提供 no-op 以满足 Main 的启动调用。
 */
public final class SharedConstants {
    public static final String VERSION_STRING = "1.21.11";
    public static final int PROTOCOL_VERSION = 774;

    private SharedConstants() {}

    /** 原版用于探测游戏版本；本核心为自研实现，无需探测，no-op。 */
    public static void tryDetectVersion() {
        // no-op
    }
}
