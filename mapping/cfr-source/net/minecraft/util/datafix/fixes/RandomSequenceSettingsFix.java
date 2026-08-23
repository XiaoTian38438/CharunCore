/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.datafix.fixes.References;

public class RandomSequenceSettingsFix
extends DataFix {
    public RandomSequenceSettingsFix(Schema schema) {
        super(schema, false);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("RandomSequenceSettingsFix", this.getInputSchema().getType(References.SAVED_DATA_RANDOM_SEQUENCES), typed -> typed.update(DSL.remainderFinder(), dynamic2 -> dynamic2.update("data", dynamic -> dynamic.emptyMap().set("sequences", (Dynamic<?>)dynamic))));
    }
}

