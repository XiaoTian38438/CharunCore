package com.CharunCore.server.plugin.api;

import java.util.Map;
import java.util.Set;

/**
 * 持久数据容器(对标 Paper PersistentDataContainer 简化版):
 * 键值随玩家数据落盘(world/playerdata/<uuid>.dat 的 pluginData 字段)。
 * 建议键名带插件前缀防冲突: "myplugin.home.x"。
 * 值类型: String / int / long / double / boolean / byte。
 */
public final class PersistentDataContainer {

    private final Map<String, String> backing;

    PersistentDataContainer(Map<String, String> backing) {
        this.backing = backing == null ? new java.util.HashMap<>() : backing;
    }

    public void setString(String key, String value) { backing.put(key, value); }
    public String getString(String key, String def) {
        String v = backing.get(key);
        return v == null ? def : v;
    }

    public void setInt(String key, int value) { backing.put(key, Integer.toString(value)); }
    public int getInt(String key, int def) {
        String v = backing.get(key);
        try { return v == null ? def : Integer.parseInt(v); } catch (NumberFormatException e) { return def; }
    }

    public void setLong(String key, long value) { backing.put(key, Long.toString(value)); }
    public long getLong(String key, long def) {
        String v = backing.get(key);
        try { return v == null ? def : Long.parseLong(v); } catch (NumberFormatException e) { return def; }
    }

    public void setDouble(String key, double value) { backing.put(key, Double.toString(value)); }
    public double getDouble(String key, double def) {
        String v = backing.get(key);
        try { return v == null ? def : Double.parseDouble(v); } catch (NumberFormatException e) { return def; }
    }

    public void setBoolean(String key, boolean value) { backing.put(key, Boolean.toString(value)); }
    public boolean getBoolean(String key, boolean def) {
        String v = backing.get(key);
        return v == null ? def : Boolean.parseBoolean(v);
    }

    public void setByte(String key, byte value) { backing.put(key, Byte.toString(value)); }
    public byte getByte(String key, byte def) {
        String v = backing.get(key);
        try { return v == null ? def : Byte.parseByte(v); } catch (NumberFormatException e) { return def; }
    }

    public boolean has(String key) { return backing.containsKey(key); }
    public void remove(String key) { backing.remove(key); }
    public Set<String> getKeys() { return Set.copyOf(backing.keySet()); }
    public void clear() { backing.clear(); }
    public int size() { return backing.size(); }
}
