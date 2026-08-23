package com.CharunCore.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class ServerConfig {

    public static int serverPort = 25565;
    public static boolean onlineMode = false;
    public static boolean uuidFix = true;
    public static int compressionThreshold = 256;
    public static int viewDistance = 12;
    public static int simulationDistance = 8;
    public static int maxPlayers = 20;
    public static String motd = "§bCharunCore §7| §a生存服务器";
    public static String levelSeed = "";
    public static int difficulty = 1;
    public static String defaultGameMode = "survival";
    public static boolean allowNether = true;
    public static int spawnProtection = 16;
    public static boolean whiteList = false;

    private static final Path FILE = Path.of("server.properties");

    private ServerConfig() {}

    public static void load() {
        Properties props = new Properties();
        if (Files.exists(FILE)) {
            try (InputStream in = Files.newInputStream(FILE)) {
                props.load(new java.io.InputStreamReader(in, java.nio.charset.StandardCharsets.UTF_8));
            } catch (IOException e) {
                System.err.println("[配置] 读取 server.properties 失败, 使用默认值: " + e.getMessage());
            }
        }
        serverPort = getInt(props, "server-port", serverPort);
        onlineMode = getBool(props, "online-mode", onlineMode);
        uuidFix = getBool(props, "uuid-fix", uuidFix);
        compressionThreshold = getInt(props, "network-compression-threshold", compressionThreshold);
        viewDistance = clamp(getInt(props, "view-distance", viewDistance), 2, 32);
        simulationDistance = clamp(getInt(props, "simulation-distance", simulationDistance), 2, 32);
        maxPlayers = clamp(getInt(props, "max-players", maxPlayers), 1, 1000);
        motd = props.getProperty("motd", motd);
        levelSeed = props.getProperty("level-seed", levelSeed);
        difficulty = clamp(getInt(props, "difficulty", difficulty), 0, 3);
        defaultGameMode = props.getProperty("gamemode", defaultGameMode);
        allowNether = getBool(props, "allow-nether", allowNether);
        spawnProtection = getInt(props, "spawn-protection", spawnProtection);
        whiteList = getBool(props, "white-list", whiteList);
        save(props);
    }

    private static void save(Properties props) {
        props.setProperty("server-port", String.valueOf(serverPort));
        props.setProperty("online-mode", String.valueOf(onlineMode));
        props.setProperty("uuid-fix", String.valueOf(uuidFix));
        props.setProperty("network-compression-threshold", String.valueOf(compressionThreshold));
        props.setProperty("view-distance", String.valueOf(viewDistance));
        props.setProperty("simulation-distance", String.valueOf(simulationDistance));
        props.setProperty("max-players", String.valueOf(maxPlayers));
        props.setProperty("motd", motd);
        props.setProperty("level-seed", levelSeed);
        props.setProperty("difficulty", String.valueOf(difficulty));
        props.setProperty("gamemode", defaultGameMode);
        props.setProperty("allow-nether", String.valueOf(allowNether));
        props.setProperty("spawn-protection", String.valueOf(spawnProtection));
        props.setProperty("white-list", String.valueOf(whiteList));
        try (OutputStream out = Files.newOutputStream(FILE)) {
            props.store(new java.io.OutputStreamWriter(out, java.nio.charset.StandardCharsets.UTF_8),
                    "CharunCore MC Server 1.21.11");
        } catch (IOException e) {
            System.err.println("[配置] 写入 server.properties 失败: " + e.getMessage());
        }
    }

    private static int getInt(Properties props, String key, int def) {
        try {
            return Integer.parseInt(props.getProperty(key, String.valueOf(def)).trim());
        } catch (NumberFormatException e) {
            return def;
        }
    }

    private static boolean getBool(Properties props, String key, boolean def) {
        String v = props.getProperty(key);
        return v == null ? def : Boolean.parseBoolean(v.trim());
    }

    private static int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }

    public static int defaultGameModeId() {
        return switch (defaultGameMode.toLowerCase()) {
            case "creative", "1" -> 1;
            case "adventure", "2" -> 2;
            case "spectator", "3" -> 3;
            default -> 0;
        };
    }
}
