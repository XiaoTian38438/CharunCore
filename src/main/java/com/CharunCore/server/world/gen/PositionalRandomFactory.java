package com.CharunCore.server.world.gen;

public interface PositionalRandomFactory {
    RandomSource at(int x, int y, int z);
    RandomSource fromHashOf(String name);
    RandomSource fromSeed(long seed);
}
