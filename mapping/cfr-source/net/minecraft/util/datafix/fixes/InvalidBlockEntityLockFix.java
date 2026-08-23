/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import java.util.Optional;
import net.minecraft.util.datafix.fixes.InvalidLockComponentFix;
import net.minecraft.util.datafix.fixes.References;

public class InvalidBlockEntityLockFix
extends DataFix {
    public InvalidBlockEntityLockFix(Schema schema) {
        super(schema, false);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("BlockEntityLockToComponentFix", this.getInputSchema().getType(References.BLOCK_ENTITY), typed -> typed.update(DSL.remainderFinder(), dynamic -> {
            Optional optional = dynamic.get("lock").result();
            if (optional.isEmpty()) {
                return dynamic;
            }
            Dynamic dynamic2 = InvalidLockComponentFix.fixLock(optional.get());
            if (dynamic2 != null) {
                return dynamic.set("lock", dynamic2);
            }
            return dynamic.remove("lock");
        }));
    }
}

