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
import com.mojang.datafixers.types.templates.List;
import net.minecraft.util.datafix.ExtraDataFixUtils;
import net.minecraft.util.datafix.fixes.References;

public class MapBannerBlockPosFormatFix
extends DataFix {
    public MapBannerBlockPosFormatFix(Schema schema) {
        super(schema, false);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        Type<?> type = this.getInputSchema().getType(References.SAVED_DATA_MAP_DATA);
        OpticFinder<?> opticFinder = type.findField("data");
        OpticFinder<?> opticFinder2 = opticFinder.type().findField("banners");
        OpticFinder opticFinder3 = DSL.typeFinder(((List.ListType)opticFinder2.type()).getElement());
        return this.fixTypeEverywhereTyped("MapBannerBlockPosFormatFix", type, typed2 -> typed2.updateTyped(opticFinder, typed -> typed.updateTyped(opticFinder2, typed2 -> typed2.updateTyped(opticFinder3, typed -> typed.update(DSL.remainderFinder(), dynamic -> dynamic.update("Pos", ExtraDataFixUtils::fixBlockPos))))));
    }
}

