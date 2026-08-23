package com.CharunCore.server.worldgen.noisechunk.aquifer;

import com.CharunCore.server.worldgen.density.DensityFunction;

public interface Aquifer {

    static Aquifer createDisabled(FluidPicker fluidPicker) {
        return new Aquifer() {
            @Override
            public java.lang.Integer computeSubstance(DensityFunction.FunctionContext functionContext, double d) {
                if (d > 0.0) {
                    return null;
                }
                return fluidPicker.computeFluid(functionContext.blockX(), functionContext.blockY(), functionContext.blockZ()).at(functionContext.blockY());
            }

            @Override
            public boolean shouldScheduleFluidUpdate() {
                return false;
            }
        };
    }

    java.lang.Integer computeSubstance(DensityFunction.FunctionContext var1, double var2);

    boolean shouldScheduleFluidUpdate();

    record FluidStatus(int fluidLevel, int fluidType) {
        public java.lang.Integer at(int n) {
            return n < this.fluidLevel ? this.fluidType : 0;
        }
    }

    @FunctionalInterface
    interface FluidPicker {
        FluidStatus computeFluid(int x, int y, int z);
    }
}
