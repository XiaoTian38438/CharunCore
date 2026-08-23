/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.datafix.fixes.AbstractUUIDFix;
import net.minecraft.util.datafix.fixes.References;
import org.slf4j.Logger;

public class LevelUUIDFix
extends AbstractUUIDFix {
    private static final Logger LOGGER = LogUtils.getLogger();

    public LevelUUIDFix(Schema schema) {
        super(schema, References.LEVEL);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        Type<?> type = this.getInputSchema().getType(this.typeReference);
        OpticFinder<?> opticFinder = type.findField("CustomBossEvents");
        OpticFinder<Pair<Either<?, Unit>, Dynamic<?>>> opticFinder2 = DSL.typeFinder(DSL.and(DSL.optional(DSL.field("Name", this.getInputSchema().getTypeRaw(References.TEXT_COMPONENT))), DSL.remainderType()));
        return this.fixTypeEverywhereTyped("LevelUUIDFix", type, typed -> typed.update(DSL.remainderFinder(), dynamic -> {
            dynamic = this.updateDragonFight((Dynamic<?>)dynamic);
            dynamic = this.updateWanderingTrader((Dynamic<?>)dynamic);
            return dynamic;
        }).updateTyped(opticFinder, typed2 -> typed2.updateTyped(opticFinder2, typed -> typed.update(DSL.remainderFinder(), this::updateCustomBossEvent))));
    }

    private Dynamic<?> updateWanderingTrader(Dynamic<?> dynamic) {
        return LevelUUIDFix.replaceUUIDString(dynamic, "WanderingTraderId", "WanderingTraderId").orElse(dynamic);
    }

    private Dynamic<?> updateDragonFight(Dynamic<?> dynamic2) {
        return dynamic2.update("DimensionData", dynamic -> dynamic.updateMapValues(pair -> pair.mapSecond(dynamic2 -> dynamic2.update("DragonFight", dynamic -> LevelUUIDFix.replaceUUIDLeastMost(dynamic, "DragonUUID", "Dragon").orElse((Dynamic<?>)dynamic)))));
    }

    private Dynamic<?> updateCustomBossEvent(Dynamic<?> dynamic) {
        return dynamic.update("Players", dynamic3 -> dynamic.createList(dynamic3.asStream().map(dynamic -> LevelUUIDFix.createUUIDFromML(dynamic).orElseGet(() -> {
            LOGGER.warn("CustomBossEvents contains invalid UUIDs.");
            return dynamic;
        }))));
    }
}

