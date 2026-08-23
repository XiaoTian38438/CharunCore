/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import java.util.Optional;
import net.minecraft.util.datafix.fixes.References;

public class HeightmapRenamingFix
extends DataFix {
    public HeightmapRenamingFix(Schema schema, boolean bl) {
        super(schema, bl);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        Type<?> type = this.getInputSchema().getType(References.CHUNK);
        OpticFinder<?> opticFinder = type.findField("Level");
        return this.fixTypeEverywhereTyped("HeightmapRenamingFix", type, typed2 -> typed2.updateTyped(opticFinder, typed -> typed.update(DSL.remainderFinder(), this::fix)));
    }

    private Dynamic<?> fix(Dynamic<?> dynamic) {
        Optional<Dynamic<?>> optional;
        Optional<Dynamic<?>> optional2;
        Optional<Dynamic<?>> optional3;
        Optional<Dynamic<?>> optional4 = dynamic.get("Heightmaps").result();
        if (optional4.isEmpty()) {
            return dynamic;
        }
        Dynamic<?> dynamic2 = optional4.get();
        Optional<Dynamic<?>> optional5 = dynamic2.get("LIQUID").result();
        if (optional5.isPresent()) {
            dynamic2 = dynamic2.remove("LIQUID");
            dynamic2 = dynamic2.set("WORLD_SURFACE_WG", optional5.get());
        }
        if ((optional3 = dynamic2.get("SOLID").result()).isPresent()) {
            dynamic2 = dynamic2.remove("SOLID");
            dynamic2 = dynamic2.set("OCEAN_FLOOR_WG", optional3.get());
            dynamic2 = dynamic2.set("OCEAN_FLOOR", optional3.get());
        }
        if ((optional2 = dynamic2.get("LIGHT").result()).isPresent()) {
            dynamic2 = dynamic2.remove("LIGHT");
            dynamic2 = dynamic2.set("LIGHT_BLOCKING", optional2.get());
        }
        if ((optional = dynamic2.get("RAIN").result()).isPresent()) {
            dynamic2 = dynamic2.remove("RAIN");
            dynamic2 = dynamic2.set("MOTION_BLOCKING", optional.get());
            dynamic2 = dynamic2.set("MOTION_BLOCKING_NO_LEAVES", optional.get());
        }
        return dynamic.set("Heightmaps", dynamic2);
    }
}

