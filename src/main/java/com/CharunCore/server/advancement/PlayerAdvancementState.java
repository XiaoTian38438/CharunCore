package com.CharunCore.server.advancement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 单个玩家的成就进度 (对应原版 AdvancementProgress + PlayerAdvancements 的玩家部分)。
 *
 * <p>结构：achievementId -> 已达成 criterion 名称集合。一个成就被判定为 done 当且仅当其
 * {@link AdvancementDef#requirements} 全部满足 (本系统单组 AND，即唯一 criterion 达成)。</p>
 */
public final class PlayerAdvancementState {

    /** advancementId -> 已完成的 criterion 名称集合。 */
    private final Map<String, Set<String>> done = new HashMap<>();

    /** 该玩家已整体完成的成就 id 集合 (用于持久化与去重)。 */
    private final Set<String> completedAdvancements = new HashSet<>();

    /**
     * 判定某 criterion 是否已完成。
     */
    public boolean isCriterionDone(String advId, String criterion) {
        Set<String> s = done.get(advId);
        return s != null && s.contains(criterion);
    }

    /**
     * 判定某成就是否已整体完成。
     */
    public boolean isDone(String advId) {
        return completedAdvancements.contains(advId);
    }

    /**
     * 授予某成就的指定 criterion。若该 criterion 已由完成则无操作并返回 false。
     * 若该授予使整个成就首次达成，则返回 true (调用方据此下发 toast)。
     */
    public boolean grant(String advId, String criterion) {
        boolean alreadyComplete = completedAdvancements.contains(advId);
        Set<String> s = done.computeIfAbsent(advId, k -> new HashSet<>());
        s.add(criterion);
        if (!alreadyComplete) {
            completedAdvancements.add(advId);
            return true; // 首次整体达成
        }
        return false;
    }

    /**
     * 直接标记某成就整体完成 (用于从磁盘加载已授予成就)。
     */
    public void markDone(String advId) {
        completedAdvancements.add(advId);
        // 不填充具体 criterion —— 仅用于“已完成”状态展示，不参与新授予判定。
    }

    /**
     * 已整体完成的成就 id 集合 (只读快照)。
     */
    public Set<String> getCompletedAdvancements() {
        return Set.copyOf(completedAdvancements);
    }
}
