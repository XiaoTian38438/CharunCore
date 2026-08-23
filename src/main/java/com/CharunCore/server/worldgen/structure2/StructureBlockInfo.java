package com.CharunCore.server.worldgen.structure2;

import org.cloudburstmc.nbt.NbtMap;

public record StructureBlockInfo(int x, int y, int z, int blockStateId, NbtMap nbt) {
    public StructureBlockInfo offset(int ox, int oy, int oz) {
        return new StructureBlockInfo(x + ox, y + oy, z + oz, blockStateId, nbt);
    }
}
