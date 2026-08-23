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
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.util.datafix.fixes.References;

public class EquipmentFormatFix
extends DataFix {
    public EquipmentFormatFix(Schema schema) {
        super(schema, true);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        Type<?> type = this.getInputSchema().getTypeRaw(References.ITEM_STACK);
        Type<?> type2 = this.getOutputSchema().getTypeRaw(References.ITEM_STACK);
        OpticFinder<?> opticFinder = type.findField("id");
        return this.fix(type, type2, opticFinder);
    }

    private <ItemStackOld, ItemStackNew> TypeRewriteRule fix(Type<ItemStackOld> type, Type<ItemStackNew> type2, OpticFinder<?> opticFinder) {
        Type<Pair<String, Pair<Either<ItemStackOld, Unit>, Pair<Either<ItemStackOld, Unit>, Pair<Either<ItemStackOld, Unit>, Either<ItemStackOld, Unit>>>>>> type3 = DSL.named(References.ENTITY_EQUIPMENT.typeName(), DSL.and(DSL.optional(DSL.field("ArmorItems", DSL.list(type))), DSL.optional(DSL.field("HandItems", DSL.list(type))), DSL.optional(DSL.field("body_armor_item", type)), DSL.optional(DSL.field("saddle", type))));
        Type<Pair<String, Either<Pair<Either<ItemStackNew, Unit>, Pair<Either<ItemStackNew, Unit>, Pair<Either<ItemStackNew, Unit>, Pair<Either<ItemStackNew, Unit>, Pair<Either<ItemStackNew, Unit>, Pair<Either<ItemStackNew, Unit>, Pair<Either<ItemStackNew, Unit>, Pair<Either<ItemStackNew, Unit>, Dynamic<?>>>>>>>>>, Unit>>> type4 = DSL.named(References.ENTITY_EQUIPMENT.typeName(), DSL.optional(DSL.field("equipment", DSL.and(DSL.optional(DSL.field("mainhand", type2)), DSL.optional(DSL.field("offhand", type2)), DSL.optional(DSL.field("feet", type2)), DSL.and(DSL.optional(DSL.field("legs", type2)), DSL.optional(DSL.field("chest", type2)), DSL.optional(DSL.field("head", type2)), DSL.and(DSL.optional(DSL.field("body", type2)), DSL.optional(DSL.field("saddle", type2)), DSL.remainderType()))))));
        if (!type3.equals(this.getInputSchema().getType(References.ENTITY_EQUIPMENT))) {
            throw new IllegalStateException("Input entity_equipment type does not match expected");
        }
        if (!type4.equals(this.getOutputSchema().getType(References.ENTITY_EQUIPMENT))) {
            throw new IllegalStateException("Output entity_equipment type does not match expected");
        }
        return this.fixTypeEverywhere("EquipmentFormatFix", type3, type4, dynamicOps -> {
            Predicate<Object> predicate = object -> {
                Typed<Object> typed = new Typed<Object>(type, (DynamicOps<?>)dynamicOps, object);
                return typed.getOptional(opticFinder).isEmpty();
            };
            return pair -> {
                String string = (String)pair.getFirst();
                Pair pair2 = (Pair)pair.getSecond();
                List list = ((Either)pair2.getFirst()).map(Function.identity(), unit -> List.of());
                List list2 = ((Either)((Pair)pair2.getSecond()).getFirst()).map(Function.identity(), unit -> List.of());
                Either either = (Either)((Pair)((Pair)pair2.getSecond()).getSecond()).getFirst();
                Either either2 = (Either)((Pair)((Pair)pair2.getSecond()).getSecond()).getSecond();
                Either either3 = EquipmentFormatFix.getItemFromList(0, list, predicate);
                Either either4 = EquipmentFormatFix.getItemFromList(1, list, predicate);
                Either either5 = EquipmentFormatFix.getItemFromList(2, list, predicate);
                Either either6 = EquipmentFormatFix.getItemFromList(3, list, predicate);
                Either either7 = EquipmentFormatFix.getItemFromList(0, list2, predicate);
                Either either8 = EquipmentFormatFix.getItemFromList(1, list2, predicate);
                if (EquipmentFormatFix.areAllEmpty(either, either2, either3, either4, either5, either6, either7, either8)) {
                    return Pair.of(string, Either.right(Unit.INSTANCE));
                }
                return Pair.of(string, Either.left(Pair.of(either7, Pair.of(either8, Pair.of(either3, Pair.of(either4, Pair.of(either5, Pair.of(either6, Pair.of(either, Pair.of(either2, new Dynamic(dynamicOps)))))))))));
            };
        });
    }

    @SafeVarargs
    private static boolean areAllEmpty(Either<?, Unit> ... eitherArray) {
        for (Either<?, Unit> either : eitherArray) {
            if (!either.right().isEmpty()) continue;
            return false;
        }
        return true;
    }

    private static <ItemStack> Either<ItemStack, Unit> getItemFromList(int n, List<ItemStack> list, Predicate<ItemStack> predicate) {
        if (n >= list.size()) {
            return Either.right(Unit.INSTANCE);
        }
        ItemStack ItemStack2 = list.get(n);
        if (predicate.test(ItemStack2)) {
            return Either.right(Unit.INSTANCE);
        }
        return Either.left(ItemStack2);
    }
}

