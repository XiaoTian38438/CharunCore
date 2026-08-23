/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Suppliers
 */
package net.minecraft.util.datafix.fixes;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;
import java.util.function.Supplier;
import net.minecraft.util.datafix.ExtraDataFixUtils;
import net.minecraft.util.datafix.fixes.EntityRenameFix;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

public class ThrownPotionSplitFix
extends EntityRenameFix {
    private final Supplier<ItemIdFinder> itemIdFinder = Suppliers.memoize(() -> {
        Type<?> type = this.getInputSchema().getChoiceType(References.ENTITY, "minecraft:potion");
        Type<?> type2 = ExtraDataFixUtils.patchSubType(type, this.getInputSchema().getType(References.ENTITY), this.getOutputSchema().getType(References.ENTITY));
        OpticFinder<?> opticFinder = type2.findField("Item");
        OpticFinder<Pair<String, String>> opticFinder2 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
        return new ItemIdFinder(opticFinder, opticFinder2);
    });

    public ThrownPotionSplitFix(Schema schema) {
        super("ThrownPotionSplitFix", schema, true);
    }

    @Override
    protected Pair<String, Typed<?>> fix(String string, Typed<?> typed) {
        if (!string.equals("minecraft:potion")) {
            return Pair.of(string, typed);
        }
        String string2 = this.itemIdFinder.get().getItemId(typed);
        if ("minecraft:lingering_potion".equals(string2)) {
            return Pair.of("minecraft:lingering_potion", typed);
        }
        return Pair.of("minecraft:splash_potion", typed);
    }

    record ItemIdFinder(OpticFinder<?> itemFinder, OpticFinder<Pair<String, String>> itemIdFinder) {
        public String getItemId(Typed<?> typed2) {
            return typed2.getOptionalTyped(this.itemFinder).flatMap(typed -> typed.getOptional(this.itemIdFinder)).map(Pair::getSecond).map(NamespacedSchema::ensureNamespaced).orElse("");
        }
    }
}

