package com.CharunCore.server.worldgen.structure2;

import com.CharunCore.server.world.gen.RandomSource;

public enum Mirror {
    NONE, LEFT_RIGHT, FRONT_BACK;

    public static Mirror random(RandomSource rng) {
        return values()[rng.nextInt(3)];
    }
}
