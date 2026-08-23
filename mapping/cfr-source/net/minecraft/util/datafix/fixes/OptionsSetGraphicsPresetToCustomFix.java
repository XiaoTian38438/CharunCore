/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import net.minecraft.util.datafix.fixes.References;

public class OptionsSetGraphicsPresetToCustomFix
extends DataFix {
    public OptionsSetGraphicsPresetToCustomFix(Schema schema) {
        super(schema, true);
    }

    @Override
    public TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("graphicsPreset set to \"custom\"", this.getInputSchema().getType(References.OPTIONS), typed -> typed.update(DSL.remainderFinder(), dynamic -> dynamic.set("graphicsPreset", dynamic.createString("custom"))));
    }
}

