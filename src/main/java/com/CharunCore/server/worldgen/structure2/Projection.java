package com.CharunCore.server.worldgen.structure2;

public enum Projection {
    RIGID,
    TERRAIN_MATCHING;

    public static Projection fromString(String s) {
        return "terrain_matching".equals(s) ? TERRAIN_MATCHING : RIGID;
    }
}
