package com.CharunCore.server.worldgen.biome;

import com.CharunCore.server.worldgen.density.NoiseRouter;

public class MultiNoiseBiomeSource {

    private final Climate.ParameterList<Integer> parameters;

    public MultiNoiseBiomeSource() {
        this.parameters = new Climate.ParameterList<>(OverworldBiomeBuilder.buildParameters());
    }

    public Climate.Sampler createSampler(NoiseRouter router) {
        return new Climate.Sampler(
            router.temperature(),
            router.vegetation(),
            router.continents(),
            router.erosion(),
            router.depth(),
            router.ridges()
        );
    }

    public int getBiome(Climate.TargetPoint target) {
        return parameters.findValue(target);
    }

    public int getBiome(int quartX, int quartY, int quartZ, Climate.Sampler sampler) {
        return getBiome(sampler.sample(quartX, quartY, quartZ));
    }
}
