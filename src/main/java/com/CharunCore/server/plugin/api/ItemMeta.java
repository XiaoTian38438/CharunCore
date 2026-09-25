package com.CharunCore.server.plugin.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 物品元数据(对标 Paper ItemMeta): 显示名/lore/附魔/耐久/不可破坏/发光覆盖/自定义模型数据。
 * 可变对象, 链式调用。经 {@link ItemStack#meta()} 或 {@link ItemStack#withMeta(ItemMeta)} 挂载。
 * 落盘支持: displayName/enchants/damage 随玩家背包持久化; lore/unbreakable/glint 等会话内有效。
 */
public final class ItemMeta {

    private String displayName;
    private List<String> lore;
    private final Map<String, Integer> enchants = new LinkedHashMap<>();
    private int damage;
    private boolean unbreakable;
    private Boolean glintOverride;
    private Integer customModelData;

    public ItemMeta() {}

    // ── 显示名/lore ─────────────────────────────────────────────

    public ItemMeta setDisplayName(String name) { this.displayName = name; return this; }
    public String getDisplayName() { return displayName; }
    public boolean hasDisplayName() { return displayName != null && !displayName.isEmpty(); }

    /** 设置 lore(整组替换), 每条为一行, 支持 § 颜色码。 */
    public ItemMeta setLore(List<String> lines) {
        this.lore = lines == null ? null : new ArrayList<>(lines);
        return this;
    }

    public ItemMeta addLoreLine(String line) {
        if (lore == null) lore = new ArrayList<>();
        lore.add(line);
        return this;
    }

    public List<String> getLore() { return lore == null ? null : new ArrayList<>(lore); }
    public boolean hasLore() { return lore != null && !lore.isEmpty(); }

    // ── 附魔 ────────────────────────────────────────────────────

    /** 附魔名称与原版一致: sharpness/protection/efficiency/unbreaking/fortune 等。 */
    public ItemMeta addEnchant(String enchantName, int level) {
        enchants.put(enchantName.toLowerCase(), level);
        return this;
    }

    public ItemMeta removeEnchant(String enchantName) {
        enchants.remove(enchantName.toLowerCase());
        return this;
    }

    public int getEnchantLevel(String enchantName) {
        return enchants.getOrDefault(enchantName.toLowerCase(), 0);
    }

    public Map<String, Integer> getEnchants() { return new LinkedHashMap<>(enchants); }
    public boolean hasEnchants() { return !enchants.isEmpty(); }

    // ── 耐久/标志 ───────────────────────────────────────────────

    /** 已损失耐久 (>0 时客户端显示受损耐久条)。 */
    public ItemMeta setDamage(int damage) { this.damage = Math.max(0, damage); return this; }
    public int getDamage() { return damage; }

    public ItemMeta setUnbreakable(boolean unbreakable) { this.unbreakable = unbreakable; return this; }
    public boolean isUnbreakable() { return unbreakable; }

    /** null = 跟随物品; true/false = 强制开启/关闭附魔光效。 */
    public ItemMeta setGlintOverride(Boolean glint) { this.glintOverride = glint; return this; }
    public Boolean getGlintOverride() { return glintOverride; }

    public ItemMeta setCustomModelData(int data) { this.customModelData = data; return this; }
    public Integer getCustomModelData() { return customModelData; }

    public boolean isEmpty() {
        return !hasDisplayName() && !hasLore() && enchants.isEmpty()
                && damage <= 0 && !unbreakable && glintOverride == null && customModelData == null;
    }

    @Override
    public ItemMeta clone() {
        ItemMeta m = new ItemMeta();
        m.displayName = displayName;
        if (lore != null) m.lore = new ArrayList<>(lore);
        m.enchants.putAll(enchants);
        m.damage = damage;
        m.unbreakable = unbreakable;
        m.glintOverride = glintOverride;
        m.customModelData = customModelData;
        return m;
    }
}
