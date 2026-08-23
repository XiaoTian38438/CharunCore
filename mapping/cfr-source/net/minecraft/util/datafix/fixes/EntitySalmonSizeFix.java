/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import net.minecraft.util.datafix.fixes.NamedEntityFix;
import net.minecraft.util.datafix.fixes.References;

public class EntitySalmonSizeFix
extends NamedEntityFix {
    public EntitySalmonSizeFix(Schema schema) {
        super(schema, false, "EntitySalmonSizeFix", References.ENTITY, "minecraft:salmon");
    }

    @Override
    protected Typed<?> fix(Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), dynamic -> {
            String string = dynamic.get("type").asString("medium");
            if (string.equals("large")) {
                return dynamic;
            }
            return dynamic.set("type", dynamic.createString("medium"));
        });
    }
}

