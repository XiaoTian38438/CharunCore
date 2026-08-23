package com.CharunCore.server.advancement;

import java.util.List;
import java.util.Set;

/**
 * 成就定义 (对应原版 Advancement + 单个 Criterion)。
 *
 * <p>best-effort 架构：每个 AdvancementDef 表示一个成就节点，含展示信息 (title/description/icon/frame)、
 * 父节点 (用于客户端树形结构)、触发条件与达成所需 criteria。本系统为每个成就使用单一 criterion
 * (name = {@link #criterionName})，requirements 用 AND 逻辑 (单组包含该 criterion)，与原版多数叶子成就一致。</p>
 *
 * <p>匹配逻辑 (matchBlocks / matchDimension) 在 AdvancementManager 中由对应 trigger 消费。</p>
 */
public final class AdvancementDef {

    /** 成就 key，如 "minecraft:story/mine_stone"。 */
    public final String id;
    /** 父成就 key；根节点为 null。客户端需要父节点存在才能构建树。 */
    public final String parentId;
    /** 展示标题 (纯文本)。 */
    public final String title;
    /** 展示描述 (纯文本)。 */
    public final String description;
    /** 图标物品协议 registry id (由 BlockManager.getItemIdByName 解析)。 */
    public final int iconItemId;
    /** 框类型。 */
    public final AdvancementFrame frame;
    /** 是否弹出 toast (对应 flags/show_toast)。 */
    public final boolean showToast;
    /** 是否在进度界面隐藏 (对应 flags/hidden)。 */
    public final boolean hidden;
    /** 背景纹理 (可空)；非空时写入 flags/background_texture 与背景字符串。 */
    public final String background;
    /** 树形布局坐标。 */
    public final float x, y;

    /** 触发类型。 */
    public final AdvancementTrigger trigger;
    /** criterion 名称 (用于 progress 映射)。 */
    public final String criterionName;
    /** 该成就包含的所有 criterion 名称 (叶子成就为 [criterionName])。 */
    public final List<String> criteria;
    /** requirements 逻辑：每组内 AND，组间 OR。本系统单组。 */
    public final List<List<String>> requirements;

    /** 是否为根节点 (无 criterion，仅用于树结构/展示)。 */
    public final boolean isRoot;

    /** BLOCK_BREAK / BLOCK_PLACE 匹配：放置/破坏的方块名需在此集合内 (null 表示不限制)。 */
    public final Set<String> matchBlocks;
    /** BLOCK_BREAK / BLOCK_PLACE 匹配：方块名需以这些后缀之一结尾 (用于 _sapling 等)。 */
    public final Set<String> matchSuffixes;
    /** ENTER_DIMENSION 匹配：目标维度的 key (如 "minecraft:the_nether")。 */
    public final String matchDimension;
    /** ENTITY_KILL 匹配：击杀的生物名需在此集合内 (null 表示任意生物)。 */
    public final Set<String> matchMobs;

    private AdvancementDef(Builder b) {
        this.id = b.id;
        this.parentId = b.parentId;
        this.title = b.title;
        this.description = b.description;
        this.iconItemId = b.iconItemId;
        this.frame = b.frame;
        this.showToast = b.showToast;
        this.hidden = b.hidden;
        this.background = b.background;
        this.x = b.x;
        this.y = b.y;
        this.trigger = b.trigger;
        this.criterionName = b.criterionName;
        this.criteria = b.criteria;
        this.requirements = b.requirements;
        this.isRoot = b.isRoot;
        this.matchBlocks = b.matchBlocks;
        this.matchSuffixes = b.matchSuffixes;
        this.matchDimension = b.matchDimension;
        this.matchMobs = b.matchMobs;
    }

    static final class Builder {
        String id;
        String parentId;
        String title;
        String description;
        int iconItemId;
        AdvancementFrame frame = AdvancementFrame.TASK;
        boolean showToast = true;
        boolean hidden = false;
        String background;
        float x, y;
        AdvancementTrigger trigger;
        String criterionName = "c0";
        List<String> criteria = List.of("c0");
        List<List<String>> requirements = List.of(List.of("c0"));
        boolean isRoot = false;
        Set<String> matchBlocks;
        Set<String> matchSuffixes;
        String matchDimension;
        Set<String> matchMobs;

        Builder id(String v) { this.id = v; return this; }
        Builder parent(String v) { this.parentId = v; return this; }
        Builder title(String v) { this.title = v; return this; }
        Builder description(String v) { this.description = v; return this; }
        Builder icon(int v) { this.iconItemId = v; return this; }
        Builder frame(AdvancementFrame v) { this.frame = v; return this; }
        Builder showToast(boolean v) { this.showToast = v; return this; }
        Builder hidden(boolean v) { this.hidden = v; return this; }
        Builder background(String v) { this.background = v; return this; }
        Builder pos(float x, float y) { this.x = x; this.y = y; return this; }
        Builder trigger(AdvancementTrigger v) { this.trigger = v; return this; }
        Builder criterion(String name) { this.criterionName = name; this.criteria = List.of(name); this.requirements = List.of(List.of(name)); return this; }
        Builder root() { this.isRoot = true; this.criteria = List.of(); this.requirements = List.of(); return this; }
        Builder matchBlocks(Set<String> v) { this.matchBlocks = v; return this; }
        Builder matchSuffixes(Set<String> v) { this.matchSuffixes = v; return this; }
        Builder matchDimension(String v) { this.matchDimension = v; return this; }
        Builder matchMobs(Set<String> v) { this.matchMobs = v; return this; }

        AdvancementDef build() { return new AdvancementDef(this); }
    }
}
