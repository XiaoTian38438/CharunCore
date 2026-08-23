package com.CharunCore.server.world;

import com.CharunCore.server.Main;
import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.entity.ArrowEntity;
import com.CharunCore.server.world.entity.EnderPearlEntity;
import com.CharunCore.server.world.entity.Entity;
import com.CharunCore.server.world.entity.EntityManager;
import com.CharunCore.server.world.entity.ItemEntity;
import com.CharunCore.server.world.entity.MobEntity;
import com.CharunCore.server.world.chunk.Chunk;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 最基础红石引擎：
 *   拉杆 → 邻居更新 → 红石线传播 → 红石灯/红石火把
 *
 * 只处理：lever / redstone_wire / redstone_lamp / redstone_torch
 * 不处理：comparator / observer / piston / scheduled tick
 */
public class RedstoneEngine {

    // 线程安全的 TNT 爆炸调度器，避免使用 java.util.Timer 导致的内存泄漏
    private static final ScheduledExecutorService tntScheduler = Executors.newScheduledThreadPool(2);
    // 跟踪活跃的 TNT 爆炸定时任务，用于取消
    private static final ConcurrentHashMap<String, java.util.concurrent.ScheduledFuture<?>> activeTntTasks = new ConcurrentHashMap<>();

    // ── 维度上下文：红石引擎的方块读写与广播都按"当前处理维度"进行 ──
    // 用 ThreadLocal + 入口设置/恢复，内部私有方法零签名改动；TNT 调度线程须先捕获维度
    private static final ThreadLocal<DimensionType> CTX_DIM =
            ThreadLocal.withInitial(() -> DimensionType.OVERWORLD);

    private static DimensionType ctxDim() { return CTX_DIM.get(); }

    // 重入保护：onBlockChangedInner 内部 putState 会回调 setBlock->onBlockChanged，
    // 若已在更新中则直接返回，避免活塞/红石线自激导致的无限递归(StackOverflow)。
    private static final ThreadLocal<Boolean> IN_UPDATE =
            ThreadLocal.withInitial(() -> Boolean.FALSE);

    private static int getState(int x, int y, int z) {
        return WorldManager.getBlockState(CTX_DIM.get(), x, y, z);
    }

    private static void putState(int x, int y, int z, int s) {
        WorldManager.setBlock(CTX_DIM.get(), x, y, z, s);
    }

    /** 在指定维度上下文中执行 op，执行完恢复原维度（防跨调用泄漏） */
    private static void withDim(DimensionType dim, Runnable op) {
        DimensionType prev = CTX_DIM.get();
        CTX_DIM.set(dim);
        try { op.run(); }
        finally { CTX_DIM.set(prev); }
    }

    /** 方块改变时调用此方法触发邻居更新（默认主世界） */
    public static void onBlockChanged(int x, int y, int z) {
        onBlockChanged(DimensionType.OVERWORLD, x, y, z);
    }

    /** #34 区块加载后重评估红石: 扫描区块内红石灯/红石线并触发 onBlockChanged,
     *  使持久化的 lit/power 状态按当前信号源重新计算(曾只读回旧状态, 信号源已变
     *  但灯仍亮 -> "重进游戏红石灯还是不会熄灭")。 */
    public static void onChunkLoaded(DimensionType dim, int chunkX, int chunkZ) {
        Chunk c = WorldManager.getChunkCached(dim, chunkX, chunkZ);
        if (c == null) return;
        for (int sx = 0; sx < 16; sx++) {
            for (int sz = 0; sz < 16; sz++) {
                for (int y = dim.minY; y < dim.minY + dim.height; y++) {
                    int st = c.getBlock(sx, y, sz);
                    if (st == 0) continue;
                    String n = BlockStateHelper.getName(st);
                    if (n.equals("redstone_lamp") || n.equals("redstone_wire")
                            || n.equals("lever") || n.endsWith("_button")
                            || n.equals("redstone_torch") || n.equals("redstone_wall_torch")
                            || n.equals("repeater") || n.equals("comparator")
                            || n.equals("observer") || n.equals("pressure_plate")
                            || n.startsWith("weighted_pressure_plate")) {
                        onBlockChanged(dim, chunkX * 16 + sx, y, chunkZ * 16 + sz);
                    }
                }
            }
        }
    }

    /** 维度感知入口：在指定维度上下文中执行红石更新 */
    public static void onBlockChanged(DimensionType dim, int x, int y, int z) {
        if (IN_UPDATE.get()) return;
        withDim(dim, () -> {
            IN_UPDATE.set(Boolean.TRUE);
            try { onBlockChangedInner(x, y, z); }
            finally { IN_UPDATE.set(Boolean.FALSE); }
        });
    }

    private static void onBlockChangedInner(int x, int y, int z) {
        // 【核心修复】：使用 visited set 防止 BFS 重新访问已处理的位置
        // 避免红石线改变→再次触发 onBlockChanged→无限回调
        java.util.Set<Long> visited = new java.util.HashSet<>();
        // 本批涉及的红石线集合: BFS 后做多轮收敛重算(单次通过不保证 power 收敛,
        // 拆电源后长线衰减不彻底会残留"微弱信号"且落盘后重进仍在)。
        java.util.Set<Long> wirePositions = new java.util.HashSet<>();

        // 先更新该位置自身（如果是红石线，计算 power + 形状）
        int selfState = getState(x, y, z);
        if ("redstone_wire".equals(BlockStateHelper.getName(selfState))) {
            wirePositions.add(posKey(x, y, z));
            int newState = selfState;
            int power = calcWirePower(x, y, z);
            String curP = BlockStateHelper.getProp(selfState, "power");
            if (curP != null && !curP.equals(String.valueOf(power))) {
                newState = BlockStateHelper.withProp(newState, "power", String.valueOf(power));
            }
            for (String dir : new String[]{"north","south","east","west"}) {
                String conn = calcWireConnection(x, y, z, dir);
                String cur  = BlockStateHelper.getProp(newState, dir);
                if (!conn.equals(cur)) {
                    newState = BlockStateHelper.withProp(newState, dir, conn);
                }
            }
            if (newState != selfState) {
                putState(x, y, z, newState);
                NetworkHandler.broadcastBlockChange(ctxDim(), x, y, z, newState);
                // 红石粉 power/shape 变化不发破坏粒子(原版行为); 曾发 level_event 2001 导致每次激活都冒破坏粒子。
            }
        }

        // 阳光传感器：注册为需周期刷新器件，并按当前昼夜立即写入 power 属性
        if ("daylight_detector".equals(BlockStateHelper.getName(selfState))) {
            registerTracked(ctxDim(), x, y, z, "daylight_detector");
            int sig = daylightSignal(selfState);
            String curP = BlockStateHelper.getProp(selfState, "power");
            if (curP == null || !curP.equals(String.valueOf(sig))) {
                int ns = BlockStateHelper.withProp(selfState, "power", String.valueOf(sig));
                putState(x, y, z, ns);
                NetworkHandler.broadcastBlockChange(ctxDim(), x, y, z, ns);
            }
        }

        // BFS 传播 (visited 去重防环; 深度上限 256 覆盖长红石线)
        Deque<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{x, y, z, 256});
        visited.add(posKey(x, y, z));

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int cx = cur[0], cy = cur[1], cz = cur[2], depth = cur[3];
            if (depth <= 0) continue;

            int[][] neighbors = {
                {cx+1,cy,cz},{cx-1,cy,cz},
                {cx,cy+1,cz},{cx,cy-1,cz},
                {cx,cy,cz+1},{cx,cy,cz-1}
            };

