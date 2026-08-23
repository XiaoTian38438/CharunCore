/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.util.datafix.fixes.References;

public class FeatureFlagRemoveFix
extends DataFix {
    private final String name;
    private final Set<String> flagsToRemove;

    public FeatureFlagRemoveFix(Schema schema, String string, Set<String> set) {
        super(schema, false);
        this.name = string;
        this.flagsToRemove = set;
    }

    @Override
    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped(this.name, this.getInputSchema().getType(References.LIGHTWEIGHT_LEVEL), typed -> typed.update(DSL.remainderFinder(), this::fixTag));
    }

    private <T> Dynamic<T> fixTag(Dynamic<T> dynamic) {
        List list = dynamic.get("removed_features").asStream().collect(Collectors.toCollection(ArrayList::new));
        Dynamic<T> dynamic3 = dynamic.update("enabled_features", dynamic2 -> DataFixUtils.orElse(dynamic2.asStreamOpt().result().map(stream -> stream.filter(dynamic2 -> {
            Optional<String> optional = dynamic2.asString().result();
            if (optional.isEmpty()) {
                return true;
            }
            boolean bl = this.flagsToRemove.contains(optional.get());
            if (bl) {
                list.add(dynamic.createString(optional.get()));
            }
            return !bl;
        })).map(dynamic::createList), dynamic2));
        if (!list.isEmpty()) {
            dynamic3 = dynamic3.set("removed_features", dynamic.createList(list.stream()));
        }
        return dynamic3;
    }
}

