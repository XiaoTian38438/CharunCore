/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;

public class EntityFallDistanceFloatToDoubleFix
extends DataFix {
    private final DSL.TypeReference type;

    public EntityFallDistanceFloatToDoubleFix(Schema schema, DSL.TypeReference typeReference) {
        super(schema, false);
        this.type = typeReference;
    }

    @Override
    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("EntityFallDistanceFloatToDoubleFixFor" + this.type.typeName(), this.getOutputSchema().getType(this.type), EntityFallDistanceFloatToDoubleFix::fixEntity);
    }

    private static Typed<?> fixEntity(Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), dynamic2 -> dynamic2.renameAndFixField("FallDistance", "fall_distance", dynamic -> dynamic.createDouble(dynamic.asFloat(0.0f))));
    }
}

