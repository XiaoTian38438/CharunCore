/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Reference2DoubleArrayMap
 *  it.unimi.dsi.fastutil.objects.Reference2DoubleMap$Entry
 *  it.unimi.dsi.fastutil.objects.Reference2DoubleMaps
 */
package net.minecraft.world.attribute;

import it.unimi.dsi.fastutil.objects.Reference2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2DoubleMap;
import it.unimi.dsi.fastutil.objects.Reference2DoubleMaps;
import java.util.Objects;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.LerpFunction;

public class SpatialAttributeInterpolator {
    private final Reference2DoubleArrayMap<EnvironmentAttributeMap> weightsBySource = new Reference2DoubleArrayMap();

    public void clear() {
        this.weightsBySource.clear();
    }

    public SpatialAttributeInterpolator accumulate(double d, EnvironmentAttributeMap environmentAttributeMap) {
        this.weightsBySource.mergeDouble((Object)environmentAttributeMap, d, Double::sum);
        return this;
    }

    public <Value> Value applyAttributeLayer(EnvironmentAttribute<Value> environmentAttribute, Value Value2) {
        if (this.weightsBySource.isEmpty()) {
            return Value2;
        }
        if (this.weightsBySource.size() == 1) {
            EnvironmentAttributeMap environmentAttributeMap = (EnvironmentAttributeMap)this.weightsBySource.keySet().iterator().next();
            return environmentAttributeMap.applyModifier(environmentAttribute, Value2);
        }
        LerpFunction<Value> lerpFunction = environmentAttribute.type().spatialLerp();
        Object var4_5 = null;
        double d = 0.0;
        for (Reference2DoubleMap.Entry entry : Reference2DoubleMaps.fastIterable(this.weightsBySource)) {
            EnvironmentAttributeMap environmentAttributeMap = (EnvironmentAttributeMap)entry.getKey();
            double d2 = entry.getDoubleValue();
            Value Value3 = environmentAttributeMap.applyModifier(environmentAttribute, Value2);
            d += d2;
            if (var4_5 == null) {
                var4_5 = Value3;
                continue;
            }
            float f = (float)(d2 / d);
            var4_5 = lerpFunction.apply(f, var4_5, Value3);
        }
        return Objects.requireNonNull(var4_5);
    }
}

