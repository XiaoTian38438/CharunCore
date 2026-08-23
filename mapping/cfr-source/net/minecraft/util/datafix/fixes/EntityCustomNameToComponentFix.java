/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import java.util.Optional;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.ExtraDataFixUtils;
import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

public class EntityCustomNameToComponentFix
extends DataFix {
    public EntityCustomNameToComponentFix(Schema schema) {
        super(schema, true);
    }

    @Override
    public TypeRewriteRule makeRule() {
        Type<?> type = this.getInputSchema().getType(References.ENTITY);
        Type<?> type2 = this.getOutputSchema().getType(References.ENTITY);
        OpticFinder<String> opticFinder = DSL.fieldFinder("id", NamespacedSchema.namespacedString());
        OpticFinder<?> opticFinder2 = type.findField("CustomName");
        Type<?> type3 = type2.findFieldType("CustomName");
        return this.fixTypeEverywhereTyped("EntityCustomNameToComponentFix", type, type2, (Typed<?> typed) -> EntityCustomNameToComponentFix.fixEntity(typed, type2, opticFinder, opticFinder2, type3));
    }

    private static <T> Typed<?> fixEntity(Typed<?> typed, Type<?> type, OpticFinder<String> opticFinder, OpticFinder<String> opticFinder2, Type<T> type2) {
        Optional<String> optional = typed.getOptional(opticFinder2);
        if (optional.isEmpty()) {
            return ExtraDataFixUtils.cast(type, typed);
        }
        if (optional.get().isEmpty()) {
            return Util.writeAndReadTypedOrThrow(typed, type, dynamic -> dynamic.remove("CustomName"));
        }
        String string = typed.getOptional(opticFinder).orElse("");
        Dynamic<?> dynamic2 = EntityCustomNameToComponentFix.fixCustomName(typed.getOps(), optional.get(), string);
        return typed.set(opticFinder2, Util.readTypedOrThrow(type2, dynamic2));
    }

    private static <T> Dynamic<T> fixCustomName(DynamicOps<T> dynamicOps, String string, String string2) {
        if ("minecraft:commandblock_minecart".equals(string2)) {
            return new Dynamic<T>(dynamicOps, dynamicOps.createString(string));
        }
        return LegacyComponentDataFixUtils.createPlainTextComponent(dynamicOps, string);
    }
}

