/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.util.datafix.fixes.References;

public class MobSpawnerEntityIdentifiersFix
extends DataFix {
    public MobSpawnerEntityIdentifiersFix(Schema schema, boolean bl) {
        super(schema, bl);
    }

    private Dynamic<?> fix(Dynamic<?> dynamic2) {
        Dynamic dynamic3;
        if (!"MobSpawner".equals(dynamic2.get("id").asString(""))) {
            return dynamic2;
        }
        Optional<String> optional = dynamic2.get("EntityId").asString().result();
        if (optional.isPresent()) {
            dynamic3 = DataFixUtils.orElse(dynamic2.get("SpawnData").result(), dynamic2.emptyMap());
            dynamic3 = dynamic3.set("id", dynamic3.createString(optional.get().isEmpty() ? "Pig" : optional.get()));
            dynamic2 = dynamic2.set("SpawnData", dynamic3);
            dynamic2 = dynamic2.remove("EntityId");
        }
        if (((Optional)((Object)(dynamic3 = dynamic2.get("SpawnPotentials").asStreamOpt().result()))).isPresent()) {
            dynamic2 = dynamic2.set("SpawnPotentials", dynamic2.createList(((Stream)((Optional)((Object)dynamic3)).get()).map(dynamic -> {
                Optional<String> optional = dynamic.get("Type").asString().result();
                if (optional.isPresent()) {
                    Dynamic dynamic2 = DataFixUtils.orElse(dynamic.get("Properties").result(), dynamic.emptyMap()).set("id", dynamic.createString(optional.get()));
                    return dynamic.set("Entity", dynamic2).remove("Type").remove("Properties");
                }
                return dynamic;
            })));
        }
        return dynamic2;
    }

    @Override
    public TypeRewriteRule makeRule() {
        Type<?> type = this.getOutputSchema().getType(References.UNTAGGED_SPAWNER);
        return this.fixTypeEverywhereTyped("MobSpawnerEntityIdentifiersFix", this.getInputSchema().getType(References.UNTAGGED_SPAWNER), type, (Typed<?> typed) -> {
            Dynamic dynamic = typed.get(DSL.remainderFinder());
            DataResult dataResult = type.readTyped(this.fix(dynamic = dynamic.set("id", dynamic.createString("MobSpawner"))));
            if (dataResult.result().isEmpty()) {
                return typed;
            }
            return dataResult.result().get().getFirst();
        });
    }
}

