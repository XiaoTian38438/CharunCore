package com.CharunCore.server.world.light;

import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.DimensionType;
import com.CharunCore.server.world.WorldManager;
import com.CharunCore.server.world.chunk.Chunk;

public final class LightEngine {

    private static volatile int[] EMISSION = new int[0];
    private static volatile int[] OPACITY = new int[0];

    private LightEngine() {}

    public static void init() {
        java.util.Map<String, int[]> byName = new java.util.HashMap<>();
        int maxState = 0;
        try (java.io.Reader r = new java.io.InputStreamReader(
                new java.io.FileInputStream("json/1.21.11/blocks.json"),
                java.nio.charset.StandardCharsets.UTF_8)) {
            com.google.gson.JsonArray arr = com.google.gson.JsonParser.parseReader(r).getAsJsonArray();
            for (com.google.gson.JsonElement e : arr) {
                com.google.gson.JsonObject o = e.getAsJsonObject();
                String name = o.get("name").getAsString();
                int emit = o.has("emitLight") ? o.get("emitLight").getAsInt() : 0;
                int filter = o.has("filterLight") ? o.get("filterLight").getAsInt() : 0;
                byName.put(name, new int[]{emit, filter});
                if (o.has("maxStateId")) maxState = Math.max(maxState, o.get("maxStateId").getAsInt());
            }
        } catch (Exception e) {
            System.err.println("[光照] blocks.json 亮度表加载失败: " + e.getMessage());
            return;
        }
        int[] emission = new int[maxState + 1];
        int[] opacity = new int[maxState + 1];
        java.util.Arrays.fill(opacity, 15);
        for (int stateId = 0; stateId <= maxState; stateId++) {
            String name = BlockStateHelper.getName(stateId);
            int[] pair = byName.get(name);
            if (pair != null) {
                emission[stateId] = pair[0];
                opacity[stateId] = pair[1];
            }
        }
        EMISSION = emission;
        OPACITY = opacity;
        System.out.println("[光照] 亮度表已加载: " + byName.size() + " 方块, stateId 上限 " + maxState);
    }

    public static int emission(int stateId) {
        int[] t = EMISSION;
        return stateId >= 0 && stateId < t.length ? t[stateId] : 0;
    }

    public static int opacity(int stateId) {
        int[] t = OPACITY;
        return stateId >= 0 && stateId < t.length ? t[stateId] : 15;
    }

    public static synchronized void ensureChunkLight(Chunk chunk) {
        if (chunk.isLightComputed()) return;
        computeChunkLight(chunk);
    }

