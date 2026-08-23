/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import java.util.Objects;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.fixes.EntityRenameFix;
import net.minecraft.util.datafix.fixes.References;

public class EntityHorseSplitFix
extends EntityRenameFix {
    public EntityHorseSplitFix(Schema schema, boolean bl) {
        super("EntityHorseSplitFix", schema, bl);
    }

    @Override
    protected Pair<String, Typed<?>> fix(String string, Typed<?> typed) {
        if (Objects.equals("EntityHorse", string)) {
            Dynamic<?> dynamic2 = typed.get(DSL.remainderFinder());
            int n = dynamic2.get("Type").asInt(0);
            String string2 = switch (n) {
                default -> "Horse";
                case 1 -> "Donkey";
                case 2 -> "Mule";
                case 3 -> "ZombieHorse";
                case 4 -> "SkeletonHorse";
            };
            Type<?> type = this.getOutputSchema().findChoiceType(References.ENTITY).types().get(string2);
            return Pair.of(string2, Util.writeAndReadTypedOrThrow(typed, type, dynamic -> dynamic.remove("Type")));
        }
        return Pair.of(string, typed);
    }
}