            for (int[] nb : neighbors) {
                int ns = getState(nb[0], nb[1], nb[2]);
                String name = BlockStateHelper.getName(ns);
                boolean changed = false;
                int newState = ns;

                if ("redstone_wire".equals(name)) {
                    // 收集线位置供 BFS 后收敛重算
                    wirePositions.add(posKey(nb[0], nb[1], nb[2]));
                    // 计算该wire应有的功率
                    int power = calcWirePower(nb[0], nb[1], nb[2]);
                    String cur_p = BlockStateHelper.getProp(ns, "power");
                    if (cur_p != null && !cur_p.equals(String.valueOf(power))) {
                        newState = BlockStateHelper.withProp(ns, "power", String.valueOf(power));
                        changed = true;
                    }
                    // 【新增】同步更新邻居红石线的形状
                    for (String dir : new String[]{"north","south","east","west"}) {
                        String conn = calcWireConnection(nb[0], nb[1], nb[2], dir);
                        String curConn = BlockStateHelper.getProp(newState, dir);
                        if (!conn.equals(curConn)) {
                            newState = BlockStateHelper.withProp(newState, dir, conn);
                            changed = true;
                        }
                    }
                } else if ("redstone_lamp".equals(name)) {
                    boolean shouldLit = receivesAnyPower(nb[0], nb[1], nb[2]);
                    String litStr = BlockStateHelper.getProp(ns, "lit");
                    boolean curLit = "true".equals(litStr);
                    if (curLit != shouldLit) {
                        newState = BlockStateHelper.withProp(ns, "lit", shouldLit ? "true" : "false");
                        changed = true;
                    }

                // ── 【新增】扩展红石受体 ─────────────────────────────────────
                } else if ("tnt".equals(name)) {
                    if (receivesAnyPower(nb[0], nb[1], nb[2])) {
                        primeTnt(ctxDim(), nb[0], nb[1], nb[2], 80);
                        continue;
                    }
                } else if ("powered_rail".equals(name) || "activator_rail".equals(name)) {
                    // #23 修复: 用链感知判定整条铁轨链是否被供电(而非仅直接电源),
                    // 否则 BFS 会把 propagateRailPower 激活的链上轨重新算回 false。
                    boolean powered = railChainPowered(nb[0], nb[1], nb[2], name);
                    String curP = BlockStateHelper.getProp(ns, "powered");
                    if ((powered && !"true".equals(curP)) || (!powered && "true".equals(curP))) {
                        newState = BlockStateHelper.withProp(ns, "powered", powered ? "true" : "false");
                        changed = true;
                    }
                } else if (name.endsWith("_door")) {
                    boolean powered = receivesAnyPower(nb[0], nb[1], nb[2]);
                    String curPowered = BlockStateHelper.getProp(ns, "powered");
                    if ((powered && !"true".equals(curPowered)) || (!powered && "true".equals(curPowered))) {
                        newState = BlockStateHelper.withProp(ns, "powered", powered ? "true" : "false");
                        newState = BlockStateHelper.withProp(newState, "open", powered ? "true" : "false");
                        changed = true;
                        // 同步门的另一半
                        String half = BlockStateHelper.getProp(ns, "half");
                        int otherY = "lower".equals(half) ? nb[1] + 1 : nb[1] - 1;
                        int otherState = getState(nb[0], otherY, nb[2]);
                        if (otherState != 0 && BlockStateHelper.getName(otherState).endsWith("_door")) {
                            int otherNew = BlockStateHelper.withProp(otherState, "powered", powered ? "true" : "false");
                            otherNew = BlockStateHelper.withProp(otherNew, "open", powered ? "true" : "false");
                            putState(nb[0], otherY, nb[2], otherNew);
                            NetworkHandler.broadcastBlockChange(ctxDim(), nb[0], otherY, nb[2], otherNew);
                        }
                    }
                } else if (name.endsWith("_trapdoor")) {
                    boolean powered = receivesAnyPower(nb[0], nb[1], nb[2]);
                    String curPowered = BlockStateHelper.getProp(ns, "powered");
                    if ((powered && !"true".equals(curPowered)) || (!powered && "true".equals(curPowered))) {
                        newState = BlockStateHelper.withProp(ns, "powered", powered ? "true" : "false");
                        newState = BlockStateHelper.withProp(newState, "open", powered ? "true" : "false");
                        changed = true;
                    }
                } else if ("note_block".equals(name)) {
                    boolean powered = receivesAnyPower(nb[0], nb[1], nb[2]);
                    String curP = BlockStateHelper.getProp(ns, "powered");
                    boolean wasPowered = "true".equals(curP);
                    if ((powered && !wasPowered)) {
                        // 上升沿：发出对应 instrument + note 的声音 (P8-#9)
                        playNote(nb[0], nb[1], nb[2], ns);
                    }
                    if ((powered && !wasPowered) || (!powered && wasPowered)) {
                        newState = BlockStateHelper.withProp(ns, "powered", powered ? "true" : "false");
                        changed = true;
                    }
                } else if ("dispenser".equals(name) || "dropper".equals(name)) {
                    boolean powered = receivesAnyPower(nb[0], nb[1], nb[2]);
                    String curP = BlockStateHelper.getProp(ns, "triggered");
                    if ((powered && !"true".equals(curP)) || (!powered && "true".equals(curP))) {
                        newState = BlockStateHelper.withProp(ns, "triggered", powered ? "true" : "false");
                        changed = true;
                        // 上升沿: 触发发射/投掷 (P5-#1 发射器行为)
                        if (powered && !"true".equals(curP)) {
                            dispenseFrom(ctxDim(), nb[0], nb[1], nb[2], "dropper".equals(name));
                        }
                    }
                } else if ("hopper".equals(name)) {
                    boolean powered = receivesAnyPower(nb[0], nb[1], nb[2]);
                    boolean curEnabled = !"false".equals(BlockStateHelper.getProp(ns, "enabled"));
                    boolean newEnabled = !powered;
                    if (newEnabled != curEnabled) {
                        newState = BlockStateHelper.withProp(ns, "enabled", newEnabled ? "true" : "false");
                        changed = true;
                    }
                } else if ("piston".equals(name) || "sticky_piston".equals(name)) {
                    boolean powered = receivesAnyPower(nb[0], nb[1], nb[2]);
                    String curP = BlockStateHelper.getProp(ns, "extended");
                    boolean wasExtended = "true".equals(curP);
                    if (powered && !wasExtended) {
                        // 障碍顶住时 extendPiston 返回 false → 不置 extended, 活塞保持收缩
                        if (extendPiston(nb[0], nb[1], nb[2], ns, name.equals("sticky_piston"))) {
                            newState = BlockStateHelper.withProp(ns, "extended", "true");
                            changed = true;
                        }
                    } else if (!powered && wasExtended) {
                        newState = BlockStateHelper.withProp(ns, "extended", "false");
                        changed = true;
                        // 缩回：移除 piston_head
                        retractPiston(nb[0], nb[1], nb[2], ns);
                    }
                } else if (name.startsWith("redstone_torch") || name.startsWith("redstone_wall_torch")) {
                    // 红石火把反转：支撑方块通电则熄灭，否则点亮（实现 NOT 逻辑）
                    boolean powered = torchSupportPowered(nb[0], nb[1], nb[2], ns);
                    boolean lit = "true".equals(BlockStateHelper.getProp(ns, "lit"));
                    if (powered != lit) {
                        newState = BlockStateHelper.withProp(ns, "lit", powered ? "false" : "true");
                        changed = true;
                    }
                } else if (name.equals("redstone_block")) {
                    // 红石块是恒定电源, 自身不变化, 由 calcWirePower/receivesAnyPower 提供 15 信号
                } else if (name.equals("comparator")) {
                    int cs = updateComparatorState(nb[0], nb[1], nb[2], ns);
                    if (cs != -1) {
                        // 原版: 比较器有 1 tick (2 游戏刻) 延迟
                        boolean wantLit = "true".equals(BlockStateHelper.getProp(cs, "powered"));
                        long due = Main.worldAge + 2L;
                        long ckey = posKey(nb[0], nb[1], nb[2]);
                        scheduledUpdates.add(new ScheduledUpdate(ctxDim(), ckey,
                            nb[0], nb[1], nb[2], due, "comparator", wantLit));
                    }
                } else if (name.equals("repeater")) {
                    updateRepeater(nb[0], nb[1], nb[2], ns);
                } else if (name.equals("observer")) {
                    registerTracked(ctxDim(), nb[0], nb[1], nb[2], name);
                } else if (name.equals("pressure_plate") || name.startsWith("weighted_pressure_plate") || name.endsWith("_pressure_plate")) {
                    registerTracked(ctxDim(), nb[0], nb[1], nb[2], name);
                } else if ("daylight_detector".equals(name)) {
                    registerTracked(ctxDim(), nb[0], nb[1], nb[2], name);
                }

                if (changed) {
                    putState(nb[0], nb[1], nb[2], newState);
                    NetworkHandler.broadcastBlockChange(ctxDim(), nb[0], nb[1], nb[2], newState);
                    long nbKey = posKey(nb[0], nb[1], nb[2]);
                    if (visited.add(nbKey)) {
                        queue.add(new int[]{nb[0], nb[1], nb[2], depth - 1});
                    }
                }
            }

