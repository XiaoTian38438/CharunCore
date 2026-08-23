package com.CharunCore.server.worldgen.structure;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import org.cloudburstmc.nbt.NBTInputStream;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 死代码（审计 P1-6）。旧版模板加载器：裸 NBTInputStream 读 gzip 文件（无 GZIPInputStream 解包），
 * 实际解析会失败；活跃管线改用 structure2.StructureTemplate。本类仅被同包内已废弃的
 * generateStructures() 引用，而 generateStructures() 无外部调用方。删除会破坏编译，故保留并标注。
 */
@Deprecated(since = "audit P1-6", forRemoval = true)
public class StructureTemplateLoader {

    private static final Map<String, int[]> templateCache = new HashMap<>();
    private static final String TEMPLATE_DIR = "mapping/remapped_server_1.21.11.jar.src/data/minecraft/structure/";

    public static void placeTemplate(Chunk chunk, String templatePath, int cx, int cz, int baseY, int rotation) {
        NbtMap template = loadTemplate(templatePath);
        if (template == null) return;

        int[] size = template.getIntArray("size");
        if (size == null || size.length < 3) return;

        int width = size[0];
        int height = size[1];
        int depth = size[2];

        List<NbtMap> blocks = template.getList("blocks", NbtType.COMPOUND);
        List<NbtMap> palette = template.getList("palette", NbtType.COMPOUND);

        if (blocks == null || palette == null) return;

        Map<Integer, String> paletteMap = new HashMap<>();
        for (int i = 0; i < palette.size(); i++) {
            NbtMap entry = (NbtMap) palette.get(i);
            String name = entry.getString("Name");
            NbtMap props = entry.getCompound("Properties");
            if (props != null && !props.isEmpty()) {
                StringBuilder sb = new StringBuilder(name);
                for (String key : props.keySet()) {
                    sb.append("[").append(key).append("=").append(props.getString(key)).append("]");
                }
                paletteMap.put(i, sb.toString());
            } else {
                paletteMap.put(i, name);
            }
        }

        for (int i = 0; i < blocks.size(); i++) {
            NbtMap block = (NbtMap) blocks.get(i);
            int[] pos = block.getIntArray("pos");
            int stateIdx = block.getInt("state");

            if (pos == null || pos.length < 3) continue;

            int bx = pos[0];
            int by = pos[1];
            int bz = pos[2];

            int[] rotated = rotatePosition(bx, by, bz, width, depth, rotation);
            int worldX = cx + rotated[0];
            int worldY = baseY + rotated[1];
            int worldZ = cz + rotated[2];

            int lx = worldX & 15;
            int lz = worldZ & 15;
            if (lx < 0 || lx > 15 || lz < 0 || lz > 15) continue;
            if (worldY < -64 || worldY > 319) continue;

            String blockState = paletteMap.get(stateIdx);
            if (blockState == null || blockState.equals("minecraft:air")) continue;

            int blockId = BlockStateHelper.getDefault(blockState.replace("minecraft:", ""));
            if (blockId == 0 && !blockState.equals("minecraft:air")) {
                String simpleName = blockState.split("\\[")[0].replace("minecraft:", "");
                blockId = BlockStateHelper.getDefault(simpleName);
            }

            if (blockId != 0) {
                chunk.setBlock(lx, worldY, lz, blockId);
            }
        }
    }

    private static int[] rotatePosition(int x, int y, int z, int width, int depth, int rotation) {
        return switch (rotation & 3) {
            case 0 -> new int[]{x, y, z};
            case 1 -> new int[]{z, y, width - 1 - x};
            case 2 -> new int[]{width - 1 - x, y, depth - 1 - z};
            case 3 -> new int[]{depth - 1 - z, y, x};
            default -> new int[]{x, y, z};
        };
    }

    private static NbtMap loadTemplate(String path) {
        String fullPath = TEMPLATE_DIR + path;
        File file = new File(fullPath);
        if (!file.exists()) return null;

        try (FileInputStream fis = new FileInputStream(file);
             NBTInputStream nbtIn = new NBTInputStream(new DataInputStream(fis))) {
            return (NbtMap) nbtIn.readTag();
        } catch (Exception e) {
            return null;
        }
    }
}
