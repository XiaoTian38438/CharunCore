/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import java.util.List;
import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
import net.minecraft.util.datafix.fixes.NamedEntityFix;
import net.minecraft.util.datafix.fixes.References;

public class SignTextStrictJsonFix
extends NamedEntityFix {
    private static final List<String> LINE_FIELDS = List.of("Text1", "Text2", "Text3", "Text4");

    public SignTextStrictJsonFix(Schema schema) {
        super(schema, false, "SignTextStrictJsonFix", References.BLOCK_ENTITY, "Sign");
    }

    @Override
    protected Typed<?> fix(Typed<?> typed2) {
        for (String string : LINE_FIELDS) {
            OpticFinder<?> opticFinder = typed2.getType().findField(string);
            OpticFinder<?> opticFinder2 = DSL.typeFinder(this.getInputSchema().getType(References.TEXT_COMPONENT));
            typed2 = typed2.updateTyped(opticFinder, typed -> typed.update(opticFinder2, pair -> pair.mapSecond(LegacyComponentDataFixUtils::rewriteFromLenient)));
        }
        return typed2;
    }
}

