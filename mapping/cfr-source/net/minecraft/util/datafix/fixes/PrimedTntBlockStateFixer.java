/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import java.util.Map;
import java.util.Optional;
import net.minecraft.util.datafix.fixes.NamedEntityWriteReadFix;
import net.minecraft.util.datafix.fixes.References;

public class PrimedTntBlockStateFixer
extends NamedEntityWriteReadFix {
    public PrimedTntBlockStateFixer(Schema schema) {
        super(schema, true, "PrimedTnt BlockState fixer", References.ENTITY, "minecraft:tnt");
    }

    private static <T> Dynamic<T> renameFuse(Dynamic<T> dynamic) {
        Optional<Dynamic<T>> optional = dynamic.get("Fuse").get().result();
        if (optional.isPresent()) {
            return dynamic.set("fuse", optional.get());
        }
        return dynamic;
    }

    private static <T> Dynamic<T> insertBlockState(Dynamic<T> dynamic) {
        return dynamic.set("block_state", dynamic.createMap(Map.of(dynamic.createString("Name"), dynamic.createString("minecraft:tnt"))));
    }

    @Override
    protected <T> Dynamic<T> fix(Dynamic<T> dynamic) {
        return PrimedTntBlockStateFixer.renameFuse(PrimedTntBlockStateFixer.insertBlockState(dynamic));
    }
}

