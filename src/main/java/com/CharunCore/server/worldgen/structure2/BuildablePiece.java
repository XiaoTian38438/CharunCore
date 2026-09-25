package com.CharunCore.server.worldgen.structure2;

import com.CharunCore.server.worldgen.WorldGenLevel;

/** 过程化结构件的统一构建接口（要塞/末地城等非模板池结构）。 */
public interface BuildablePiece {
    BoundingBox box();

    void build(WorldGenLevel level);
}
