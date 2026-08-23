/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import java.util.Map;
import java.util.stream.Stream;
import net.minecraft.util.datafix.fixes.References;

public class CustomModelDataExpandFix
extends DataFix {
    public CustomModelDataExpandFix(Schema schema) {
        super(schema, false);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        Type<?> type = this.getInputSchema().getType(References.DATA_COMPONENTS);
        return this.fixTypeEverywhereTyped("Custom Model Data expansion", type, typed -> typed.update(DSL.remainderFinder(), dynamic2 -> dynamic2.update("minecraft:custom_model_data", dynamic -> {
            float f = dynamic.asNumber(Float.valueOf(0.0f)).floatValue();
            return dynamic.createMap(Map.of(dynamic.createString("floats"), dynamic.createList(Stream.of(dynamic.createFloat(f)))));
        })));
    }
}

