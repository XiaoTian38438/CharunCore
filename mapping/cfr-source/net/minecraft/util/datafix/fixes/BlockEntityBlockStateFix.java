/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.datafix.fixes.BlockStateData;
import net.minecraft.util.datafix.fixes.NamedEntityFix;
import net.minecraft.util.datafix.fixes.References;

public class BlockEntityBlockStateFix
extends NamedEntityFix {
    public BlockEntityBlockStateFix(Schema schema, boolean bl) {
        super(schema, bl, "BlockEntityBlockStateFix", References.BLOCK_ENTITY, "minecraft:piston");
    }

    @Override
    protected Typed<?> fix(Typed<?> typed) {
        Type<?> type = this.getOutputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:piston");
        Type<?> type2 = type.findFieldType("blockState");
        OpticFinder<?> opticFinder = DSL.fieldFinder("blockState", type2);
        Dynamic<?> dynamic = typed.get(DSL.remainderFinder());
        int n = dynamic.get("blockId").asInt(0);
        dynamic = dynamic.remove("blockId");
        int n2 = dynamic.get("blockData").asInt(0) & 0xF;
        dynamic = dynamic.remove("blockData");
        Dynamic<?> dynamic2 = BlockStateData.getTag(n << 4 | n2);
        Typed<?> typed2 = type.pointTyped(typed.getOps()).orElseThrow(() -> new IllegalStateException("Could not create new piston block entity."));
        return typed2.set(DSL.remainderFinder(), dynamic).set(opticFinder, type2.readTyped(dynamic2).result().orElseThrow(() -> new IllegalStateException("Could not parse newly created block state tag.")).getFirst());
    }
}

