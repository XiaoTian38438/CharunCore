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
import com.mojang.datafixers.types.templates.List;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.datafix.ExtraDataFixUtils;
import net.minecraft.util.datafix.fixes.References;

public class BeehiveFieldRenameFix
extends DataFix {
    public BeehiveFieldRenameFix(Schema schema) {
        super(schema, true);
    }

    private Dynamic<?> fixBeehive(Dynamic<?> dynamic) {
        return dynamic.remove("Bees");
    }

    private Dynamic<?> fixBee(Dynamic<?> dynamic) {
        dynamic = dynamic.remove("EntityData");
        dynamic = dynamic.renameField("TicksInHive", "ticks_in_hive");
        dynamic = dynamic.renameField("MinOccupationTicks", "min_ticks_in_hive");
        return dynamic;
    }

    @Override
    public TypeRewriteRule makeRule() {
        Type<?> type = this.getInputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:beehive");
        OpticFinder<?> opticFinder = DSL.namedChoice("minecraft:beehive", type);
        List.ListType listType = (List.ListType)type.findFieldType("Bees");
        Type type2 = listType.getElement();
        OpticFinder opticFinder2 = DSL.fieldFinder("Bees", listType);
        OpticFinder opticFinder3 = DSL.typeFinder(type2);
        Type<?> type3 = this.getInputSchema().getType(References.BLOCK_ENTITY);
        Type<?> type4 = this.getOutputSchema().getType(References.BLOCK_ENTITY);
        return this.fixTypeEverywhereTyped("BeehiveFieldRenameFix", type3, type4, (Typed<?> typed2) -> ExtraDataFixUtils.cast(type4, typed2.updateTyped(opticFinder, typed -> typed.update(DSL.remainderFinder(), this::fixBeehive).updateTyped(opticFinder2, typed2 -> typed2.updateTyped(opticFinder3, typed -> typed.update(DSL.remainderFinder(), this::fixBee))))));
    }
}

