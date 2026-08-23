/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.fixes.EntityRenameFix;
import net.minecraft.util.datafix.fixes.References;

public class EntityMinecartIdentifiersFix
extends EntityRenameFix {
    public EntityMinecartIdentifiersFix(Schema schema) {
        super("EntityMinecartIdentifiersFix", schema, true);
    }

    @Override
    protected Pair<String, Typed<?>> fix(String string, Typed<?> typed) {
        if (!string.equals("Minecart")) {
            return Pair.of(string, typed);
        }
        int n = typed.getOrCreate(DSL.remainderFinder()).get("Type").asInt(0);
        String string2 = switch (n) {
            default -> "MinecartRideable";
            case 1 -> "MinecartChest";
            case 2 -> "MinecartFurnace";
        };
        Type<?> type = this.getOutputSchema().findChoiceType(References.ENTITY).types().get(string2);
        return Pair.of(string2, Util.writeAndReadTypedOrThrow(typed, type, dynamic -> dynamic.remove("Type")));
    }
}

