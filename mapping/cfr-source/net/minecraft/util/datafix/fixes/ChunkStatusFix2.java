/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 */
package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import java.util.Map;
import java.util.Objects;
import net.minecraft.util.datafix.fixes.References;

public class ChunkStatusFix2
extends DataFix {
    private static final Map<String, String> RENAMES_AND_DOWNGRADES = ImmutableMap.builder().put((Object)"structure_references", (Object)"empty").put((Object)"biomes", (Object)"empty").put((Object)"base", (Object)"surface").put((Object)"carved", (Object)"carvers").put((Object)"liquid_carved", (Object)"liquid_carvers").put((Object)"decorated", (Object)"features").put((Object)"lighted", (Object)"light").put((Object)"mobs_spawned", (Object)"spawn").put((Object)"finalized", (Object)"heightmaps").put((Object)"fullchunk", (Object)"full").build();

    public ChunkStatusFix2(Schema schema, boolean bl) {
        super(schema, bl);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        Type<?> type = this.getInputSchema().getType(References.CHUNK);
        Type<?> type2 = type.findFieldType("Level");
        OpticFinder<?> opticFinder = DSL.fieldFinder("Level", type2);
        return this.fixTypeEverywhereTyped("ChunkStatusFix2", type, this.getOutputSchema().getType(References.CHUNK), (Typed<?> typed2) -> typed2.updateTyped(opticFinder, typed -> {
            String string;
            Dynamic dynamic = typed.get(DSL.remainderFinder());
            String string2 = dynamic.get("Status").asString("empty");
            if (Objects.equals(string2, string = RENAMES_AND_DOWNGRADES.getOrDefault(string2, "empty"))) {
                return typed;
            }
            return typed.set(DSL.remainderFinder(), dynamic.set("Status", dynamic.createString(string)));
        }));
    }
}