            // #16 修复: 红石粉上/下台阶连接时, BFS 只访问 6 邻居, 台阶对角处的线(1 格上/下 + 1 格水平)
            // 永远进不了队列 -> "视觉连上了(形状属性正确)但功率不传播/不激活"。这里把与当前线
            // 呈阶梯对角连接的红石线也加入 BFS, 使其功率被重算。
            if ("redstone_wire".equals(BlockStateHelper.getName(getState(cx, cy, cz)))) {
                int[][] diag = {
                    {1,0,0,-1},{-1,0,0,-1},{0,0,1,-1},{0,0,-1,-1},
                    {1,0,0,1},{-1,0,0,1},{0,0,1,1},{0,0,-1,1}
                };
                for (int[] dg : diag) {
                    int hx = cx + dg[0], hz = cz + dg[2];
                    int vy = cy + dg[3];
                    int vSide = getState(hx, cy, hz);
                    String vsName = BlockStateHelper.getName(vSide);
                    boolean pass = dg[3] < 0
                        ? (vSide == 0 || "air".equals(vsName) || BlockStateHelper.isReplaceable(vsName))
                        : (vSide != 0 && !"air".equals(vsName));
                    if (!pass) continue;
                    int vState = getState(hx, vy, hz);
                    if (!"redstone_wire".equals(BlockStateHelper.getName(vState))) continue;
                    long dk = posKey(hx, vy, hz);
                    if (visited.add(dk)) {
                        wirePositions.add(dk);
                        queue.add(new int[]{hx, vy, hz, depth - 1});
                    }
                }
            }
        }

        // 红石线网络收敛: BFS 单次通过不保证 power 收敛(A 先算时读到邻居 B 旧值, B 再降但 A 不重算),
        // 拆电源后长线残留偏高的"微弱信号"且随区块落盘。反复重算涉及线直到稳定(原版同样迭代收敛)。
        if (!wirePositions.isEmpty()) {
            for (int round = 0; round < 32; round++) {
                boolean anyChange = false;
                for (Long wk : wirePositions) {
                    int wx = keyX(wk), wy = keyY(wk), wz = keyZ(wk);
                    int ws = getState(wx, wy, wz);
                    if (!"redstone_wire".equals(BlockStateHelper.getName(ws))) continue;
                    int pw = calcWirePower(wx, wy, wz);
                    String cp = BlockStateHelper.getProp(ws, "power");
                    if (cp != null && !cp.equals(String.valueOf(pw))) {
                        int ns2 = BlockStateHelper.withProp(ws, "power", String.valueOf(pw));
                        putState(wx, wy, wz, ns2);
                        NetworkHandler.broadcastBlockChange(ctxDim(), wx, wy, wz, ns2);
                        anyChange = true;
                    }
                }
                if (!anyChange) break;
            }
            // #34 修复: 收敛后线 power 已降, 但 BFS 早前处理灯时读到旧值(灯保持亮);
            // 重新检查所有线相邻的红石灯/火把/活塞等受体, 否则"信号消失灯不灭"。
            java.util.Set<Long> lampChecked = new java.util.HashSet<>();
            for (Long wk : wirePositions) {
                int wx = keyX(wk), wy = keyY(wk), wz = keyZ(wk);
                int[][] lampNb = {
                    {wx+1,wy,wz},{wx-1,wy,wz},{wx,wy+1,wz},{wx,wy-1,wz},
                    {wx,wy,wz+1},{wx,wy,wz-1}
                };
                for (int[] lb : lampNb) {
                    long lk = posKey(lb[0], lb[1], lb[2]);
                    if (!lampChecked.add(lk)) continue;
                    int ls = getState(lb[0], lb[1], lb[2]);
                    String ln = BlockStateHelper.getName(ls);
                    if (ln == null) continue;
                    if ("redstone_lamp".equals(ln)) {
                        boolean shouldLit = receivesAnyPower(lb[0], lb[1], lb[2]);
                        String curLit = BlockStateHelper.getProp(ls, "lit");
                        if (!String.valueOf(shouldLit).equals(curLit)) {
                            int ns3 = BlockStateHelper.withProp(ls, "lit", String.valueOf(shouldLit));
                            putState(lb[0], lb[1], lb[2], ns3);
                            NetworkHandler.broadcastBlockChange(ctxDim(), lb[0], lb[1], lb[2], ns3);
                            onBlockChanged(ctxDim(), lb[0], lb[1], lb[2]);
                        }
                    } else if (ln.startsWith("redstone_torch")) {
                        boolean powered = torchSupportPowered(lb[0], lb[1], lb[2], ls);
                        String curLit2 = BlockStateHelper.getProp(ls, "lit");
                        if (!String.valueOf(!powered).equals(curLit2)) {
                            int ns4 = BlockStateHelper.withProp(ls, "lit", String.valueOf(!powered));
                            putState(lb[0], lb[1], lb[2], ns4);
                            NetworkHandler.broadcastBlockChange(ctxDim(), lb[0], lb[1], lb[2], ns4);
                            onBlockChanged(ctxDim(), lb[0], lb[1], lb[2]);
                        }
                    } else if ("repeater".equals(ln)) {
                        // #21 修复: 收敛后输入线 power 已降, 但 BFS 期间 updateRepeater 读到旧值
                        // 没有调度熄灭 -> "信号消失时离信号最近的中继器仍一直释放信号, 打掉才恢复"。
                        // 在此按收敛后的输入重估, 需要变化时立即调度 applyRepeater。
                        String facing = BlockStateHelper.getProp(ls, "facing");
                        int[] back = facingOffset(facing);
                        int in = inputLevelAt(lb[0] - back[0], lb[1] - back[1], lb[2] - back[2]);
                        boolean wantLit = in > 0;
                        boolean lit = "true".equals(BlockStateHelper.getProp(ls, "powered"));
                        boolean locked = isRepeaterLocked(lb[0], lb[1], lb[2], ls);
                        if (!locked && wantLit != lit) {
                            int delay = 1;
                            String ds = BlockStateHelper.getProp(ls, "delay");
                            if (ds != null) { try { delay = Integer.parseInt(ds); } catch (Exception ignored) {} }
                            if (delay < 1) delay = 1;
                            if (delay > 4) delay = 4;
                            long due = Main.worldAge + delay * 2L;
                            scheduledUpdates.add(new ScheduledUpdate(ctxDim(), lk, lb[0], lb[1], lb[2], due, "repeater", wantLit));
                        }
                    } else if ("comparator".equals(ln)) {
                        int cs = updateComparatorState(lb[0], lb[1], lb[2], ls);
                        if (cs != -1) {
                            boolean wantLit = "true".equals(BlockStateHelper.getProp(cs, "powered"));
                            long due = Main.worldAge + 2L;
                            scheduledUpdates.add(new ScheduledUpdate(ctxDim(), lk, lb[0], lb[1], lb[2], due, "comparator", wantLit));
                        }
                    }
                }
            }
        }
    }

    /**
     * #23 修复: 动力铁轨/激活铁轨链上是否被供电(任一链上轨直接被供电源驱动则整链激活)。
     * 曾用 propagateRailPower 单独把链上轨置 powered=true, 但 BFS 随后用 receivesAnyPower(仅直接电源)
     * 把它们重新算回 false -> 一个红石信号只激活 1 根铁轨。现改为链感知判定: 去源后整链一起熄灭。
     */
    private static boolean railChainPowered(int x, int y, int z, String railName) {
        if (receivesAnyPower(x, y, z)) return true;
        int[][] dirs = {{1,0,0},{-1,0,0},{0,0,1},{0,0,-1}};
        for (int[] d : dirs) {
            int px = x, pz = z;
            for (int i = 0; i < 8; i++) {
                px += d[0]; pz += d[2];
                int s = getState(px, y, pz);
                if (!railName.equals(BlockStateHelper.getName(s))) break;
                if (receivesAnyPower(px, y, pz)) return true;
            }
        }
        return false;
    }

    /**
     * 计算 (x,y,z) 处红石线应有的功率。
     * 取所有邻居朝本方块输出的信号等级；红石线之间衰减 1。 (P8-#1 统一走方向性输出)
     * #24 修复: 除 6 直接邻居外, 还须检查经"水平邻居空气格下方"连接的下降线, 以及经
     * "水平邻居实体方块上方"连接的爬升线 —— 否则垂直布置的红石粉"连上了也不激活"
     * (B 在 (1,y-1) 经空气格 (1,y) 与 A 相连, 但 B 不是 A 的直接邻居)。
     */
    private static int calcWirePower(int x, int y, int z) {
        int max = 0;
        int[][] neighbors = {
                {x+1,y,z},{x-1,y,z},{x,y,z+1},{x,y,z-1}, {x,y+1,z}, {x,y-1,z}
        };
        for (int[] nb : neighbors) {
            int lvl = neighborOutputTo(nb[0], nb[1], nb[2], x, y, z);
            if (lvl <= 0) continue;
            String nn = BlockStateHelper.getName(getState(nb[0], nb[1], nb[2]));
            if ("redstone_wire".equals(nn)) lvl = lvl - 1; // 线到线衰减
            max = Math.max(max, lvl);
        }
        // 下降连接: 水平邻居是空气, 其下方有红石线(本线经该格连接到低一格的线)。
        // 爬升连接: 水平邻居是实体方块, 其上方有红石线(本线经该格连接到高一格的线)。
        int[][] diag = {
                {1,0,0,-1},{-1,0,0,-1},{0,0,1,-1},{0,0,-1,-1},
                {1,0,0,1},{-1,0,0,1},{0,0,1,1},{0,0,-1,1}
        };
        for (int[] d : diag) {
            int hx = x + d[0], hy = y, hz = z + d[2];
            int vy = y + d[3];
            int vState = getState(hx, vy, hz);
            String vn = BlockStateHelper.getName(vState);
            if (!"redstone_wire".equals(vn)) continue;
            int vSide = getState(hx, y, hz);
            String vsName = BlockStateHelper.getName(vSide);
            boolean pass = d[3] < 0
                ? (vSide == 0 || "air".equals(vsName) || BlockStateHelper.isReplaceable(vsName))
                : (vSide != 0 && !"air".equals(vsName)); // 爬升需实体方块垫脚
            if (!pass) continue;
            int lvl = neighborOutputTo(hx, vy, hz, x, y, z);
            if (lvl <= 0) continue;
            lvl = lvl - 1; // 线到线衰减
            max = Math.max(max, lvl);
        }
        return Math.max(0, max);
    }

    /**
     * 发射器/投掷器触发：从 9 槽中取一个非空物品执行对应行为。
     * 在 server 主线程执行（红石更新可能在调度线程），避免跨线程改世界。
     */
    private static void dispenseFrom(DimensionType dim, int x, int y, int z, boolean isDropper) {
        try {
            ContainerStore.Pos p = new ContainerStore.Pos(dim, x, y, z);
            ContainerStore.DispenserData d = ContainerStore.dispenser(p);
            int slot = -1;
            for (int i = 0; i < 9; i++) {
                if (d.slots[i * 2] > 0 && d.slots[i * 2 + 1] > 0) { slot = i; break; }
            }
            if (slot < 0) return;
            int itemId = d.slots[slot * 2];
            String itemName = BlockManager.itemIdToName(itemId);
            if (itemName == null) return;
            // 取出一个
            d.slots[slot * 2 + 1]--;
            if (d.slots[slot * 2 + 1] <= 0) { d.slots[slot * 2] = 0; d.slots[slot * 2 + 1] = 0; }
            d.version++;
            ContainerStore.persistDispenser(p);

            // 朝向与前方输出位置
            int st = WorldManager.getBlockState(dim, x, y, z);
            String facing = BlockStateHelper.getProp(st, "facing");
            double dx, dy, dz;
            switch (facing) {
                case "up"    -> { dx = 0;  dy = 1;  dz = 0; }
                case "down"  -> { dx = 0;  dy = -1; dz = 0; }
                case "north" -> { dx = 0;  dy = 0;  dz = -1; }
                case "south" -> { dx = 0;  dy = 0;  dz = 1; }
                case "east"  -> { dx = 1;  dy = 0;  dz = 0; }
                case "west"  -> { dx = -1; dy = 0;  dz = 0; }
                default      -> { dx = 0;  dy = 0;  dz = 0; }
            }
            double px = x + 0.5 + dx * 0.7;
            double py = y + 0.5 + dy * 0.7;
            double pz = z + 0.5 + dz * 0.7;

            if (itemName.equals("arrow")) {
                ArrowEntity arrow = new ArrowEntity(EntityManager.allocateId(), px, py, pz,
                    dx * 1.2, dy * 1.2 + 0.1, dz * 1.2, (NetworkHandler) null);
                arrow.dim = dim;
                EntityManager.addEntity(arrow);
            } else if (itemName.equals("ender_pearl")) {
                EnderPearlEntity pearl = new EnderPearlEntity(EntityManager.allocateId(), px, py, pz,
                    dx * 1.2, dy * 1.2 + 0.1, dz * 1.2, null);
                pearl.dim = dim;
                EntityManager.addEntity(pearl);
            } else if (itemName.endsWith("_spawn_egg")) {
                String mob = itemName.substring(0, itemName.length() - "_spawn_egg".length());
                MobEntity m = new MobEntity(EntityManager.allocateId(), mob, px, py, pz);
                m.dim = dim;
                EntityManager.addEntity(m);
            } else if (itemName.equals("water_bucket") || itemName.equals("lava_bucket")) {
                int fx = x + (int) Math.round(dx), fy = y + (int) Math.round(dy), fz = z + (int) Math.round(dz);
                String fluid = itemName.equals("water_bucket") ? "water" : "lava";
                int fstate = BlockStateHelper.getDefault(fluid);
                if (fstate > 0) {
                    WorldManager.setBlock(dim, fx, fy, fz, fstate);
                    NetworkHandler.broadcastBlockChange(dim, fx, fy, fz, fstate);
                }
                addItemToDispenser(d, BlockManager.getItemIdByName("bucket"), 1);
            } else {
                // 通用：朝向前方以一定初速抛出物品实体 (投掷器偏向下落)
                ItemEntity drop = new ItemEntity(EntityManager.allocateId(), px, py, pz, itemId, 1);
                drop.dim = dim;
                drop.vx = dx * 0.3;
                drop.vy = dy * 0.3 + (isDropper ? -0.05 : 0.12);
                drop.vz = dz * 0.3;
                drop.pickupDelay = 10;
                EntityManager.addEntity(drop);
            }
            // 发射音效
            for (NetworkHandler h : NetworkHandler.players.values()) {
                if (h.ctx == null || h.currentDim != dim) continue;
                h.sendSoundAt(isDropper ? "minecraft:block.dropper.dispense"
                    : "minecraft:block.dispenser.dispense", x + 0.5, y + 0.5, z + 0.5, 1.0f, 1.0f);
            }
        } catch (Exception ex) {
            System.err.println("[红石] 发射器触发异常 @ " + x + "," + y + "," + z + ": " + ex);
        }
    }

    /** 把物品放回发射器 (优先堆叠同类, 否则放进首个空槽)。 */
    private static void addItemToDispenser(ContainerStore.DispenserData d, int id, int count) {
        for (int i = 0; i < 9; i++) {
            if (d.slots[i * 2] == id && d.slots[i * 2 + 1] > 0 && d.slots[i * 2 + 1] < 64) {
                d.slots[i * 2 + 1] = Math.min(64, d.slots[i * 2 + 1] + count);
                d.version++;
                return;
            }
        }
        for (int i = 0; i < 9; i++) {
            if (d.slots[i * 2] == 0) {
                d.slots[i * 2] = id;
                d.slots[i * 2 + 1] = count;
                d.version++;
                return;
            }
        }
    }

    /**
     * 判断 (x,y,z) 是否收到任何红石信号（功率 ≥ 1）。
     * 统一按方向性输出判定：只有朝向本方块供强电的器件才会驱动它。 (P8-#1)
     */
    private static boolean receivesAnyPower(int x, int y, int z) {
        int[][] neighbors = {
                {x+1,y,z},{x-1,y,z},{x,y,z+1},{x,y,z-1}, {x,y+1,z}, {x,y-1,z}
        };
        for (int[] nb : neighbors) {
            if (neighborOutputTo(nb[0], nb[1], nb[2], x, y, z) > 0) return true;
        }
        return false;
    }

    /** 由源方块指向目标方块的方向字符串 (north/south/east/west/up/down) */
    private static String dirFromTo(int sx, int sy, int sz, int tx, int ty, int tz) {
        int dx = tx - sx, dy = ty - sy, dz = tz - sz;
        if (dx == 1) return "east";
        if (dx == -1) return "west";
        if (dz == 1) return "south";
        if (dz == -1) return "north";
        if (dy == 1) return "up";
        if (dy == -1) return "down";
        return "down";
    }

    /**
     * 计算位于 (sx,sy,sz) 的方块朝 (tx,ty,tz) 方向输出的信号等级 (0..15)。
     * 方向性器件（repeater/comparator/observer/按钮/压力板）仅在 facing 指向目标时输出。
     */
    private static int neighborOutputTo(int sx, int sy, int sz, int tx, int ty, int tz) {
        int st = getState(sx, sy, sz);
        String n = BlockStateHelper.getName(st);
        if (st == 0 || "air".equals(n)) return 0;
        String dir = dirFromTo(sx, sy, sz, tx, ty, tz);
        switch (n) {
            case "redstone_wire": {
                String p = BlockStateHelper.getProp(st, "power");
                return p != null ? Integer.parseInt(p) : 0;
            }
            case "redstone_block":
                return 15;
            case "daylight_detector":
                // 阳光传感器：朝所有方向输出其 power 属性值（弱电源，等级随昼夜变化）
                return Integer.parseInt(BlockStateHelper.getProp(st, "power") != null ? BlockStateHelper.getProp(st, "power") : "0");
            case "lever":
                return "true".equals(BlockStateHelper.getProp(st, "powered")) ? 15 : 0;
            case "redstone_torch", "redstone_wall_torch":
                if (!"true".equals(BlockStateHelper.getProp(st, "lit"))) return 0;
                // 原版: 火把弱充能所有相邻方块(站立火把不含正下方); 火把→灯/活塞直连可用
                if ("down".equals(dir) && n.equals("redstone_torch")) return 0;
                return 15;
            case "trapped_chest":
                return ContainerStore.trappedChestSignal(new ContainerStore.Pos(ctxDim(), sx, sy, sz));
            case "observer":
                // 观察者从背面输出信号 (facing 是观察面)
                return ("true".equals(BlockStateHelper.getProp(st, "powered"))
                        && oppositeDir(dir).equals(BlockStateHelper.getProp(st, "facing"))) ? 15 : 0;
            case "repeater", "comparator":
                return ("true".equals(BlockStateHelper.getProp(st, "powered"))
                        && dir.equals(BlockStateHelper.getProp(st, "facing"))) ? 15 : 0;
            default:
                if (n.endsWith("_button")
                        || n.equals("pressure_plate")
                        || n.startsWith("weighted_pressure_plate")
                        || n.endsWith("_pressure_plate")) {
                    // #31 轻质/重质压力板: 信号存于 power 属性(0-15 分级); 普通板二值 powered。
                    if (n.startsWith("weighted_pressure_plate")) {
                        String pw = BlockStateHelper.getProp(st, "power");
                        if (pw == null) return 0;
                        try { return Integer.parseInt(pw); } catch (Exception ignored) { return 0; }
                    }
                    return "true".equals(BlockStateHelper.getProp(st, "powered")) ? 15 : 0;
                }
                return 0;
        }
    }

    /**
     * 计算阳光传感器(daylight_detector)应输出的红石信号等级(0..15)。
     * 忠实复刻原版 DaylightDetectorBlock.updateSignalStrength：
     *   i = skyBrightness - skyDarken;  (本服 skyBrightness = 15 - skyDarken, 故 i = 15 - 2*skyDarken)
     *   inverted 时 i = 15 - i; 否则 i = round(i * cos(sunAngle)) 做昼夜平滑;
     *   末地/下界无天光 → 输出 0。
     */
    private static int daylightSignal(int state) {
        if (!ctxDim().hasSkylight) return 0;
        long dayTime = Main.dayTime; // 0=黎明, 6000=正午, 12000=黄昏, 18000=午夜
        // 原版 Level.getSkyDarken()：getTimeOfDay = (dayTime + 6000) % 24000 / 24000
        float f = ((dayTime + 6000L) % 24000L) / 24000.0f;
        if (f < 0) f += 1.0f;
        float f1 = 1.0f - (float)(Math.cos(f * (float) Math.PI * 2.0f) * 2.0f + 0.5f);
        f1 = Math.max(0.0f, Math.min(1.0f, f1));
        f1 = 1.0f - f1;
        int skyDarken = (int) Math.floor(f1 * 11.0f);
        int i = 15 - 2 * skyDarken;
        if (i < 0) i = 0;
        boolean inverted = "true".equals(BlockStateHelper.getProp(state, "inverted"));
        if (inverted) {
            i = 15 - i;
        } else {
            // 原版 SUN_ANGLE 平滑：把太阳角度反射到 [0, PI]，绕回余弦(≥0 用于白天)
            double ang = (f - 0.5) * 2.0 * Math.PI;
            if (ang < 0) ang = -ang;
            if (ang > Math.PI) ang = 2.0 * Math.PI - ang;
            i = (int) Math.round(i * Math.cos(ang));
        }
        return Math.max(0, Math.min(15, i));
    }

    // ── 红石线形状计算 ─────────────────────────────────────────────────────

    /** 更新 (x,y,z) 处红石线的 north/south/east/west 属性并广播 */
    private static void updateWireShape(int x, int y, int z) {
        int stateId = getState(x, y, z);
        if (!"redstone_wire".equals(BlockStateHelper.getName(stateId))) return;

        int newState = stateId;
        boolean changed = false;
        for (String dir : new String[]{"north","south","east","west"}) {
            String conn = calcWireConnection(x, y, z, dir);
            String cur  = BlockStateHelper.getProp(newState, dir);
            if (!conn.equals(cur)) {
                newState = BlockStateHelper.withProp(newState, dir, conn);
                changed = true;
            }
        }
        if (changed) {
            putState(x, y, z, newState);
            NetworkHandler.broadcastBlockChange(ctxDim(), x, y, z, newState);
        }
    }

    /** 计算单个方向的连接状态：side / up / none */
    private static String calcWireConnection(int x, int y, int z, String dir) {
        int dx = 0, dz = 0;
        switch (dir) {
            case "north" -> dz = -1;
            case "south" -> dz =  1;
            case "east"  -> dx =  1;
            case "west"  -> dx = -1;
        }

        int sideId = getState(x + dx, y, z + dz);
        if (isRedstoneConnectable(sideId)) return "side";

        // 水平邻居是实体方块，检查其上方是否有红石线（爬升）
        if (sideId != 0) {
            int upId = getState(x + dx, y + 1, z + dz);
            if (isRedstoneConnectable(upId)) return "up";
        }

        // 水平邻居是空气，检查下方是否有红石线（下降）
        if (sideId == 0) {
            int downId = getState(x + dx, y - 1, z + dz);
            if (isRedstoneConnectable(downId)) return "side";
        }

        // 转角连接 (corner connection, 轻微)：水平邻居本身不可连，但其垂直于 dir 的两个相邻同高位置
        // 有红石线，则本线经该邻居转角相连 (P8-#12 best-effort)
        int[] p1, p2;
        if (dir.equals("north") || dir.equals("south")) {
            p1 = new int[]{1, 0, 0}; p2 = new int[]{-1, 0, 0};
        } else {
            p1 = new int[]{0, 0, 1}; p2 = new int[]{0, 0, -1};
        }
        if (isRedstoneConnectable(getState(x + dx + p1[0], y, z + dz + p1[2]))
                || isRedstoneConnectable(getState(x + dx + p2[0], y, z + dz + p2[2]))) {
            return "side";
        }

        return "none";
    }

    /** 判断某方块是否可与红石线建立视觉/电气连接 */
    private static boolean isRedstoneConnectable(int stateId) {
        String n = BlockStateHelper.getName(stateId);
        if (stateId == 0 || "air".equals(n)) return false;
        return n.equals("redstone_wire")
            || n.equals("lever") || n.endsWith("_button")
            || n.startsWith("redstone_torch") || n.startsWith("redstone_wall_torch")
            || n.equals("redstone_lamp") || n.equals("tnt")
            || n.equals("powered_rail") || n.equals("activator_rail") || n.equals("detector_rail")
            || n.equals("note_block") || n.endsWith("_door") || n.endsWith("_trapdoor")
            || n.equals("dispenser") || n.equals("dropper")
            || n.equals("piston") || n.equals("sticky_piston")
            || n.equals("redstone_block")
            || n.equals("comparator") || n.equals("repeater") || n.equals("observer")
            || n.equals("pressure_plate") || n.startsWith("weighted_pressure_plate")
            || n.endsWith("_pressure_plate");
    }

    // ── 真实活塞推动 (C8bis)：移植自 vanilla PistonStructureResolver ─────────
    private static final int MAX_PUSH_DEPTH = 12;
    private static final int PISTON_MIN_Y = -64;
    private static final int PISTON_MAX_Y = 319;
    private static final int RXN_NORMAL = 0, RXN_BLOCK = 1, RXN_DESTROY = 2, RXN_PUSH_ONLY = 3;
    private static final int[][] PISTON_ALL_DIRS = {
        {1,0,0},{-1,0,0},{0,1,0},{0,-1,0},{0,0,1},{0,0,-1}
    };
    private static final java.util.Set<String> PISTON_IMMOVABLE = java.util.Set.of(
        "bedrock","obsidian","crying_obsidian","respawn_anchor","reinforced_deepslate",
        "end_portal_frame","end_portal","end_gateway");
    private static final java.util.Set<String> PISTON_BLOCKENTITY = java.util.Set.of(
        "chest","trapped_chest","ender_chest","barrel","furnace","blast_furnace","smoker",
        "hopper","dispenser","dropper","brewing_stand","shulker_box","sign","hanging_sign",
        "lectern","enchanting_table","bed","bell","campfire","beacon","spawner","mob_spawner",
        "command_block","jukebox","skull","structure_block","conduit","chiseled_bookshelf",
        "end_portal","end_gateway");

    private record PistonResult(java.util.List<int[]> push, java.util.List<int[]> destroy) {}

    private static boolean pistonIsBlockEntity(String n) {
        if (n == null) return false;
        if (n.endsWith("_chest") || n.endsWith("_barrel") || n.endsWith("_shulker_box")
                || n.endsWith("_banner") || n.endsWith("_spawner")) return true;
        return PISTON_BLOCKENTITY.contains(n);
    }

    private static boolean pistonIsSticky(int state) {
        String n = BlockStateHelper.getName(state);
        return "slime_block".equals(n) || "honey_block".equals(n);
    }

    private static boolean pistonCanStick(int a, int b) {
        String na = BlockStateHelper.getName(a);
        String nb = BlockStateHelper.getName(b);
        boolean aHoney = "honey_block".equals(na);
        boolean aSlime = "slime_block".equals(na);
        boolean bHoney = "honey_block".equals(nb);
        boolean bSlime = "slime_block".equals(nb);
        if (aHoney && bSlime) return false;
        if (aSlime && bHoney) return false;
        return pistonIsSticky(a) || pistonIsSticky(b);
    }

    private static boolean pistonSameAxis(int[] d, int[] dir) {
        return (d[0]==dir[0]&&d[1]==dir[1]&&d[2]==dir[2])
            || (d[0]==-dir[0]&&d[1]==-dir[1]&&d[2]==-dir[2]);
    }

    private static boolean pistonIsAttachable(String n) {
        if (n.endsWith("_torch") || n.endsWith("_button") || n.endsWith("_rail")
                || n.endsWith("_carpet") || n.endsWith("_pressure_plate") || n.endsWith("_sign")
                || n.endsWith("_hanging_sign") || n.endsWith("_candle")) return true;
        switch (n) {
            case "redstone_wire","lever","tripwire","tripwire_hook","snow","vine","ladder",
                 "lantern","end_rod","torch","soul_torch","cobweb","cake","flower_pot","bamboo",
                 "cactus","sugar_cane","kelp","dead_bush","tall_grass","fern","dandelion","poppy",
                 "blue_orchid","allium","azure_bluet","red_tulip","orange_tulip","white_tulip",
                 "pink_tulip","oxeye_daisy","cornflower","lily_of_the_valley","wither_rose",
                 "sunflower","lilac","rose_bush","peony","red_mushroom","brown_mushroom",
                 "sea_pickle","grass","bamboo_sapling","twisting_vines","weeping_vines",
                 "crimson_roots","warped_roots","nether_sprouts","pointed_dripstone","azalea",
                 "flowering_azalea","spore_blossom","big_dripleaf","small_dripleaf","mangrove_propagule":
                return true;
            default:
                return false;
        }
    }

    private static int pistonPushReaction(int state) {
        String n = BlockStateHelper.getName(state);
        if (n == null) return RXN_NORMAL;
        if ("honey_block".equals(n)) return RXN_PUSH_ONLY; // P8-#4: 蜂蜜块不可被拉回
        if (pistonIsAttachable(n)) return RXN_DESTROY;
        return RXN_NORMAL;
    }

    private static boolean pistonIsPushable(int state, int[] pushDir, boolean checkType, int[] pistonDir) {
        String n = BlockStateHelper.getName(state);
        if (state == 0 || "air".equals(n)) return true;
        if ("piston_head".equals(n)) return false;
        if (PISTON_IMMOVABLE.contains(n)) return false;
        if (n.equals("piston") || n.equals("sticky_piston")) {
            if ("true".equals(BlockStateHelper.getProp(state, "extended"))) return false;
        } else {
            if ("bedrock".equals(n) || "reinforced_deepslate".equals(n)) return false;
            int rxn = pistonPushReaction(state);
            if (rxn == RXN_BLOCK) return false;
            if (rxn == RXN_DESTROY) return checkType;
            if (rxn == RXN_PUSH_ONLY) return java.util.Arrays.equals(pushDir, pistonDir);
        }
        // P8-#5: 除真正不可推动者（PISTON_IMMOVABLE）外，带方块实体的方块默认可推
        return true;
    }

    private static boolean pistonContains(java.util.List<int[]> list, int[] p) {
        for (int[] e : list) if (e[0]==p[0]&&e[1]==p[1]&&e[2]==p[2]) return true;
        return false;
    }

    private static int pistonIndexOf(java.util.List<int[]> list, int[] p) {
        for (int i=0;i<list.size();i++){ int[] e=list.get(i); if (e[0]==p[0]&&e[1]==p[1]&&e[2]==p[2]) return i; }
        return -1;
    }

    private static PistonResult resolvePiston(int px,int py,int pz, int[] dir, boolean extending) {
        java.util.List<int[]> toPush = new java.util.ArrayList<>();
        java.util.List<int[]> toDestroy = new java.util.ArrayList<>();
        int[] pushDir;
        int[] startPos;
        if (extending) {
            pushDir = dir;
            startPos = new int[]{px+dir[0], py+dir[1], pz+dir[2]};
        } else {
            pushDir = new int[]{-dir[0], -dir[1], -dir[2]};
            startPos = new int[]{px+dir[0]*2, py+dir[1]*2, pz+dir[2]*2};
        }
        int startState = getState(startPos[0],startPos[1],startPos[2]);
        if (!pistonIsPushable(startState, pushDir, false, dir)) {
            if (extending && pistonPushReaction(startState) == RXN_DESTROY) {
                toDestroy.add(startPos);
            }
            return new PistonResult(java.util.List.of(), toDestroy);
        }
        if (!pistonAddBlockLine(startPos, pushDir, dir, px,py,pz, toPush, toDestroy)) {
            return new PistonResult(java.util.List.of(), java.util.List.of());
        }
        for (int i=0;i<toPush.size();i++) {
            int[] bp = toPush.get(i);
            if (pistonIsSticky(getState(bp[0],bp[1],bp[2]))
                    && !pistonAddBranching(bp, pushDir, dir, px,py,pz, toPush, toDestroy)) {
                return new PistonResult(java.util.List.of(), java.util.List.of());
            }
        }
        return new PistonResult(toPush, toDestroy);
    }

    private static boolean pistonAddBlockLine(int[] blockPos, int[] pushDir, int[] pistonDir,
            int px,int py,int pz, java.util.List<int[]> toPush, java.util.List<int[]> toDestroy) {
        int state = getState(blockPos[0],blockPos[1],blockPos[2]);
        if (state == 0 || "air".equals(BlockStateHelper.getName(state))) return true;
        if (!pistonIsPushable(state, pushDir, false, pistonDir)) return true;
        if (blockPos[0]==px && blockPos[1]==py && blockPos[2]==pz) return true;
        if (pistonContains(toPush, blockPos)) return true;
        int n2 = 1;
        if (n2 + toPush.size() > MAX_PUSH_DEPTH) return false;
        while (pistonIsSticky(state)) {
            int[] backPos = new int[]{blockPos[0]-pushDir[0]*n2, blockPos[1]-pushDir[1]*n2, blockPos[2]-pushDir[2]*n2};
            int backState = state;
            state = getState(backPos[0],backPos[1],backPos[2]);
            int[] opp = new int[]{-pushDir[0],-pushDir[1],-pushDir[2]};
            if (state==0 || "air".equals(BlockStateHelper.getName(state))
                    || !pistonCanStick(backState, state)
                    || !pistonIsPushable(state, opp, false, opp)
                    || (backPos[0]==px&&backPos[1]==py&&backPos[2]==pz)) break;
            if (++n2 + toPush.size() > MAX_PUSH_DEPTH) return false;
        }
        int n3 = 0;
        for (int n = n2 - 1; n >= 0; --n) {
            toPush.add(new int[]{blockPos[0]-pushDir[0]*n, blockPos[1]-pushDir[1]*n, blockPos[2]-pushDir[2]*n});
            ++n3;
        }
        int n = 1;
        while (true) {
            int[] frontPos = new int[]{blockPos[0]+pushDir[0]*n, blockPos[1]+pushDir[1]*n, blockPos[2]+pushDir[2]*n};
            int idx = pistonIndexOf(toPush, frontPos);
            if (idx > -1) {
                pistonReorderAtCollision(n3, idx, toPush);
                for (int i=0;i<=idx+n3;++i) {
                    int[] bp4 = toPush.get(i);
                    if (pistonIsSticky(getState(bp4[0],bp4[1],bp4[2]))
                            && !pistonAddBranching(bp4, pushDir, pistonDir, px,py,pz, toPush, toDestroy)) {
                        return false;
                    }
                }
                return true;
            }
            int frontState = getState(frontPos[0],frontPos[1],frontPos[2]);
            if (frontState==0 || "air".equals(BlockStateHelper.getName(frontState))) return true;
            if (!pistonIsPushable(frontState, pushDir, true, pushDir)
                    || (frontPos[0]==px&&frontPos[1]==py&&frontPos[2]==pz)) return false;
            if (pistonPushReaction(frontState) == RXN_DESTROY) {
                toDestroy.add(frontPos);
                return true;
            }
            if (toPush.size() >= MAX_PUSH_DEPTH) return false;
            toPush.add(frontPos);
            ++n3; ++n;
        }
    }

    private static void pistonReorderAtCollision(int n, int idx, java.util.List<int[]> toPush) {
        java.util.List<int[]> a = new java.util.ArrayList<>(toPush.subList(0, idx));
        java.util.List<int[]> b = new java.util.ArrayList<>(toPush.subList(toPush.size()-n, toPush.size()));
        java.util.List<int[]> c = new java.util.ArrayList<>(toPush.subList(idx, toPush.size()-n));
        toPush.clear();
        toPush.addAll(a); toPush.addAll(b); toPush.addAll(c);
    }

    private static boolean pistonAddBranching(int[] blockPos, int[] pushDir, int[] pistonDir,
            int px,int py,int pz, java.util.List<int[]> toPush, java.util.List<int[]> toDestroy) {
        int state = getState(blockPos[0],blockPos[1],blockPos[2]);
        for (int[] d : PISTON_ALL_DIRS) {
            if (pistonSameAxis(d, pushDir)) continue;
            int[] nb = new int[]{blockPos[0]+d[0], blockPos[1]+d[1], blockPos[2]+d[2]};
            int nbState = getState(nb[0],nb[1],nb[2]);
            if (!pistonCanStick(nbState, state)) continue;
            if (pistonAddBlockLine(nb, d, pistonDir, px,py,pz, toPush, toDestroy)) continue;
            return false;
        }
        return true;
    }

    private static void pistonApplyMove(PistonResult res, int[] moveDir, boolean extending,
            int hx,int hy,int hz, int headState) {
        for (int[] p : res.push()) {
            int ny = p[1] + moveDir[1];
            if (ny < PISTON_MIN_Y || ny > PISTON_MAX_Y) return;
        }
        for (int[] p : res.destroy()) {
            putState(p[0],p[1],p[2],0);
            NetworkHandler.broadcastBlockChange(ctxDim(), p[0],p[1],p[2],0);
        }
        int n = res.push().size();
        int[] states = new int[n];
        for (int i=0;i<n;i++){ int[] p=res.push().get(i); states[i]=getState(p[0],p[1],p[2]); }
        for (int i=0;i<n;i++){ int[] p=res.push().get(i); putState(p[0],p[1],p[2],0); NetworkHandler.broadcastBlockChange(ctxDim(), p[0],p[1],p[2],0); }
        for (int i=0;i<n;i++){ int[] p=res.push().get(i); int nx=p[0]+moveDir[0],ny=p[1]+moveDir[1],nz=p[2]+moveDir[2]; putState(nx,ny,nz,states[i]); NetworkHandler.broadcastBlockChange(ctxDim(), nx,ny,nz,states[i]); }
        if (extending) {
            putState(hx,hy,hz,headState);
            NetworkHandler.broadcastBlockChange(ctxDim(), hx,hy,hz,headState);
        }
        // P8-#5: 推动带方块实体的容器时，一并迁移其 ContainerStore 数据，避免物品遗留在旧坐标
        for (int i=0;i<n;i++){
            int[] p = res.push().get(i);
            int nx=p[0]+moveDir[0], ny=p[1]+moveDir[1], nz=p[2]+moveDir[2];
            String nm = BlockStateHelper.getName(states[i]);
            if (nm != null && (nm.equals("chest") || nm.equals("trapped_chest") || nm.equals("barrel")
                    || nm.equals("hopper") || nm.equals("furnace") || nm.equals("blast_furnace")
                    || nm.equals("smoker") || nm.equals("brewing_stand"))) {
                ContainerStore.moveContainerData(
                        new ContainerStore.Pos(ctxDim(), p[0], p[1], p[2]),
                        new ContainerStore.Pos(ctxDim(), nx, ny, nz));
            }
        }
    }

    private static int[] pistonOffset(String facing) {
        return switch (facing) {
            case "north" -> new int[]{0,0,-1};
            case "south" -> new int[]{0,0,1};
            case "east"  -> new int[]{1,0,0};
            case "west"  -> new int[]{-1,0,0};
            case "up"    -> new int[]{0,1,0};
            case "down"  -> new int[]{0,-1,0};
            default      -> new int[]{0,0,-1};
        };
    }

    /** @return 是否成功伸出（前方障碍顶住时返回 false, 调用方不应置 extended=true） */
    private static boolean extendPiston(int x, int y, int z, int pistonState, boolean sticky) {
        String facing = BlockStateHelper.getProp(pistonState, "facing");
        if (facing == null) facing = "north";
        int[] off = pistonOffset(facing);
        int hx = x+off[0], hy=y+off[1], hz=z+off[2];
        int headState = BlockStateHelper.getDefault("piston_head");
        headState = BlockStateHelper.withProp(headState, "facing", facing);
        headState = BlockStateHelper.withProp(headState, "type", sticky ? "sticky" : "normal");
        headState = BlockStateHelper.withProp(headState, "short", "false");
        PistonResult res = resolvePiston(x,y,z, off, true);
        // 原版: 前方有不可推动/不可破坏的障碍时活塞不伸头
        int frontState = getState(hx,hy,hz);
        String frontName = BlockStateHelper.getName(frontState);
        if (frontState != 0 && !"air".equals(frontName)
                && !"piston_head".equals(frontName)
                && res.push().isEmpty() && res.destroy().isEmpty()
                && pistonPushReaction(frontState) != RXN_DESTROY) {
            return false; // 障碍顶住, 不伸头
        }
        // #17 修复: 先播伸头动画(block_event action=0) + 音效, 2 游戏刻后再真正移动方块并放置活塞头。
        // 曾先 pistonApplyMove 立即放头 -> 客户端看到"直接变"没有伸头动画。
        NetworkHandler.broadcastBlockEvent(ctxDim(), x, y, z, (byte) 0, (byte) 0);
        NetworkHandler.broadcastSoundAt(ctxDim(), x, y, z, "minecraft:block.piston.extend", 0.5f, 0.7f);
        long key = posKey(x, y, z);
        scheduledUpdates.add(new ScheduledUpdate(ctxDim(), key, x, y, z, Main.worldAge + 2, "piston", true));
        return true;
    }

    private static void retractPiston(int x, int y, int z, int pistonState) {
        String facing = BlockStateHelper.getProp(pistonState, "facing");
        if (facing == null) facing = "north";
        boolean sticky = "sticky_piston".equals(BlockStateHelper.getName(pistonState));
        int[] off = pistonOffset(facing);
        // #17 修复: 先播缩回动画(block_event action=1) + 音效, 2 游戏刻后再移除活塞头并粘性拉回。
        // 曾立即移除头 -> 客户端"直接变"没有缩回动画。
        NetworkHandler.broadcastBlockEvent(ctxDim(), x, y, z, (byte) 1, (byte) 1);
        NetworkHandler.broadcastSoundAt(ctxDim(), x, y, z, "minecraft:block.piston.contract", 0.5f, 0.7f);
        long key = posKey(x, y, z);
        scheduledUpdates.add(new ScheduledUpdate(ctxDim(), key, x, y, z, Main.worldAge + 2, "piston", false));
    }

    /** 活塞动画结束后真正移动方块/放置/移除活塞头(原版 PistonBaseBlock.tick)。 */
    private static void applyPiston(int x, int y, int z, boolean extending) {
        int ps = getState(x, y, z);
        String n = BlockStateHelper.getName(ps);
        if (!"piston".equals(n) && !"sticky_piston".equals(n)) return;
        String facing = BlockStateHelper.getProp(ps, "facing");
        if (facing == null) facing = "north";
        boolean sticky = "sticky_piston".equals(n);
        int[] off = pistonOffset(facing);
        int hx = x+off[0], hy=y+off[1], hz=z+off[2];
        if (extending) {
            if (!"true".equals(BlockStateHelper.getProp(ps, "extended"))) return; // 信号在动画期间消失则取消
            int headState = BlockStateHelper.getDefault("piston_head");
            headState = BlockStateHelper.withProp(headState, "facing", facing);
            headState = BlockStateHelper.withProp(headState, "type", sticky ? "sticky" : "normal");
            headState = BlockStateHelper.withProp(headState, "short", "false");
            PistonResult res = resolvePiston(x,y,z, off, true);
            pistonApplyMove(res, off, true, hx,hy,hz, headState);
        } else {
            int front = getState(hx,hy,hz);
            if ("piston_head".equals(BlockStateHelper.getName(front))) {
                putState(hx,hy,hz,0);
                NetworkHandler.broadcastBlockChange(ctxDim(), hx,hy,hz,0);
            }
            // 原版: 只有粘性活塞缩回才拉回前方方块; 普通活塞缩回不拉方块
            if (sticky) {
                int beyond = getState(x+off[0]*2, y+off[1]*2, z+off[2]*2);
                if (beyond != 0 && !"air".equals(BlockStateHelper.getName(beyond))
                        && pistonIsPushable(beyond, new int[]{-off[0],-off[1],-off[2]}, false, off)) {
                    PistonResult res = resolvePiston(x,y,z, off, false);
                    pistonApplyMove(res, new int[]{-off[0],-off[1],-off[2]}, false, hx,hy,hz, 0);
                }
            }
        }
    }

    // ── 高级红石器件 (repeater / comparator / observer / pressure_plate) ──────

    private static final java.util.Queue<ScheduledUpdate> scheduledUpdates =
            new java.util.concurrent.ConcurrentLinkedQueue<>();
    // 观察者/压力板/上次状态按维度分层，避免主世界与下界同坐标互踩
    private static final java.util.Map<DimensionType, java.util.Set<Long>> trackedObservers =
            new java.util.EnumMap<>(DimensionType.class);
    private static final java.util.Map<DimensionType, java.util.Set<Long>> trackedPlates =
            new java.util.EnumMap<>(DimensionType.class);
    private static final java.util.Map<DimensionType, java.util.Map<Long, Integer>> observerLastSeen =
            new java.util.EnumMap<>(DimensionType.class);
    private static final java.util.Map<DimensionType, java.util.Set<Long>> trackedDaylight =
            new java.util.EnumMap<>(DimensionType.class);
    static {
        for (DimensionType d : DimensionType.values()) {
            trackedObservers.put(d, java.util.concurrent.ConcurrentHashMap.newKeySet());
            trackedPlates.put(d, java.util.concurrent.ConcurrentHashMap.newKeySet());
            observerLastSeen.put(d, new java.util.concurrent.ConcurrentHashMap<>());
            trackedDaylight.put(d, java.util.concurrent.ConcurrentHashMap.newKeySet());
        }
    }

    private static final class ScheduledUpdate {
        final DimensionType dim; final long key; final int x, y, z; final long due; final String kind; final boolean value;
        ScheduledUpdate(DimensionType dim, long key, int x, int y, int z, long due, String kind, boolean value) {
            this.dim = dim; this.key = key; this.x = x; this.y = y; this.z = z;
            this.due = due; this.kind = kind; this.value = value;
        }
    }

    /** 某方块是否为红石电源（提供信号） */
    private static boolean providesPower(int state) {
        String n = BlockStateHelper.getName(state);
        if (n == null) return false;
        if (n.equals("redstone_block")) return true;
        if (n.equals("lever") && "true".equals(BlockStateHelper.getProp(state, "powered"))) return true;
        if ((n.equals("redstone_torch") || n.equals("redstone_wall_torch")) && "true".equals(BlockStateHelper.getProp(state, "lit"))) return true;
        if (n.equals("redstone_wire")) {
            String p = BlockStateHelper.getProp(state, "power");
            return p != null && Integer.parseInt(p) > 0;
        }
        if (n.endsWith("_button") && "true".equals(BlockStateHelper.getProp(state, "powered"))) return true;
        if ((n.equals("pressure_plate") || n.startsWith("weighted_pressure_plate") || n.endsWith("_pressure_plate"))
                && "true".equals(BlockStateHelper.getProp(state, "powered"))) return true;
        if (n.equals("repeater") && "true".equals(BlockStateHelper.getProp(state, "powered"))) return true;
        if (n.equals("comparator") && "true".equals(BlockStateHelper.getProp(state, "powered"))) return true;
        if (n.equals("daylight_detector")) return true;
        return false;
    }

    /** 红石火把是否被其支撑方块通电（用于反转） */
    private static boolean torchSupportPowered(int x, int y, int z, int state) {
        String name = BlockStateHelper.getName(state);
        int sx = x, sy = y, sz = z;
        if (name.equals("redstone_torch")) {
            sy = y - 1;
        } else {
            String f = BlockStateHelper.getProp(state, "facing");
            if ("north".equals(f)) sz = z - 1;
            else if ("south".equals(f)) sz = z + 1;
            else if ("west".equals(f)) sx = x - 1;
            else if ("east".equals(f)) sx = x + 1;
        }
        return providesPower(getState(sx, sy, sz));
    }

    private static int[] facingOffset(String facing) {
        return switch (facing) {
            case "north" -> new int[]{0, 0, -1};
            case "south" -> new int[]{0, 0, 1};
            case "east"  -> new int[]{1, 0, 0};
            case "west"  -> new int[]{-1, 0, 0};
            case "up"    -> new int[]{0, 1, 0};
            case "down"  -> new int[]{0, -1, 0};
            default      -> new int[]{0, 0, 0};
        };
    }

    private static String oppositeDir(String dir) {
        return switch (dir) {
            case "north" -> "south";
            case "south" -> "north";
            case "east" -> "west";
            case "west" -> "east";
            case "up" -> "down";
            case "down" -> "up";
            default -> dir;
        };
    }

    /** 从某方块接收的输入信号等级 (0..15) */
    private static int inputLevelAt(int x, int y, int z) {
        int s = getState(x, y, z);
        if (s == 0) return 0;
        if ("redstone_wire".equals(BlockStateHelper.getName(s))) {
            String p = BlockStateHelper.getProp(s, "power");
            return p != null ? Integer.parseInt(p) : 0;
        }
        return providesPower(s) ? 15 : 0;
    }

    /** 计算比较器应输出状态; 无变化返回 -1 */
    private static int updateComparatorState(int x, int y, int z, int state) {
        String facing = BlockStateHelper.getProp(state, "facing");
        int[] back = facingOffset(facing);
        int[] left = facingOffset(switch (facing) {
            case "north" -> "west"; case "west" -> "south"; case "south" -> "east"; default -> "north";
        });
        int[] right = facingOffset(switch (facing) {
            case "north" -> "east"; case "east" -> "south"; case "south" -> "west"; default -> "north";
        });
        // 原版语义: facing 指向输出面, 输入在背面 (-facing)
        int backIn = inputLevelAt(x - back[0], y - back[1], z - back[2]);
        // P8-#6: 背后方块有模拟输出信号时（容器），取填充等级
        int backAnalog = containerAnalogSignal(x - back[0], y - back[1], z - back[2]);
        backIn = Math.max(backIn, backAnalog);
        int sideMax = Math.max(
                inputLevelAt(x + left[0], y + left[1], z + left[2]),
                inputLevelAt(x + right[0], y + right[1], z + right[2]));
        boolean subtract = "subtract".equals(BlockStateHelper.getProp(state, "mode"));
        int out = subtract ? Math.max(0, backIn - sideMax) : (backIn >= sideMax ? backIn : 0);
        boolean lit = out > 0;
        boolean curLit = "true".equals(BlockStateHelper.getProp(state, "powered"));
        if (lit == curLit) return -1;
        return BlockStateHelper.withProp(state, "powered", lit ? "true" : "false");
    }

    /** 中继器: 输入变化后延迟 DELAY*2 游戏刻 再改变输出；实现侧向锁定 (P8-#7) */
    private static void updateRepeater(int x, int y, int z, int state) {
        String facing = BlockStateHelper.getProp(state, "facing");
        int[] back = facingOffset(facing);
        // 原版语义: facing 指向输出面, 输入在背面 (-facing)
        int in = inputLevelAt(x - back[0], y - back[1], z - back[2]);
        boolean wantLit = in > 0;
        boolean lit = "true".equals(BlockStateHelper.getProp(state, "powered"));

        // 锁定：侧边（左/右）有信号注入本中继器时，保持当前输出不变
        boolean locked = isRepeaterLocked(x, y, z, state);
        String curLocked = BlockStateHelper.getProp(state, "locked");
        if (locked != "true".equals(curLocked)) {
            int nl = BlockStateHelper.withProp(state, "locked", locked ? "true" : "false");
            putState(x, y, z, nl);
            NetworkHandler.broadcastBlockChange(ctxDim(), x, y, z, nl);
        }
        if (locked) return;

        if (wantLit != lit) {
            int delay = 1;
            String ds = BlockStateHelper.getProp(state, "delay");
            if (ds != null) { try { delay = Integer.parseInt(ds); } catch (Exception ignored) {} }
            if (delay < 1) delay = 1;
            if (delay > 4) delay = 4;
            long due = Main.worldAge + delay * 2L; // 1 红石刻 = 2 游戏刻
            long key = posKey(x, y, z);
            scheduledUpdates.add(new ScheduledUpdate(ctxDim(), key, x, y, z, due, "repeater", wantLit));
        }
    }

    /** 判断中继器是否被侧向信号锁定 (P8-#7) */
    private static boolean isRepeaterLocked(int x, int y, int z, int state) {
        String facing = BlockStateHelper.getProp(state, "facing");
        int[] left = facingOffset(switch (facing) {
            case "north" -> "west"; case "west" -> "south"; case "south" -> "east"; default -> "north";
        });
        int[] right = facingOffset(switch (facing) {
            case "north" -> "east"; case "east" -> "south"; case "south" -> "west"; default -> "north";
        });
        int lx = x + left[0], ly = y + left[1], lz = z + left[2];
        int rx = x + right[0], ry = y + right[1], rz = z + right[2];
        return neighborOutputTo(lx, ly, lz, x, y, z) > 0
                || neighborOutputTo(rx, ry, rz, x, y, z) > 0;
    }

    private static void applyComparator(int x, int y, int z, boolean lit) {
        int state = getState(x, y, z);
        if (!"comparator".equals(BlockStateHelper.getName(state))) return;
        boolean cur = "true".equals(BlockStateHelper.getProp(state, "powered"));
        if (cur == lit) return;
        int ns = BlockStateHelper.withProp(state, "powered", lit ? "true" : "false");
        putState(x, y, z, ns);
        NetworkHandler.broadcastBlockChange(ctxDim(), x, y, z, ns);
        onBlockChanged(ctxDim(), x, y, z);
    }

    private static void applyRepeater(int x, int y, int z, boolean lit) {
        int state = getState(x, y, z);
        if (!"repeater".equals(BlockStateHelper.getName(state))) return;
        boolean cur = "true".equals(BlockStateHelper.getProp(state, "powered"));
        if (cur == lit) return;
        int ns = BlockStateHelper.withProp(state, "powered", lit ? "true" : "false");
        putState(x, y, z, ns);
        NetworkHandler.broadcastBlockChange(ctxDim(), x, y, z, ns);
        onBlockChanged(ctxDim(), x, y, z);
    }

    private static void applyObserverOff(int x, int y, int z) {
        int state = getState(x, y, z);
        if (!"observer".equals(BlockStateHelper.getName(state))) return;
        if (!"true".equals(BlockStateHelper.getProp(state, "powered"))) return;
        int ns = BlockStateHelper.withProp(state, "powered", "false");
        putState(x, y, z, ns);
        NetworkHandler.broadcastBlockChange(ctxDim(), x, y, z, ns);
        onBlockChanged(ctxDim(), x, y, z);
    }

    /** 触发相邻红石重新求值（陷阱箱信号变化时调用，默认主世界） */
    public static void updateNeighborsAt(int x, int y, int z) {
        updateNeighborsAt(DimensionType.OVERWORLD, x, y, z);
    }

    /** 维度感知入口：在指定维度上下文中触发相邻红石重新求值 */
    public static void updateNeighborsAt(DimensionType dim, int x, int y, int z) {
        withDim(dim, () -> {
            int[][] n = {{x+1,y,z},{x-1,y,z},{x,y+1,z},{x,y-1,z},{x,y,z+1},{x,y,z-1}};
            for (int[] nb : n) onBlockChanged(ctxDim(), nb[0], nb[1], nb[2]);
        });
    }

    // ── 比较器容器模拟信号 (P8-#6) ───────────────────────────────────────────
    private static int containerAnalogSignal(int x, int y, int z) {
        int st = getState(x, y, z);
        String n = BlockStateHelper.getName(st);
        if (n == null) return 0;
        ContainerStore.Pos p = new ContainerStore.Pos(ctxDim(), x, y, z);
        if (n.equals("chest") || n.equals("trapped_chest") || n.equals("barrel")) {
            return fillSignal(ContainerStore.chest(p).slots, 27);
        }
        if (n.equals("hopper")) {
            return fillSignal(ContainerStore.hopper(p).slots, 5);
        }
        if (n.equals("furnace") || n.equals("blast_furnace") || n.equals("smoker")) {
            return fillSignal(ContainerStore.furnace(p, n).slots, 3);
        }
        if (n.equals("brewing_stand")) {
            return fillSignal(ContainerStore.brewing(p).slots, 5);
        }
        return 0;
    }

    private static int fillSignal(int[] slots, int count) {
        int filled = 0;
        for (int i = 0; i < count; i++) {
            if (slots[2 * i] > 0 && slots[2 * i + 1] > 0) filled++;
        }
        if (filled == 0) return 0;
        return Math.max(1, Math.min(15, (int) Math.round(15.0 * filled / count)));
    }

    // ── 音符盒 (P8-#9) ───────────────────────────────────────────────────────
    private static void playNote(int x, int y, int z, int state) {
        String noteStr = BlockStateHelper.getProp(state, "note");
        int note = 0;
        if (noteStr != null) { try { note = Integer.parseInt(noteStr); } catch (Exception ignored) {} }
        if (note < 0) note = 0;
        if (note > 24) note = 24;
        // #32 修复: 客户端对 note_block 的 block_event(0x07) 播粒子, 但声音依赖服务端 sound 包
        // (实测 block_event 只有动效无声音)。双通道: block_event(粒子) + level_sound_event(0x73 声音)。
        NetworkHandler.broadcastBlockEvent(ctxDim(), x, y, z, (byte) 0, (byte) 0);
        String instrument = noteInstrument(x, y, z);
        String sound = "minecraft:block.note_block." + instrument;
        float pitch = (float) Math.pow(2.0, (note - 12) / 12.0);
        for (NetworkHandler p : NetworkHandler.players.values()) {
            if (p.ctx != null && p.ctx.channel().isActive()
                    && p.currentDim == ctxDim()) {
                p.sendSoundAt(sound, x + 0.5, y + 0.5, z + 0.5, 3.0f, pitch);
            }
        }
    }

    /** 玩家右键音符盒调音时发声（维度由调用方上下文决定）。 */
    public static void playNoteManual(DimensionType dim,
                                      int x, int y, int z, int state) {
        withDim(dim, () -> playNote(x, y, z, state));
    }

    /** 依据下方（或上方可穿透乐器块）方块决定音符盒乐器，近似原版映射 */
    private static String noteInstrument(int x, int y, int z) {
        int below = getState(x, y - 1, z);
        String bn = BlockStateHelper.getName(below);
        if (bn != null) {
            if (bn.contains("glass") || bn.equals("sea_lantern") || bn.equals("beacon")) return "hat";
            if (bn.equals("sand") || bn.equals("gravel") || bn.contains("concrete_powder")) return "snare";
            if (bn.contains("wood") || bn.contains("planks") || bn.contains("log") || bn.equals("note_block")) return "bass";
            if (bn.contains("stone") || bn.equals("cobblestone") || bn.equals("obsidian") || bn.equals("bedrock")) return "basedrum";
            if (bn.equals("clay") || bn.contains("terracotta")) return "flute";
            if (bn.equals("gold_block")) return "bell";
            if (bn.equals("ice") || bn.equals("packed_ice") || bn.equals("blue_ice")) return "chime";
            if (bn.equals("bone_block")) return "xylophone";
            if (bn.equals("iron_block")) return "iron_xylophone";
            if (bn.equals("soul_sand")) return "cow_bell";
            if (bn.equals("pumpkin") || bn.equals("melon")) return "didgeridoo";
            if (bn.equals("emerald_block")) return "bit";
            if (bn.equals("hay_block")) return "banjo";
            if (bn.equals("glowstone")) return "pling";
        }
        return "harp";
    }

    // ── TNT 爆炸 (P8-#15 best-effort)：按抗性近似、生成掉落物、对实体造成伤害 ──
    /** 激活 TNT: 移除方块并生成"点燃的 TNT"实体(带 FUSE 元数据, 客户端渲染闪烁),
     *  引信走游戏刻(EntityManager.tick), 到时经 ExplosionEngine 爆炸(连锁/水下规则生效)。 */
    public static void primeTnt(DimensionType dim, int x, int y, int z, int fuseTicks) {
        int state = WorldManager.getBlockState(dim, x, y, z);
        if (!"tnt".equals(BlockStateHelper.getName(state))) return;
        putDimState(dim, x, y, z, 0);
        NetworkHandler.broadcastBlockChange(dim, x, y, z, 0);
        Entity tnt =
                new Entity(
                        EntityManager.allocateId(), 0, x + 0.5, y, z + 0.5);
        tnt.typeName = "tnt";
        tnt.dim = dim;
        tnt.tntFuse = Math.max(10, fuseTicks);
        tnt.vx = (java.util.concurrent.ThreadLocalRandom.current().nextDouble() - 0.5) * 0.1;
        tnt.vy = 0.2;
        tnt.vz = (java.util.concurrent.ThreadLocalRandom.current().nextDouble() - 0.5) * 0.1;
        EntityManager.addEntity(tnt);
        for (NetworkHandler p : NetworkHandler.players.values()) {
            if (p.ctx == null || p.currentDim != dim) continue;
            p.sendSoundAt("minecraft:entity.tnt.primed", x + 0.5, y + 0.5, z + 0.5, 1.0f, 1.0f);
        }
    }

    private static void putDimState(DimensionType dim, int x, int y, int z, int state) {
        WorldManager.setBlock(dim, x, y, z, state);
    }

    private static void detonate(int tx, int ty, int tz) {
        putState(tx, ty, tz, 0);
        NetworkHandler.broadcastBlockChange(ctxDim(), tx, ty, tz, 0);
        int r = 3; // 近似原版 TNT 半径
        for (int dx = -r; dx <= r; dx++) {
            for (int dy = -r; dy <= r; dy++) {
                for (int dz = -r; dz <= r; dz++) {
                    double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    if (dist > r + 0.5) continue;
                    int ex = tx + dx, ey = ty + dy, ez = tz + dz;
                    int s = getState(ex, ey, ez);
                    String en = BlockStateHelper.getName(s);
                    if (s == 0 || "air".equals(en)) continue;
                    // 不可破坏集合（近似原版爆炸抗性）
                    if ("bedrock".equals(en) || "obsidian".equals(en) || "end_portal".equals(en)
                            || "end_gateway".equals(en) || "reinforced_deepslate".equals(en)
                            || "crying_obsidian".equals(en)) continue;
                    // 外圈按距离概率破坏，近似抗性衰减
                    if (dist > 1.5 && Math.random() > (r + 1 - dist) / (r + 1)) continue;
                    putState(ex, ey, ez, 0);
                    NetworkHandler.broadcastBlockChange(ctxDim(), ex, ey, ez, 0);
                }
            }
        }
        // 对范围内实体造成伤害与击退 (best-effort)
        for (Entity e : EntityManager.getAllEntities()) {
            double d = Math.sqrt((e.x - tx) * (e.x - tx) + (e.y - ty) * (e.y - ty) + (e.z - tz) * (e.z - tz));
            if (d > r + 1) continue;
            float dmg = (float) (r + 1 - d);
            if (dmg > 0) {
                e.health -= dmg;
                EntityManager.broadcastEntityHurt(e);
                if (e.health <= 0) EntityManager.removeEntity(e.id);
            }
        }
    }

    /** 注册需 tick 扫描的器件 (观察者/压力板) */
    /** 坐标 key: 原版 BlockPos 编码 (x 26位高, z 26位中, y 12位低)。修复旧编码 x<<32|z32|y16 的位冲突(z 高16位被 y 覆盖)。 */
    private static long posKey(int x, int y, int z) {
        return ((long) x & 0x3FFFFFFL) << 38 | ((long) z & 0x3FFFFFFL) << 12 | ((long) y & 0xFFFL);
    }

    private static int keyX(long key) { return (int) (key >> 38); }
    private static int keyZ(long key) { return (int) ((key << 26) >> 38); }
    private static int keyY(long key) { return (int) ((key << 52) >> 52); }

    /** 注册观察者/压力板（默认主世界） */
    public static void registerTracked(int x, int y, int z, String name) {
        registerTracked(DimensionType.OVERWORLD, x, y, z, name);
    }

    /** 维度感知入口：注册观察者/压力板到指定维度集合 */
    public static void registerTracked(DimensionType dim, int x, int y, int z, String name) {
        long key = posKey(x, y, z);
        if (name.equals("observer")) trackedObservers.get(dim).add(key);
        else if (name.equals("pressure_plate") || name.startsWith("weighted_pressure_plate") || name.endsWith("_pressure_plate")) trackedPlates.get(dim).add(key);
        else if (name.equals("daylight_detector")) trackedDaylight.get(dim).add(key);
    }

    /** 主循环每 tick 调用: 处理定时更新 + 观察者/压力板扫描（各维度独立处理） */
    public static void tick() {
        try {
            long now = Main.worldAge; // P8-#10: 基于游戏 tick 计数，而非墙钟
            for (DimensionType dim : DimensionType.values()) {
                withDim(dim, () -> {
                    java.util.List<ScheduledUpdate> due = new java.util.ArrayList<>();
                    for (ScheduledUpdate u : scheduledUpdates) {
                        if (u.dim == dim && u.due <= now) due.add(u);
                    }
                    for (ScheduledUpdate u : due) {
                        if (scheduledUpdates.remove(u)) {
                            if ("repeater".equals(u.kind)) applyRepeater(u.x, u.y, u.z, u.value);
                            else if ("observer_off".equals(u.kind)) applyObserverOff(u.x, u.y, u.z);
                            else if ("comparator".equals(u.kind)) applyComparator(u.x, u.y, u.z, u.value);
                            else if ("piston".equals(u.kind)) applyPiston(u.x, u.y, u.z, u.value);
                        }
                    }

                    for (long key : trackedObservers.get(dim)) {
                        int x = keyX(key);
                        int z = keyZ(key);
                        int y = keyY(key);
                        int state = getState(x, y, z);
                        if (!"observer".equals(BlockStateHelper.getName(state))) { trackedObservers.get(dim).remove(key); observerLastSeen.get(dim).remove(key); continue; }
                        String facing = BlockStateHelper.getProp(state, "facing");
                        int[] off = facingOffset(facing);
                        int frontState = getState(x + off[0], y + off[1], z + off[2]);
                        Integer last = observerLastSeen.get(dim).get(key);
                        if (last == null) { observerLastSeen.get(dim).put(key, frontState); continue; }
                        if (last != frontState) {
                            observerLastSeen.get(dim).put(key, frontState);
                            int ns = BlockStateHelper.withProp(state, "powered", "true");
                            putState(x, y, z, ns);
                            NetworkHandler.broadcastBlockChange(ctxDim(), x, y, z, ns);
                            onBlockChanged(ctxDim(), x, y, z);
                            // P8-#8: 精确 1 红石刻(2 游戏刻) 脉冲，基于游戏 tick
                            scheduledUpdates.add(new ScheduledUpdate(dim, key, x, y, z, now + 2, "observer_off", false));
                        }
                    }

                    for (long key : trackedPlates.get(dim)) {
                        int x = keyX(key);
                        int z = keyZ(key);
                        int y = keyY(key);
                        int state = getState(x, y, z);
                        String n = BlockStateHelper.getName(state);
                        if (n == null || (!n.equals("pressure_plate") && !n.startsWith("weighted_pressure_plate") && !n.endsWith("_pressure_plate"))) {
                            trackedPlates.get(dim).remove(key); continue;
                        }
                        // #31: 轻质/重质压力板按实体数量输出分级信号(原版 WeightedPressurePlateBlock):
                        // 轻质 maxWeight=15, 重质=150; signal = ceil(min(count,max)/max * 15)。
                        // 曾只做二值 -> 轻质板放几个物品/掉落物信号始终 15, 且普通压力板受影响。
                        int signal;
                        if (n.startsWith("light_weighted_pressure_plate")) {
                            signal = weightedPlateSignal(dim, x, y, z, 15);
                        } else if (n.startsWith("heavy_weighted_pressure_plate")) {
                            signal = weightedPlateSignal(dim, x, y, z, 150);
                        } else {
                            signal = entityOnPlate(dim, x, y, z) ? 15 : 0;
                        }
                        String curPower = BlockStateHelper.getProp(state, "power");
                        int cur = 0;
                        if (curPower != null) { try { cur = Integer.parseInt(curPower); } catch (Exception ignored) {} }
                        if (signal != cur) {
                            int ns = BlockStateHelper.withProp(state, "power", String.valueOf(signal));
                            putState(x, y, z, ns);
                            NetworkHandler.broadcastBlockChange(ctxDim(), x, y, z, ns);
                            onBlockChanged(ctxDim(), x, y, z);
                        }
                    }

                    // 阳光传感器：每 20 游戏刻(=1 秒)按昼夜重算输出，变化时刷新并触发相邻红石重求值
                    if (now % 20 == 0) {
                        for (long key : trackedDaylight.get(dim)) {
                            int x = keyX(key), y = keyY(key), z = keyZ(key);
                            int state = getState(x, y, z);
                            String n = BlockStateHelper.getName(state);
                            if (n == null || !n.equals("daylight_detector")) { trackedDaylight.get(dim).remove(key); continue; }
                            int sig = daylightSignal(state);
                            String curP = BlockStateHelper.getProp(state, "power");
                            int cur = 0;
                            if (curP != null) { try { cur = Integer.parseInt(curP); } catch (Exception ignored) {} }
                            if (sig != cur) {
                                int ns = BlockStateHelper.withProp(state, "power", String.valueOf(sig));
                                putState(x, y, z, ns);
                                NetworkHandler.broadcastBlockChange(ctxDim(), x, y, z, ns);
                                onBlockChangedInner(x, y, z);
                            }
                        }
                    }
                });
            }
        } catch (Exception e) {
            // 红石 tick 异常不得影响主循环
        }
    }

    private static boolean entityOnPlate(DimensionType dim, int x, int y, int z) {
        // 玩家 (P8-#13)
        for (NetworkHandler p : NetworkHandler.players.values()) {
            if (p.ctx == null || p.isDead || p.currentDim != dim) continue;
            int px = (int) Math.floor(p.x);
            int py = (int) Math.floor(p.y);
            int pz = (int) Math.floor(p.z);
            if (px == x && pz == z && (py == y || py == y - 1)) return true;
        }
        // 生物 / 物品实体 (P8-#13 best-effort)
        for (Entity e : EntityManager.getAllEntities()) {
            if (e.dim != dim) continue;
            int ex = (int) Math.floor(e.x);
            int ey = (int) Math.floor(e.y);
            int ez = (int) Math.floor(e.z);
            if (ex == x && ez == z && (ey == y || ey == y - 1 || ey == y - 2)) return true;
        }
        return false;
    }

    /** #31 轻质/重质压力板: 统计板上实体数量(玩家+生物+掉落物), 按 maxWeight 映射为 0-15 信号。 */
    private static int weightedPlateSignal(DimensionType dim, int x, int y, int z, int maxWeight) {
        int count = 0;
        for (NetworkHandler p : NetworkHandler.players.values()) {
            if (p.ctx == null || p.isDead || p.currentDim != dim) continue;
            int px = (int) Math.floor(p.x);
            int py = (int) Math.floor(p.y);
            int pz = (int) Math.floor(p.z);
            // #31 修复: 仅板面正上方(实体脚部在板 y 或 y-1), 排除 y-2(板下实体误触发)。
            if (px == x && pz == z && (py == y || py == y - 1)) count++;
        }
        for (Entity e : EntityManager.getAllEntities()) {
            if (e.dim != dim) continue;
            // 排除玩家(上面已计), 仅计生物/掉落物
            if (e instanceof com.CharunCore.server.world.entity.MobEntity) {
                int ex = (int) Math.floor(e.x);
                int ey = (int) Math.floor(e.y);
                int ez = (int) Math.floor(e.z);
                if (ex == x && ez == z && (ey == y || ey == y - 1)) count++;
            } else if (e instanceof com.CharunCore.server.world.entity.ItemEntity) {
                // 掉落物: 允许 y 略低于板面(物品沉入板内半格), 但排除 y-2(板下方误触发)
                int ex = (int) Math.floor(e.x);
                int ey = (int) Math.floor(e.y);
                int ez = (int) Math.floor(e.z);
                if (ex == x && ez == z && (ey == y || ey == y - 1)) count++;
            }
        }
        if (count <= 0) return 0;
        int capped = Math.min(count, maxWeight);
        // 原版: ceil(capped/maxWeight * 15)
        return (capped * 15 + maxWeight - 1) / maxWeight;
    }
}