    private static void computeChunkLight(Chunk chunk) {
        DimensionType dim = chunk.dim;
        chunk.clearLight();
        LongFifo skyQueue = new LongFifo(4096);
        LongFifo blockQueue = new LongFifo(1024);
        int minY = dim.minY;
        int top = dim.minY + dim.height - 1;

        if (dim.hasSkylight) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    int level = 15;
                    for (int y = top; y >= minY; y--) {
                        int op = opacity(chunk.getBlock(x, y, z));
                        if (op > 0) level = Math.max(0, level - Math.max(op, 1));
                        if (level == 0) break;
                        setSkyLocal(chunk, x, y, z, level);
                    }
                }
            }
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = top; y >= minY; y--) {
                        int level = getSkyLocal(chunk, x, y, z);
                        if (level <= 1) {
                            if (level == 0) break;
                            continue;
                        }
                        if (needsSpread(chunk, x, y, z, level)) {
                            skyQueue.push(packLocal(chunk, x, y, z));
                        }
                    }
                }
            }
        }

        for (int sec = 0; sec < chunk.getSectionCount(); sec++) {
            int[] blocks = chunk.getSections()[sec].getBlocks();
            for (int i = 0; i < 4096; i++) {
                int em = emission(blocks[i]);
                if (em > 0) {
                    int x = i & 15, z = (i >> 4) & 15, y = ((i >> 8) & 15) + (sec << 4) + chunk.getMinY();
                    setBlockLocal(chunk, x, y, z, em);
                    blockQueue.push(packLocal(chunk, x, y, z));
                }
            }
        }

        java.util.Set<Chunk> touched = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<>());
        propagate(skyQueue, dim, true, touched);
        propagate(blockQueue, dim, false, touched);
        chunk.markLightComputed();
        for (Chunk other : touched) {
            if (other != chunk) NetworkHandler.broadcastLightUpdate(other);
        }
    }

    private static boolean needsSpread(Chunk chunk, int x, int y, int z, int level) {
        if (x > 0 && getSkyLocal(chunk, x - 1, y, z) < level - 1) return true;
        if (x < 15 && getSkyLocal(chunk, x + 1, y, z) < level - 1) return true;
        if (z > 0 && getSkyLocal(chunk, x, y, z - 1) < level - 1) return true;
        if (z < 15 && getSkyLocal(chunk, x, y, z + 1) < level - 1) return true;
        if (y > chunk.getMinY() && getSkyLocal(chunk, x, y - 1, z) < level - 1) return true;
        return false;
    }

    public static void onBlockChanged(DimensionType dim, int x, int y, int z, int oldState, int newState) {
        Chunk chunk = WorldManager.getChunkCached(dim, x >> 4, z >> 4);
        if (chunk == null || !chunk.isLightComputed()) return;
        int oldEm = emission(oldState), newEm = emission(newState);
        int oldOp = opacity(oldState), newOp = opacity(newState);

        LongFifo removal = new LongFifo(64);
        LongFifo add = new LongFifo(64);
        java.util.Set<Chunk> touched = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<>());
        touched.add(chunk);

        int cur = getLight(dim, x, y, z, false);
        if (cur > newEm) {
            removal.push(pack(x, y, z, cur));
            setLight(dim, x, y, z, 0, false, null);
        }
        if (newEm > 0) {
            setLight(dim, x, y, z, Math.max(cur, newEm), false, null);
            add.push(pack(x, y, z, 0));
        }
        if (newOp < oldOp) {
            add.push(pack(x, y, z, 0));
            pushNeighborLights(add, dim, x, y, z, false);
        }
        unpropagate(removal, dim, false, add, touched);
        propagate(add, dim, false, touched);

        if (dim.hasSkylight && (newOp != oldOp)) {
            LongFifo skyRemoval = new LongFifo(64);
            LongFifo skyAdd = new LongFifo(64);
            int curS = getLight(dim, x, y, z, true);
            if (newOp > oldOp) {
                if (curS > 0) {
                    skyRemoval.push(pack(x, y, z, curS));
                    setLight(dim, x, y, z, 0, true, null);
                }
                for (int yy = y - 1; yy >= dim.minY; yy--) {
                    int v = getLight(dim, x, yy, z, true);
                    if (v == 0) break;
                    skyRemoval.push(pack(x, yy, z, v));
                    setLight(dim, x, yy, z, 0, true, null);
                }
            } else {
                skyAdd.push(pack(x, y, z, 0));
                skyAdd.push(pack(x, y + 1, z, 0));
                pushNeighborLights(skyAdd, dim, x, y, z, true);
            }
            unpropagate(skyRemoval, dim, true, skyAdd, touched);
            propagate(skyAdd, dim, true, touched);
        }

        for (Chunk c : touched) NetworkHandler.broadcastLightUpdate(c);
    }

    private static void pushNeighborLights(LongFifo queue, DimensionType dim, int x, int y, int z, boolean sky) {
        queue.push(pack(x + 1, y, z, 0));
        queue.push(pack(x - 1, y, z, 0));
        queue.push(pack(x, y, z + 1, 0));
        queue.push(pack(x, y, z - 1, 0));
        queue.push(pack(x, y + 1, z, 0));
        queue.push(pack(x, y - 1, z, 0));
    }

    private static void propagate(LongFifo queue, DimensionType dim, boolean sky, java.util.Set<Chunk> touched) {
        int minY = dim.minY, maxY = dim.minY + dim.height;
        while (!queue.isEmpty()) {
            long entry = queue.pop();
            int x = unpackX(entry), y = unpackY(entry), z = unpackZ(entry);
            int level = getLight(dim, x, y, z, sky);
            if (level <= 1) continue;
            for (int dir = 0; dir < 6; dir++) {
                int nx = x + DX[dir], ny = y + DY[dir], nz = z + DZ[dir];
                if (ny < minY || ny >= maxY || nx < X_MIN || nx > X_MAX || nz < Z_MIN || nz > Z_MAX) continue;
                int state = WorldManager.getBlockStateCached(dim, nx, ny, nz);
                int op = opacity(state);
                if (op >= 15) continue;
                int newLevel;
                if (sky && DY[dir] == -1 && level == 15 && op == 0) {
                    newLevel = 15;
                } else {
                    newLevel = level - Math.max(1, op);
                }
                if (newLevel <= 0) continue;
                int existing = getLight(dim, nx, ny, nz, sky);
                if (newLevel > existing) {
                    Chunk c = setLight(dim, nx, ny, nz, newLevel, sky, touched);
                    if (c != null && touched != null) touched.add(c);
                    if (newLevel > 1 || (sky && newLevel == 15)) {
                        queue.push(pack(nx, ny, nz, 0));
                    }
                }
            }
        }
    }

    private static void unpropagate(LongFifo removal, DimensionType dim, boolean sky, LongFifo repropagate,
                                    java.util.Set<Chunk> touched) {
        while (!removal.isEmpty()) {
            long entry = removal.pop();
            int x = unpackX(entry), y = unpackY(entry), z = unpackZ(entry);
            int oldLevel = unpackLevel(entry);
            for (int dir = 0; dir < 6; dir++) {
                int nx = x + DX[dir], ny = y + DY[dir], nz = z + DZ[dir];
                if (ny < dim.minY || ny >= dim.minY + dim.height) continue;
                int nl = getLight(dim, nx, ny, nz, sky);
                if (nl == 0) continue;
                if (nl < oldLevel || (sky && DY[dir] == -1 && oldLevel == 15 && nl == 15)) {
                    setLight(dim, nx, ny, nz, 0, sky, touched);
                    removal.push(pack(nx, ny, nz, nl));
                } else {
                    repropagate.push(pack(nx, ny, nz, 0));
                }
            }
        }
    }

    private static final int[] DX = {1, -1, 0, 0, 0, 0};
    private static final int[] DY = {0, 0, 1, -1, 0, 0};
    private static final int[] DZ = {0, 0, 0, 0, 1, -1};
    private static final int X_MIN = -1_000_000, X_MAX = 1_000_000;
    private static final int Z_MIN = -1_000_000, Z_MAX = 1_000_000;

    private static long pack(int x, int y, int z, int level) {
        return ((long) (x + X_MAX) & 0x1FFFFF)
                | (((long) (z + Z_MAX) & 0x1FFFFF) << 21)
                | ((long) (y + 1024) << 42)
                | ((long) level << 53);
    }

    private static long packLocal(Chunk chunk, int x, int y, int z) {
        return pack((chunk.getX() << 4) + x, y, (chunk.getZ() << 4) + z, 0);
    }

    private static int unpackX(long e) { return (int) (e & 0x1FFFFF) - X_MAX; }
    private static int unpackZ(long e) { return (int) ((e >> 21) & 0x1FFFFF) - Z_MAX; }
    private static int unpackY(long e) { return (int) ((e >> 42) & 0x7FF) - 1024; }
    private static int unpackLevel(long e) { return (int) ((e >> 53) & 0xF); }

    public static int skyLight(DimensionType dim, int x, int y, int z) {
        return getLight(dim, x, y, z, true);
    }

    public static int blockLight(DimensionType dim, int x, int y, int z) {
        return getLight(dim, x, y, z, false);
    }

    static int getLight(DimensionType dim, int x, int y, int z, boolean sky) {
        if (y < dim.minY || y >= dim.minY + dim.height) return 0;
        Chunk c = WorldManager.getChunkCached(dim, x >> 4, z >> 4);
        if (c == null) return 0;
        byte[] a = lightArray(c, (y - c.getMinY()) >> 4, sky);
        if (a == null) return 0;
        int lx = x & 15, lz = z & 15, ly = y & 15;
        return (a[((ly << 8) | (lz << 4) | lx) >> 1] >> ((lx & 1) << 2)) & 15;
    }

    private static Chunk setLight(DimensionType dim, int x, int y, int z, int value, boolean sky,
                                  java.util.Set<Chunk> touched) {
        if (y < dim.minY || y >= dim.minY + dim.height) return null;
        Chunk c = WorldManager.getChunkCached(dim, x >> 4, z >> 4);
        if (c == null) return null;
        setLightLocal(c, x & 15, y, z & 15, value, sky);
        return c;
    }

    private static void setLightLocal(Chunk c, int lx, int y, int lz, int value, boolean sky) {
        byte[][] arrays = sky ? c.skyLightSections() : c.blockLightSections();
        int sec = (y - c.getMinY()) >> 4;
        if (sec < 0 || sec >= arrays.length) return;
        byte[] a = arrays[sec];
        if (a == null) {
            if (value == 0) return;
            a = new byte[2048];
            arrays[sec] = a;
        }
        int idx = ((y & 15) << 8) | (lz << 4) | lx;
        int shift = (lx & 1) << 2;
        a[idx >> 1] = (byte) ((a[idx >> 1] & ~(15 << shift)) | (value << shift));
    }

    private static byte[] lightArray(Chunk c, int sec, boolean sky) {
        byte[][] arrays = sky ? c.skyLightSections() : c.blockLightSections();
        if (sec < 0 || sec >= arrays.length) return null;
        return arrays[sec];
    }

    private static void setSkyLocal(Chunk chunk, int x, int y, int z, int level) {
        setLightLocal(chunk, x, y, z, level, true);
    }

    private static void setBlockLocal(Chunk chunk, int x, int y, int z, int level) {
        setLightLocal(chunk, x, y, z, level, false);
    }

    private static int getSkyLocal(Chunk chunk, int x, int y, int z) {
        byte[] a = lightArray(chunk, (y - chunk.getMinY()) >> 4, true);
        if (a == null) return 0;
        return (a[(((y & 15) << 8) | (z << 4) | x) >> 1] >> ((x & 1) << 2)) & 15;
    }

    static final class LongFifo {
        private long[] data;
        private int head, tail;

        LongFifo(int capacity) {
            data = new long[Math.max(16, capacity)];
        }

        void push(long v) {
            if (tail == data.length) {
                if (head > data.length / 2) {
                    System.arraycopy(data, head, data, 0, tail - head);
                    tail -= head;
                    head = 0;
                } else {
                    data = java.util.Arrays.copyOf(data, data.length * 2);
                }
            }
            data[tail++] = v;
        }

        long pop() {
            return data[head++];
        }

        boolean isEmpty() {
            return head == tail;
        }
    }
}
