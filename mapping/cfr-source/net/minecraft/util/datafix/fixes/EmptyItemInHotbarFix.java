/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Dynamic;
import java.util.Optional;
import net.minecraft.util.datafix.fixes.References;

public class EmptyItemInHotbarFix
extends DataFix {
    public EmptyItemInHotbarFix(Schema schema) {
        super(schema, false);
    }

    @Override
    public TypeRewriteRule makeRule() {
        OpticFinder<?> opticFinder = DSL.typeFinder(this.getInputSchema().getType(References.ITEM_STACK));
        return this.fixTypeEverywhereTyped("EmptyItemInHotbarFix", this.getInputSchema().getType(References.HOTBAR), typed -> typed.update(opticFinder, pair2 -> pair2.mapSecond(pair -> {
            boolean bl;
            Optional<String> optional = ((Either)pair.getFirst()).left().map(Pair::getSecond);
            Dynamic dynamic = (Dynamic)((Pair)pair.getSecond()).getSecond();
            boolean bl2 = optional.isEmpty() || optional.get().equals("minecraft:air");
            boolean bl3 = bl = dynamic.get("Count").asInt(0) <= 0;
            if (bl2 || bl) {
                return Pair.of(Either.right(Unit.INSTANCE), Pair.of(Either.right(Unit.INSTANCE), dynamic.emptyMap()));
            }
            return pair;
        })));
    }
}

