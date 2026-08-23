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
import java.util.function.Function;
import java.util.function.UnaryOperator;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.ExtraDataFixUtils;
import net.minecraft.util.datafix.fixes.References;

public class ProjectileStoredWeaponFix
extends DataFix {
    public ProjectileStoredWeaponFix(Schema schema) {
        super(schema, true);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        Type<?> type = this.getInputSchema().getType(References.ENTITY);
        Type<?> type2 = this.getOutputSchema().getType(References.ENTITY);
        return this.fixTypeEverywhereTyped("Fix Arrow stored weapon", type, type2, ExtraDataFixUtils.chainAllFilters(this.fixChoice("minecraft:arrow"), this.fixChoice("minecraft:spectral_arrow")));
    }

    private Function<Typed<?>, Typed<?>> fixChoice(String string) {
        Type<?> type = this.getInputSchema().getChoiceType(References.ENTITY, string);
        Type<?> type2 = this.getOutputSchema().getChoiceType(References.ENTITY, string);
        return ProjectileStoredWeaponFix.fixChoiceCap(string, type, type2);
    }

    private static <T> Function<Typed<?>, Typed<?>> fixChoiceCap(String string, Type<?> type, Type<T> type2) {
        OpticFinder<?> opticFinder = DSL.namedChoice(string, type);
        return typed2 -> typed2.updateTyped(opticFinder, type2, typed -> Util.writeAndReadTypedOrThrow(typed, type2, UnaryOperator.identity()));
    }
}

