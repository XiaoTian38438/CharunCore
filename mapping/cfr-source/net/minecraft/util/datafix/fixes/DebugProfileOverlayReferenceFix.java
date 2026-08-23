/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import net.minecraft.util.datafix.fixes.References;

public class DebugProfileOverlayReferenceFix
extends DataFix {
    public DebugProfileOverlayReferenceFix(Schema schema) {
        super(schema, false);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("DebugProfileOverlayReferenceFix", this.getInputSchema().getType(References.DEBUG_PROFILE), typed -> typed.update(DSL.remainderFinder(), dynamic2 -> dynamic2.update("custom", dynamic -> dynamic.updateMapValues(pair -> pair.mapSecond(dynamic -> {
            if (dynamic.asString("").equals("inF3")) {
                return dynamic.createString("inOverlay");
            }
            return dynamic;
        })))));
    }
}

