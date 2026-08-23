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
import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
import net.minecraft.util.datafix.fixes.References;

public class ScoreboardDisplayNameFix
extends DataFix {
    private final String name;
    private final DSL.TypeReference type;

    public ScoreboardDisplayNameFix(Schema schema, String string, DSL.TypeReference typeReference) {
        super(schema, false);
        this.name = string;
        this.type = typeReference;
    }

    @Override
    protected TypeRewriteRule makeRule() {
        Type<?> type = this.getInputSchema().getType(this.type);
        OpticFinder<?> opticFinder = type.findField("DisplayName");
        OpticFinder<?> opticFinder2 = DSL.typeFinder(this.getInputSchema().getType(References.TEXT_COMPONENT));
        return this.fixTypeEverywhereTyped(this.name, type, typed2 -> typed2.updateTyped(opticFinder, typed -> typed.update(opticFinder2, pair -> pair.mapSecond(LegacyComponentDataFixUtils::createTextComponentJson))));
    }
}

